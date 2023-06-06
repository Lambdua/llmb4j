package com.llmb.chain.base;

/**
 * @author LiangTao
 * @date 2023年06月05 15:15
 **/
public interface LLMProcess {
    void doProcess(LLMProcessPayload payload, LLMProcessChain chain);

}
