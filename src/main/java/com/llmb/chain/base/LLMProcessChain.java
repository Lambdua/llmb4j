package com.llmb.chain.base;

/**
 * chain链顶级接口
 *
 * @param <P> payload
 * @author LiangTao
 * @date 2023年06月02 16:34
 **/
public interface LLMProcessChain {
    void doProcess(LLMProcessPayload payload);

}
