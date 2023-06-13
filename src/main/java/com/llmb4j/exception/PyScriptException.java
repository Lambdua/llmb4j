package com.llmb4j.exception;

/**
 * @author LiangTao
 * @date 2023年06月07 11:22
 **/
public class PyScriptException extends RuntimeException {

    public PyScriptException(String message) {
        super(message);
    }

    public PyScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
