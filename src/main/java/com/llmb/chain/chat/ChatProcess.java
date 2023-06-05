package com.llmb.chain.chat;

import com.llmb.chain.base.LLMProcessChain;
import com.llmb.chain.base.LLMProcessPayload;
import com.llmb.models.base.LLMModel;
import com.llmb.models.base.LLmConfig;
import com.llmb.prompt.chat.ChatMessage;
import com.llmb.prompt.chat.ChatPromptTemplate;

import java.util.Optional;

/**
 * @author LiangTao
 * @date 2023年06月05 15:36
 **/
public class ChatProcess extends StrProcess<ChatMessage, LLMProcessPayload> {
    protected ChatProcess(LLMModel<? extends LLmConfig, ChatMessage> llm) {
        super(llm, new ChatPromptTemplate());
    }


    @Override
    public void doProcess(LLMProcessPayload payload, LLMProcessChain<LLMProcessPayload> chain) {
        ChatMessage msg = null;
        if (payload.getMemory() != null) {
            msg = promptTemplate.toMsg(payload.getTemplateTarget(), payload.getMemory());
        }else {
            msg= promptTemplate.toMsg(payload.getTemplateTarget(), payload.getParamsRecord());
        }
        boolean isStream= Optional.ofNullable(payload.getChatConfig()).orElse(llm.getConfig()).isStream();
        if (isStream){
            // llm.streamChat()
        }



    }
}
