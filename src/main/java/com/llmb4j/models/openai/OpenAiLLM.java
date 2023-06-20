package com.llmb4j.models.openai;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.llmb4j.callbacks.CallbackWrapper;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.exception.ChatLLMException;
import com.llmb4j.log.LLmLogStyle;
import com.llmb4j.models.base.BaseLLMChatPayload;
import com.llmb4j.models.base.BaseLLMCompletionPayload;
import com.llmb4j.models.base.BaseLanguageModel;
import com.llmb4j.models.openai.base.OpenAiModels;
import com.llmb4j.models.openai.base.Usage;
import com.llmb4j.models.openai.completion.CompletionChoice;
import com.llmb4j.models.openai.completion.CompletionRequest;
import com.llmb4j.models.openai.completion.CompletionResult;
import com.llmb4j.models.openai.completion.chat.*;
import com.llmb4j.models.openai.service.OpenAiService;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.ChatMsgUtil;
import com.llmb4j.util.LLmConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author LiangTao
 * @date 2023年05月29 11:16
 **/
@Slf4j
public class OpenAiLLM implements BaseLanguageModel<OpenAiLLmConfig> {
    private final OpenAiService openAiService;

    private OpenAiLLmConfig defaultChatConfig;

    private static final Logger llmLogger = LLmConstants.llmLogger;


    public void setDefaultChatConfig(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
    }

