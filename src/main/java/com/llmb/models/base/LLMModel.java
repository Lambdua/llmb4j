package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月26 11:30
 **/
public interface LLMModel<C extends ChatConfig> {
    Flowable<LLMMessage> streamChatCompletion(C chatConfig, List<LLMMessage> chatMsgs);

    Flowable<LLMMessage> streamChatCompletion(List<LLMMessage> chatMsgs);

    LLMMessage createChatCompletion(C chatConfig, List<LLMMessage> chatMsgs);

    LLMMessage createChatCompletion(List<LLMMessage> chatMsgs);

}
