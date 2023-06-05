package com.llmb.chain.base;

import com.llmb.chain.chat.StrProcess;
import com.llmb.prompt.base.LLMStrMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 字符串chain的通用实现
 * @author LiangTao
 * @date 2023年06月05 10:18
 **/
@Getter
@Setter
public class LLMStrProcessChain<P extends LLMProcessPayload> implements LLMProcessChain<P> {

    private StrProcess<? extends LLMStrMessage,LLMProcessPayload>[] processes;

    private int index = 0;

    public LLMStrProcessChain(StrProcess<? extends LLMStrMessage,LLMProcessPayload>[] processes) {
        this.processes = processes;
    }

    public void addProcess(StrProcess<? extends LLMStrMessage,LLMProcessPayload> process){
        StrProcess<? extends LLMStrMessage,LLMProcessPayload>[] newProcesses = new StrProcess[processes.length + 1];
        System.arraycopy(processes, 0, newProcesses, 0, processes.length);
        newProcesses[processes.length] = process;
        processes = newProcesses;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void doProcess(P payload) {
        if (index < processes.length) {
            StrProcess<? extends LLMStrMessage,LLMProcessPayload> processor = processes[index++];
            processor.doProcess(payload, (LLMProcessChain<LLMProcessPayload>) this);
        }
    }
}
