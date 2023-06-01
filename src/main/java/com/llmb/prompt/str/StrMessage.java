package com.llmb.prompt.str;

import com.llmb.prompt.base.LLMStrMessage;

/**
 * @author LiangTao
 * @date 2023年05月30 15:44
 **/
public record StrMessage(String msg) implements LLMStrMessage {
    @Override
    public String getMsg() {
        return msg;
    }
}
