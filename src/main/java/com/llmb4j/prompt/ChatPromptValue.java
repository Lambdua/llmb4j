package com.llmb4j.prompt;

import com.llmb4j.prompt.base.PromptValue;
import com.llmb4j.prompt.base.RoleMessage;
import com.llmb4j.util.ChatMsgUtil;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年06月16 10:42
 **/
public class ChatPromptValue implements PromptValue {
    private List<? extends RoleMessage> chatHistory;

    @Override
    public List<? extends RoleMessage> toMessages() {
        return chatHistory;
    }

    @Override
    public String toString() {
        return ChatMsgUtil.getBufferString(chatHistory);
    }
}
