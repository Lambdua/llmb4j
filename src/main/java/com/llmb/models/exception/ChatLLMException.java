package com.llmb.models.exception;

/**
 * 和llm模型通讯异常
 *
 * @author LiangTao
 * @date 2023年05月29 11:14
 **/
public class ChatLLMException extends RuntimeException{

    public ChatLLMException() {
    }

    public ChatLLMException(String message) {
        super(message);
    }

    public ChatLLMException(String message, Throwable cause) {
        super(message, cause);
    }
}
