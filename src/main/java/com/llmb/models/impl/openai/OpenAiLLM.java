package com.llmb.models.impl.openai;

import com.llmb.models.base.LLMModel;
import com.llmb.models.base.PoolProperties;
import com.llmb.models.impl.openai.api.OpenAiService;
import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;

import java.time.Duration;
import java.util.List;

import static com.llmb.util.SettingUtil.PROMPT;

/**
 * @author LiangTao
 * @date 2023年05月29 11:16
 **/
public class OpenAiLLM implements LLMModel<OpenAiChatConfig> {
    private OpenAiService openAiService;

    private OpenAiChatConfig defaultChatConfig;


    public OpenAiLLM() {
        this.defaultChatConfig = withDefault();
        this.openAiService = new OpenAiService(OpenAiChatConfig.apiKey, Duration.ofSeconds(30), OpenAiChatConfig.baseUrl, new PoolProperties());
    }

    private OpenAiChatConfig withDefault() {
        OpenAiChatConfig openAiChatConfig = new OpenAiChatConfig();
        openAiChatConfig.setSystemPrompt(PROMPT.get("prompt", "defaultSystemPrompt"));
        openAiChatConfig.setTemperature(0);
        openAiChatConfig.setPresencePenalty(0);
        openAiChatConfig.setUseModel("gpt-3.5-turbo");
        return openAiChatConfig;
    }


    @Override
    public Flowable<LLMMessage> streamChatCompletion(OpenAiChatConfig chatConfig, List<LLMMessage> chatMsgs) {
        return null;
    }

    @Override
    public Flowable<LLMMessage> streamChatCompletion(List<LLMMessage> chatMsgs) {
        return streamChatCompletion(this.defaultChatConfig, chatMsgs);
    }

    @Override
    public LLMMessage createChatCompletion(OpenAiChatConfig chatConfig, List<LLMMessage> chatMsgs) {
        return null;
    }

    @Override
    public LLMMessage createChatCompletion(List<LLMMessage> chatMsgs) {
        return createChatCompletion(this.defaultChatConfig, chatMsgs);
    }
}
