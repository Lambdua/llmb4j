package com.llmb.chain.base;

/**
 * @author LiangTao
 * @date 2023年06月05 15:15
 **/
public interface LLMProcess<P extends LLMProcessPayload> {
    void doProcess(P payload, LLMProcessChain<P> chain);

}