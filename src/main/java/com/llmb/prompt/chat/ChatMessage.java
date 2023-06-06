package com.llmb.prompt.chat;

import com.llmb.prompt.base.LLMMessage;

/**
 * @param msg 消息内容
 * @param role 消息角色
 * @author LiangTao
 * @date 2023年05月25 17:25
 **/
public record ChatMessage(String msg, ChatRole role) implements LLMMessage {
    public ChatMessage(String msg, ChatRole role) {
        this.msg = msg;
        this.role = role;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