    public OpenAiLLM() {
        this.defaultChatConfig = withDefaultConfig();
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiLLM(OpenAiLLmConfig defaultConfig) {
        this.defaultChatConfig = defaultConfig;
        this.openAiService = LLmConstants.defaultOpenAiService;
    }

    public OpenAiLLM(OpenAiLLmConfig defaultChatConfig, OpenAiService openAiService) {
        this.defaultChatConfig = defaultChatConfig;
        this.openAiService = openAiService;
    }

    public static OpenAiLLmConfig withDefaultConfig() {
        OpenAiLLmConfig openAiChatConfig = new OpenAiLLmConfig();
        openAiChatConfig.setDefaultChatModelName(OpenAiModels.gpt35turbo);
        openAiChatConfig.setDefaultCompletionModelName(OpenAiModels.textdavinci003);
        openAiChatConfig.setDefaultEmbeddingModelName(OpenAiModels.textembeddedada002);
        openAiChatConfig.setCallbackHandlers(new ArrayList<>());
        return openAiChatConfig;
    }

    public static OpenAiLLmChatPayload withDefaultChatModel() {
        OpenAiLLmChatPayload openAiLLmChatPayload = new OpenAiLLmChatPayload();
        openAiLLmChatPayload.setStream(false);
        openAiLLmChatPayload.setVerbose(false);
        openAiLLmChatPayload.setModelName(OpenAiModels.gpt35turbo);
        openAiLLmChatPayload.setN(1);
        openAiLLmChatPayload.setTemperature(0.5D);
        return openAiLLmChatPayload;
    }

    public static OpenAiLLmCompletionPayload withDefaultCompletionModel() {
        OpenAiLLmCompletionPayload openAiLLmCompletionPayload = new OpenAiLLmCompletionPayload();
        openAiLLmCompletionPayload.setStream(false);
        openAiLLmCompletionPayload.setVerbose(false);
        openAiLLmCompletionPayload.setModelName(OpenAiModels.textdavinci003);
        openAiLLmCompletionPayload.setN(1);
        openAiLLmCompletionPayload.setTemperature(0.5D);
        return openAiLLmCompletionPayload;
    }


    @Override
    public OpenAiLLmConfig getConfig() {
        return this.defaultChatConfig;
    }

    @Override
    public String llmType() {
        return "openai";
    }


    @Override
    public <P extends BaseLLMChatPayload> List<RoleMessage> generateChat(P payload) {
        if (payload instanceof OpenAiLLmChatPayload openAiChatPayload) {
            Assert.isTrue(payload.chatHistory.stream().allMatch(OpenAiRoleMessage.class::isInstance),
                    "OpenAiLLmChatPayload.chatHistory must be OpenAiRoleMessage");
            final CallbackWrapper callBack = new CallbackWrapper(getConfig().getCallbackHandlers());
            if (openAiChatPayload.callbackHandler != null) {
                callBack.addCallback(openAiChatPayload.callbackHandler);
            }
            try {
                if (payload.verbose) {
                    MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.CONFIG.type);
                    llmLogger.info("generateChat configPayload: {}",payload);
                }

                List<? extends RoleMessage> chatHistory = payload.chatHistory;
                callBack.onChatModelStart(Map.of("name", llmType()), chatHistory, null, null, null);
                ChatCompletionRequest request = createRequest(openAiChatPayload);

                int totalTokens;
                int promptTokens;
                int completionTokens;
                String finishReason;

                List<List<Generation>> generations = new ArrayList<>();
                if (payload.stream) {
                    List<Generation> streamGenerations = new ArrayList<>();
                    openAiService.streamChatCompletion(request)
                            .filter(item -> CharSequenceUtil.isNotEmpty(item.getChoices().get(0).getMessage().getContent()))
                            .doOnNext(response -> {
                                Generation g = Generation.builder()
                                        .text(response.getChoices().get(0).getMessage().getContent())
                                        .generationInfo(BeanUtil.beanToMap(response.getChoices().get(0).getMessage()))
                                        .build();
                                g.getGenerationInfo().put("finishReason", response.getChoices().get(0).getFinishReason());
                                callBack.onLlmNewToken(g.getText(), null, null, null);
                                streamGenerations.add(g);
                            }).blockingSubscribe();
                    completionTokens = getNumTokens(streamGenerations.stream().map(Generation::getText).reduce("", (a, b) -> a + b), payload.modelName);
                    promptTokens = getNumTokensFromMessages(chatHistory, payload.modelName);
                    totalTokens = promptTokens + completionTokens;
                    finishReason = MapUtil.getStr(streamGenerations.get(streamGenerations.size() - 1).getGenerationInfo(), "finishReason", "");
                    generations.add(streamGenerations);
                } else {
                    ChatCompletionResult result = openAiService.createChatCompletion(request);
                    Usage tokenUsage = result.getUsage();
                    List<ChatCompletionChoice> choices = result.getChoices();

                    promptTokens = (int) tokenUsage.getPromptTokens();
                    completionTokens = (int) tokenUsage.getCompletionTokens();
                    totalTokens = promptTokens + completionTokens;
                    finishReason = choices.get(choices.size() - 1).getFinishReason();

                    choices.stream().map(choice -> {
                        Generation.GenerationBuilder builder = Generation.builder();
                        builder.text(choice.getMessage().getContent());
                        Map<String, Object> generationInfo = BeanUtil.beanToMap(choice.getMessage());
                        generationInfo.put("finishReason", choice.getFinishReason());
                        builder.generationInfo(generationInfo);
                        return builder.build();
                    }).forEach(generation -> generations.add(Collections.singletonList(generation)));
                }
                LLMResult llmResult = new LLMResult(generations, Map.of(
                        "totalTokens", totalTokens,
                        "promptTokens", promptTokens,
                        "completionTokens", completionTokens,
                        "finishReason", finishReason

                ));
                if (payload.verbose) {
                    MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.INPUT.type);
                    String input = ChatMsgUtil.getBufferString(chatHistory);
                    llmLogger.info(input);
                }
                callBack.onLlmEnd(llmResult, null, null, null);
                return llmResult.getGenerations().stream().map(itemGenerations -> {
                    Map<String, Object> itemInfo = itemGenerations.get(0).getGenerationInfo();
                    ChatFunctionCall functionCall = MapUtil.get(itemInfo, "functionCall", ChatFunctionCall.class);
                    String content;
                    if (functionCall != null) {
                        content = "use funcation call;funcation name:" + functionCall.getName() + ",args:" + functionCall.getArguments().toString();
                    } else {
                        content = itemGenerations.stream().map(Generation::getText).reduce("", String::concat);
                    }

                    if (payload.verbose) {
                        MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.OUTPUT.type);
                        llmLogger.info(content);
                    }
                    return (RoleMessage) new OpenAiRoleMessage(content, ChatRole.AI,
                            MapUtil.getStr(itemInfo, "name"),
                            functionCall
                    );
                }).toList();
            } catch (Exception e) {
                callBack.onLlmError(e, null, null, null);
                if (callBack.isThrowError()) {
                    throw new ChatLLMException(e);
                }
            }finally {
                MDC.remove(LLmConstants.llmLogTypeKey);
            }
            return Collections.emptyList();
        }
        return BaseLanguageModel.super.generateChat(payload);
    }

    @Override
    public <P extends BaseLLMCompletionPayload> LLMResult generateCompletion(P payload) {
        if (payload instanceof OpenAiLLmCompletionPayload openAiPayload) {
            final CallbackWrapper callBack = new CallbackWrapper(getConfig().getCallbackHandlers());
            if (openAiPayload.callbackHandler != null) {
                callBack.addCallback(openAiPayload.callbackHandler);
            }
            try {
                if (openAiPayload.verbose){
                    MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.CONFIG.type);
                    llmLogger.info("generateCompletion configPayload: {}",openAiPayload);
                }
                List<String> allPrompts = payload.getPrompts();
                callBack.onLlmStart(Map.of("name", llmType()), allPrompts, null, null, null);
                List<List<Generation>> allGenerations = new ArrayList<>();
                AtomicInteger totalTokens = new AtomicInteger(0);
                AtomicInteger promptTokens = new AtomicInteger(0);
                AtomicInteger completionTokens = new AtomicInteger(0);
                AtomicReference<String> finishReason = new AtomicReference<>("");
                if (payload.stream) {
                    allPrompts.stream().map(prompt -> {
                        CompletionRequest request = createRequest(openAiPayload, prompt);
                        List<Generation> streamItemGeneration = new ArrayList<>();
                        openAiService.streamCompletion(request).doOnNext(response -> {
                            CompletionChoice completionChoice = response.getChoices().get(0);
                            Generation generation = new Generation(completionChoice.getText(), BeanUtil.beanToMap(completionChoice));
                            callBack.onLlmNewToken(generation.getText(), null, null, null);
                            streamItemGeneration.add(generation);
                        }).blockingSubscribe();
                        promptTokens.addAndGet(getNumTokens(prompt, payload.modelName));
                        completionTokens.addAndGet(getNumTokens(streamItemGeneration.stream().map(Generation::getText).reduce("", String::concat), payload.modelName));
                        totalTokens.addAndGet(promptTokens.get() + completionTokens.get());
                        finishReason.set(MapUtil.getStr(streamItemGeneration.get(streamItemGeneration.size() - 1).getGenerationInfo(), "finish_reason", ""));
                        return streamItemGeneration;
                    }).forEach(allGenerations::add);
                } else {
                    allPrompts.stream().map(prompt -> {
                        CompletionRequest request = createRequest(openAiPayload, prompt);

                        CompletionResult result = openAiService.createCompletion(request);
                        List<CompletionChoice> choices = result.getChoices();
                        Usage usage = result.getUsage();
                        totalTokens.addAndGet((int) usage.getTotalTokens());
                        promptTokens.addAndGet((int) usage.getPromptTokens());
                        completionTokens.addAndGet((int) usage.getCompletionTokens());
                        finishReason.set(choices.get(choices.size() - 1).getFinish_reason());
                        return choices.stream().map(choice -> new Generation(choice.getText(), BeanUtil.beanToMap(choice))).toList();
                    }).flatMap(List::stream).forEach(gen -> allGenerations.add(Collections.singletonList(gen)));
                }
                LLMResult result = new LLMResult(allGenerations, Map.of(
                        "totalTokens", totalTokens,
                        "promptTokens", promptTokens,
                        "completionTokens", completionTokens,
                        "finishReason", finishReason
                ));
                callBack.onLlmEnd(result, null, null, null);
                if (payload.verbose) {
                    MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.INPUT.type);
                    allPrompts.forEach(llmLogger::info);
                    //callback中可能修改了result
                    List<List<Generation>> finalGenerations = result.getGenerations();
                    for (int i = 0; i < finalGenerations.size(); i++) {
                        MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.OUTPUT.type);
                        List<Generation> itemGenerations = finalGenerations.get(i);
                        String itemResponse = itemGenerations.stream().map(Generation::getText).reduce("", (a, b) -> a + b);
                        llmLogger.info(itemResponse);
                    }
                    MDC.remove(LLmConstants.llmLogTypeKey);
                }
                return result;
            } catch (Exception e) {
                callBack.onLlmError(e, null, null, null);
                if (callBack.isThrowError()) {
                    throw new ChatLLMException(e);
                }
                return null;
            }finally {
                MDC.remove(LLmConstants.llmLogTypeKey);
            }
        }
        return BaseLanguageModel.super.generateCompletion(payload);
    }


    @Override
    public String predictCompletion(String text, List<String> stop) {
        OpenAiLLmCompletionPayload completionPayload = withDefaultCompletionModel();
        completionPayload.setPrompts(Collections.singletonList(text));
        completionPayload.setStop(stop);
        return generateCompletion(completionPayload).getGenerations().get(0).get(0).getText();
    }


    private CompletionRequest createRequest(OpenAiLLmCompletionPayload openAiPayload, String prompt) {
        return CompletionRequest.builder()
                .prompt(prompt)
                .maxTokens(openAiPayload.maxTokens)
                .temperature(openAiPayload.temperature)
                .topP(openAiPayload.topP)
                .echo(openAiPayload.echo)
                .logitBias(openAiPayload.logitBias)
                .bestOf(openAiPayload.bestOf)
                .model(openAiPayload.modelName)
                .stop(openAiPayload.stop)
                .n(openAiPayload.n)
                .user(openAiPayload.userIdentity)
                .presencePenalty(openAiPayload.presencePenalty)
                .frequencyPenalty(openAiPayload.frequencyPenalty)
                .suffix(openAiPayload.suffix)
                .stream(openAiPayload.stream)
                .logprobs(openAiPayload.logprobs)
                .build();
    }


    private ChatCompletionRequest createRequest(OpenAiLLmChatPayload payload) {
        List<ChatMessage> messages = new ArrayList<>();
        payload.chatHistory.stream()
                .map(OpenAiRoleMessage.class::cast)
                .map(chatRecord ->
                        new ChatMessage(
                                switch (chatRecord.getRole()) {
                                    case SYSTEM -> "system";
                                    case HUMAN -> "user";
                                    case AI -> "assistant";
                                    case FUNCTION -> "function";
                                    default ->
                                            throw new IllegalStateException("Unexpected value: " + chatRecord.getRole());
                                }, chatRecord.getContent(), chatRecord.getName(), chatRecord.getFunctionCall())
                ).forEach(messages::add);
        return ChatCompletionRequest.builder()
                .temperature(payload.temperature)
                .presencePenalty(payload.presencePenalty)
                .frequencyPenalty(payload.frequencyPenalty)
                .maxTokens(payload.maxTokens)
                .logitBias(payload.logitBias)
                .topP(payload.topP)
                .user(payload.userIdentity)
                .n(payload.n)
                .stream(payload.stream)
                .model(payload.modelName)
                .stop(payload.stop)
                .functions(payload.functions)
                .functionCall(payload.functionCall)
                .messages(messages).build();
    }


}
