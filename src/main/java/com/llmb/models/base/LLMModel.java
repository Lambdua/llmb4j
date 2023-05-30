package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月26 11:30
 **/
public interface LLMModel<C extends ChatConfig, MI extends LLMMessage,MO extends LLMMessage> {
    Flowable<MO> streamChatCompletion(C chatConfig, List<MI> chatMsgs);

    Flowable<MO> streamChatCompletion(List<MI> chatMsgs);

    List<MO> createChatCompletion(C chatConfig, List<MI> chatMsgs);

    List<MO> createChatCompletion(List<MI> chatMsgs);

}
