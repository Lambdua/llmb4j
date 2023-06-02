package com.llmb.models.impl.openai;

import cn.hutool.core.text.CharSequenceUtil;
import com.llmb.models.base.AbstractLLMModel;
import com.llmb.models.base.PoolProperties;
import com.llmb.models.impl.openai.api.OpenAiService;
import com.llmb.prompt.chat.ChatMessage;
import com.llmb.prompt.chat.ChatRole;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.llmb.util.SettingUtil.PROMPT;

/**
 * @author LiangTao
 * @date 2023年05月29 11:16
 **/
@Slf4j
public class OpenAiLLM extends AbstractLLMModel<OpenAiChatConfig, ChatMessage, ChatMessage> {
    private final OpenAiService openAiService;

    private OpenAiChatConfig defaultChatConfig;


    public void setDefaultChatConfig(OpenAiChatConfig defaultChatConfig) {
        this.defaultChatConfig = defaultChatConfig;
    }

    public OpenAiLLM() {
        this.defaultChatConfig = withDefault();
        this.openAiService = new OpenAiService(OpenAiChatConfig.apiKey, Duration.ofSeconds(30), OpenAiChatConfig.baseUrl, new PoolProperties());
    }

    public OpenAiLLM(OpenAiChatConfig defaultChatConfig){
        this.defaultChatConfig = defaultChatConfig;
        this.openAiService = new OpenAiService(OpenAiChatConfig.apiKey, Duration.ofSeconds(30), OpenAiChatConfig.baseUrl, new PoolProperties());
    }

    public OpenAiLLM(OpenAiChatConfig defaultChatConfig, OpenAiService openAiService){
        this.defaultChatConfig = defaultChatConfig;
        this.openAiService = openAiService;
    }




    private OpenAiChatConfig withDefault() {
        OpenAiChatConfig openAiChatConfig = new OpenAiChatConfig();
        openAiChatConfig.setSystemPrompt(PROMPT.get("prompt", "defaultSystemPrompt"));
        openAiChatConfig.setTemperature(0);
        openAiChatConfig.setPresencePenalty(0);
        openAiChatConfig.setUseModel("gpt-3.5-turbo");
        openAiChatConfig.setN(1);
        return openAiChatConfig;
    }


    @Override
    public OpenAiChatConfig getChatConfig() {
       return this.defaultChatConfig;
    }

    @Override
    public Flowable<ChatMessage> doStreamChat(OpenAiChatConfig chatConfig, List<ChatMessage> chatMsgs) {
        return openAiService.streamChatCompletion(createRequest(chatConfig, chatMsgs, true))
                .filter(item-> CharSequenceUtil.isNotEmpty(item.getChoices().get(0).getMessage().getContent()))
                .map(response -> {
            ChatCompletionChoice chatCompletionChoice = response.getChoices().get(0);
            return new ChatMessage(chatCompletionChoice.getMessage().getContent(), ChatRole.AI);
        });
    }


    @Override
    public List<ChatMessage> doFullChat(OpenAiChatConfig chatConfig, List<ChatMessage> chatMsgs) {
        List<ChatMessage> chatMessageResults = openAiService.createChatCompletion(createRequest(chatConfig, chatMsgs, false))
                .getChoices().stream()
                .map(response -> new ChatMessage(response.getMessage().getContent(), ChatRole.AI))
                .toList();
        return chatMessageResults;
    }


    private ChatCompletionRequest createRequest(OpenAiChatConfig chatConfig, List<ChatMessage> chatMsgs, boolean isStream) {
        List<com.theokanning.openai.completion.chat.ChatMessage> messages = new ArrayList<>();
        messages.add(0, new com.theokanning.openai.completion.chat.ChatMessage(ChatRole.SYSTEM.getValue(), chatConfig.getSystemPrompt()));
        chatMsgs.stream()
                .map(chatRecord -> new com.theokanning.openai.completion.chat.ChatMessage(chatRecord.role().getValue(), chatRecord.getMsg()))
                .forEach(messages::add);
        return ChatCompletionRequest.builder()
                .temperature(chatConfig.getTemperature())
                .presencePenalty(chatConfig.getPresencePenalty())
                .n(chatConfig.n)
                .stream(isStream)
                .model(chatConfig.getUseModel())
                .messages(messages).build();
    }
}
