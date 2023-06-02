package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月26 11:30
 **/
public interface LLMModel<C extends ChatConfig, MI extends LLMMessage<?>,MO extends LLMMessage<?>> {

    C getChatConfig();

    Flowable<MO> streamChat(C chatConfig, List<MI> chatMsgs);

    default Flowable<MO> streamChat(List<MI> chatMsgs){
        return streamChat(getChatConfig(),chatMsgs);
    }

    List<MO> fullChat(C chatConfig, List<MI> chatMsgs);

    default List<MO> fullChat(List<MI> chatMsgs){
        return fullChat(getChatConfig(),chatMsgs);
    }

}
