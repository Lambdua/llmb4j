package com.llmb.models.base;

/**
 * @author LiangTao
 * @date 2023年05月26 11:29
 **/
public interface LLmConfig {

    /**
     * 是否打印llm的通讯交互信息
     */
    default boolean isLogLLmMsg(){
        return true;
    }
}
