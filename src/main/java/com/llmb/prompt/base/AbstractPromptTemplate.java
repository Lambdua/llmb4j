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
public abstract class AbstractPromptTemplate<M extends LLMMessage> implements LLMPromptTemplate<M>{

    protected LLMOutputParse<M,?> outputParse;

    protected LLMInputParse<M> inputParse;

    public abstract LLMInputParse<M>  createDefaultInputParse();

    public abstract  LLMOutputParse<M,String>  createDefaultOutputParse();

    protected AbstractPromptTemplate() {
        this.inputParse=createDefaultInputParse();
        this.outputParse = createDefaultOutputParse();
    }

    protected AbstractPromptTemplate(LLMOutputParse<M,String>  outputParse) {
        this.outputParse =  outputParse;
        this.inputParse=createDefaultInputParse();
    }

    protected AbstractPromptTemplate(LLMInputParse<M>  inputParse) {
        this.inputParse = inputParse;
        this.outputParse = createDefaultOutputParse();
    }

    protected AbstractPromptTemplate(LLMOutputParse<M,String>  outputParse, LLMInputParse<M>  inputParse) {
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
