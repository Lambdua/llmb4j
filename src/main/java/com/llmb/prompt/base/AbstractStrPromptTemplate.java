package com.llmb.prompt.base;

import com.llmb.memory.LLMMemory;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LiangTao
 * @date 2023年05月31 11:39
 **/
@Getter
@Setter
public abstract class AbstractStrPromptTemplate<M extends LLMStrMessage> implements LLmStrPromptTemplate<M>{

    protected LLMOutputParse<M,?> outputParse;

    protected LLMStrInputParse<M> inputParse;

    public abstract LLMStrInputParse<M>  createDefaultInputParse();

    public abstract  LLMOutputParse<M,?>  createDefaultOutputParse();

    protected AbstractStrPromptTemplate() {
        this.inputParse=createDefaultInputParse();
        this.outputParse = createDefaultOutputParse();
    }

    protected AbstractStrPromptTemplate(LLmStrOutputParse<M>  outputParse) {
        this.outputParse =  outputParse;
        this.inputParse=createDefaultInputParse();
    }

    protected AbstractStrPromptTemplate(LLMStrInputParse<M>  inputParse) {
        this.inputParse = inputParse;
        this.outputParse = createDefaultOutputParse();
    }

    protected AbstractStrPromptTemplate(LLmStrOutputParse<M>  outputParse, LLMStrInputParse<M>  inputParse) {
        this.outputParse = outputParse;
        this.inputParse = inputParse;
    }

    @Override
    public M toMsg(String target, LLMMemory argsPayload) {
       return inputParse.toMsg(target,argsPayload);
    }

    @Override
    public M toMsg(String target, Record argsPayload) {
        return inputParse.toMsg(target,argsPayload);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <O> O parse(M msg) {
        return (O) outputParse.parse(msg);
    }
}
