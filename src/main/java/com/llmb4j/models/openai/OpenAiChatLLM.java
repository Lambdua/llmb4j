package com.llmb4j.models.openai;// package com.llmb.models.openai;

import cn.hutool.core.text.CharSequenceUtil;
import com.llmb4j.callbacks.BaseCallbackHandler;
import com.llmb4j.callbacks.CallbackWrapper;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.exception.ChatLLMException;
import com.llmb4j.models.base.BaseLanguageModel;
import com.llmb4j.models.openai.api.OpenAiService;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.LLmConstants;
import com.llmb4j.util.PyScript;
import com.theokanning.openai.Usage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.*;

/**
 * @author LiangTao
 * @date 2023年05月29 11:16
 **/
@Slf4j
public class OpenAiChatLLM implements BaseLanguageModel {
    private final OpenAiService openAiService;

    private OpenAiLLmConfig defaultChatConfig;

    private final Logger llmLogger = LLmConstants.llmLogger;


    public void setDefaultChatConfig(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
    }

    public OpenAiChatLLM() {
        this.defaultChatConfig = withDefault();
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiChatLLM(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiChatLLM(OpenAiLLmConfig defaultChatConfig, OpenAiService openAiService) {
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

    public OpenAiLLmConfig getConfig() {
        return this.defaultChatConfig;
    }

    @Override
    public LLMResult generateChat(List<RoleMessage> prompts, List<String> stop, BaseCallbackHandler callbackHandler) {
        final BaseCallbackHandler callBack=Optional.ofNullable(callbackHandler).orElse(new CallbackWrapper(Collections.EMPTY_LIST));
        try {

            callBack.onLlmStart(Map.of("name", llmType()), prompts.stream().map(RoleMessage::toString).toList(), null, null, null);
            ChatCompletionRequest request = createRequest(prompts, stop);
            List<Generation> generations = new ArrayList<>();
            if (getConfig().isStream()) {
                openAiService.streamChatCompletion(request)
                        .filter(item -> CharSequenceUtil.isNotEmpty(item.getChoices().get(0).getMessage().getContent()))
                        .map(response -> Generation.builder()
                                .text(response.getChoices().get(0).getMessage().getContent())
                                .generationInfo(Map.of("finishReason", response.getChoices().get(0).getFinishReason()))
                                .build()).doOnNext(g -> {
                            callBack.onLlmNewToken(g.getText(), null, null, null);
                            generations.add(g);

                        });
            } else {
                ChatCompletionResult result = openAiService.createChatCompletion(request);
                Usage tokenUsage = result.getUsage();
                Generation.GenerationBuilder builder = Generation.builder();
                builder.generationInfo(Map.of("completionTokens", tokenUsage.getCompletionTokens(), "totalTokens", tokenUsage.getTotalTokens()));
                builder.text(result.getChoices().get(0).getMessage().getContent());
                Generation generation = builder.build();
                generations.add(generation);
            }
            LLMResult llmResult = new LLMResult(Arrays.asList(generations), Map.of(
                    "name", llmType(),
                    "modelName", getConfig().getModelName()
            ));
            if (isVerbose()) {
                String result = generations.stream().map(Generation::getText).reduce("", (a, b) -> a + b);
                llmLogger.info(result);
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

    @Override
    public RoleMessage predictChat(List<RoleMessage> messages, List<String> stop) {
        String response = generateChat(messages, stop, null).getGenerations().get(0).stream().map(Generation::getText).reduce("", (a, b) -> a + b);
        return new RoleMessage(response, ChatRole.AI);
    }

    private ChatCompletionRequest createRequest(List<RoleMessage> chatMsgs, List<String> stop) {
        List<ChatMessage> messages = new ArrayList<>();
        chatMsgs.stream()
                .map(chatRecord -> new ChatMessage(chatRecord.role().getValue(), chatRecord.content()))
                .forEach(messages::add);
        OpenAiLLmConfig config = getConfig();
        return ChatCompletionRequest.builder()
                .temperature(config.getTemperature())
                .presencePenalty(config.getPresencePenalty())
                .n(config.getN())
                .stream(config.isStream())
                .model(config.getModelName())
                .stop(stop)
                .messages(messages).build();
    }

    @Override
    public boolean isVerbose() {
        return getConfig().isVerbose();
    }

    @Override
    public String llmType() {
        return "openai";
    }

    @Override
    public String getModelName() {
        return defaultChatConfig.getModelName();
    }

    @Override
    public List<Integer> getTokenIds(String text) {
        return PyScript.tokenIds(text, getModelName());
    }


}
