package com.llmb.prompt.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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

    public AbstractStrPromptTemplate() {
        this.inputParse=createDefaultInputParse();
        this.outputParse = createDefaultOutputParse();
    }

    public AbstractStrPromptTemplate(LLmStrOutputParse<M>  outputParse) {
        this.outputParse =  outputParse;
        this.inputParse=createDefaultInputParse();
    }

    public AbstractStrPromptTemplate(LLMStrInputParse<M>  inputParse) {
        this.inputParse = inputParse;
        this.outputParse = createDefaultOutputParse();
    }

    public AbstractStrPromptTemplate(LLmStrOutputParse<M>  outputParse, LLMStrInputParse<M>  inputParse) {
        this.outputParse = outputParse;
        this.inputParse = inputParse;
    }

    @Override
    public M toMsg(String target, Map<String, Object> argsPayload) {
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
