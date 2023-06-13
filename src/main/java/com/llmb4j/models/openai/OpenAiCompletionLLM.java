package com.llmb4j.models.openai;

import cn.hutool.core.text.CharSequenceUtil;
import com.llmb4j.callbacks.BaseCallbackHandler;
import com.llmb4j.callbacks.CallbackWrapper;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.exception.ChatLLMException;
import com.llmb4j.models.base.BaseLanguageModel;
import com.llmb4j.models.openai.api.OpenAiService;
import com.llmb4j.util.LLmConstants;
import com.theokanning.openai.Usage;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import org.slf4j.Logger;

import java.util.*;

/**
 * @author LiangTao
 * @date 2023年06月13 13:39
 **/
public class OpenAiCompletionLLM implements BaseLanguageModel {
    private final OpenAiService openAiService;

    private OpenAiLLmConfig defaultChatConfig;

    private final Logger llmLogger = LLmConstants.llmLogger;

    public void setDefaultChatConfig(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
    }

    public OpenAiCompletionLLM() {
        this.defaultChatConfig = withDefault();
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiCompletionLLM(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiCompletionLLM(OpenAiLLmConfig defaultChatConfig, OpenAiService openAiService) {
        this.defaultChatConfig = defaultChatConfig;
        this.openAiService = openAiService;
    }

    private OpenAiLLmConfig withDefault() {
        OpenAiLLmConfig openAiChatConfig = new OpenAiLLmConfig();
        openAiChatConfig.setTemperature(0);
        openAiChatConfig.setPresencePenalty(0);
        openAiChatConfig.setModelName("gpt-3.5-turbo");
        openAiChatConfig.setN(1);
        openAiChatConfig.setStream(false);
        openAiChatConfig.setMaxTokens(3000);
        openAiChatConfig.setVerbose(true);
        return openAiChatConfig;
    }

    @Override
    public boolean isVerbose() {
        return defaultChatConfig.isVerbose();
    }


    @Override
    public LLMResult generateCompletion(List<String> prompts, List<String> stop, BaseCallbackHandler callbackHandler) {
        final BaseCallbackHandler callBack = Optional.ofNullable(callbackHandler).orElse(new CallbackWrapper(Collections.EMPTY_LIST));
        try {
            callBack.onLlmStart(Map.of("name", llmType()), prompts, null, null, null);
            List<List<Generation>> allGenerations = new ArrayList<>();
            if (getConfig().isStream()) {
                for (String prompt : prompts) {
                    List<Generation> generations = new ArrayList<>();
                    CompletionRequest request = createRequest(prompt, stop);
                    openAiService.streamCompletion(request)
                            .filter(item -> CharSequenceUtil.isNotEmpty(item.getChoices().get(0).getText()))
                            .map(response -> Generation.builder()
                                    .text(response.getChoices().get(0).getText())
                                    .generationInfo(Map.of("finishReason", response.getChoices().get(0).getFinish_reason()))
                                    .build()).doOnNext(g -> {
                                callBack.onLlmNewToken(g.getText(), null, null, null);
                                generations.add(g);
                            });
                    allGenerations.add(generations);
                }
            } else {
                for (String prompt : prompts) {
                    List<Generation> generations = new ArrayList<>();
                    CompletionRequest request = createRequest(prompt, stop);
                    CompletionResult result = openAiService.createCompletion(request);
                    Usage tokenUsage = result.getUsage();
                    Generation.GenerationBuilder builder = Generation.builder();
                    builder.generationInfo(Map.of(
                                    "completionTokens", tokenUsage.getCompletionTokens(),
                                    "totalTokens", tokenUsage.getTotalTokens(),
                                    "finishReason", result.getChoices().get(0).getFinish_reason()
                            )
                    );
                    builder.text(result.getChoices().get(0).getText());
                    Generation generation = builder.build();
                    generations.add(generation);
                    allGenerations.add(generations);
                }
            }
            LLMResult llmResult = new LLMResult(allGenerations, Map.of(
                    "name", llmType(),
                    "modelName", getConfig().getModelName()
            ));
            if (isVerbose()) {
                allGenerations.stream().map(item -> item.stream().map(Generation::getText).reduce("", (a, b) -> a + b)).forEach(llmLogger::info);
            }
            callBack.onLlmEnd(llmResult, null, null, null);
            return llmResult;


        } catch (Exception e) {
            callBack.onLlmError(e, null, null, null);
            if (callBack.throwError) {
                throw new ChatLLMException(e);
            }
        }
        return null;
    }

    public OpenAiLLmConfig getConfig() {
        return defaultChatConfig;
    }

    private CompletionRequest createRequest(String prompt, List<String> stop) {
        OpenAiLLmConfig config = getConfig();
        return CompletionRequest.builder()
                .temperature(config.getTemperature())
                .presencePenalty(config.getPresencePenalty())
                .n(config.getN())
                .stream(config.isStream())
                .model(config.getModelName())
                .stop(stop)
                .prompt(prompt).build();
    }


    @Override
    public String predictCompletion(String text, List<String> stop) {
        return generateCompletion(List.of(text), stop, null).getGenerations().get(0).stream().map(Generation::getText)
                .reduce("", (a, b) -> a + b);
    }

    @Override
    public String llmType() {
        return "openai";
    }

    @Override
    public String getModelName() {
        return defaultChatConfig.getModelName();
    }
}
