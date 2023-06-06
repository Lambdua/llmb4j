package com.llmb.prompt.str;

import com.llmb.prompt.base.LLMMessage;

/**
 * @author LiangTao
 * @date 2023年05月30 15:44
 **/
public record StrMessage(String msg) implements LLMMessage {
    @Override
    public String getMsg() {
        return msg;
    }
}
