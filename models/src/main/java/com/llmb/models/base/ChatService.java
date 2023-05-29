package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月26 11:30
 **/
public interface ChatService {


    Flowable<ChatStreamChunk> streamChatCompletion(ChatConfig chatConfig, List<LLMMessage> chatMsgs);

    Flowable<ChatStreamChunk> streamChatCompletion(List<LLMMessage> chatMsgs);

    ChatFullResult createChatCompletion(ChatConfig chatConfig, List<LLMMessage> chatMsgs);

    ChatFullResult createChatCompletion(List<LLMMessage> chatMsgs);

}
