package com.llmb.chain.chat;

import com.llmb.chain.base.LLMProcess;
import com.llmb.chain.base.LLMProcessPayload;
import com.llmb.models.base.LLMModel;
import com.llmb.models.base.LLmConfig;
import com.llmb.prompt.base.LLMStrMessage;
import com.llmb.prompt.base.LLmStrPromptTemplate;

/**
 * @author LiangTao
 * @date 2023年06月05 15:22
 **/
public abstract class StrProcess<M extends LLMStrMessage,P extends LLMProcessPayload> implements LLMProcess<P> {
    protected LLMModel<? extends LLmConfig,M> llm;

    protected LLmStrPromptTemplate<M> promptTemplate;

    protected StrProcess(LLMModel<? extends LLmConfig,M> llm, LLmStrPromptTemplate<M> promptTemplate) {
        this.llm = llm;
        this.promptTemplate = promptTemplate;
    }

}
