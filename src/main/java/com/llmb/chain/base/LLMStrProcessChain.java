package com.llmb.chain.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 字符串chain的通用实现
 * @author LiangTao
 * @date 2023年06月05 10:18
 **/
@Getter
@Setter
public class LLMStrProcessChain implements LLMProcessChain {

    private LLMProcess[] processes;

    private int index = 0;

    public LLMStrProcessChain(LLMProcess[] processes) {
        this.processes = processes;
    }

    public void addProcess(LLMProcess process){
        LLMProcess[] newProcesses = new LLMProcess[processes.length + 1];
        System.arraycopy(processes, 0, newProcesses, 0, processes.length);
        newProcesses[processes.length] = process;
        processes = newProcesses;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void doProcess(LLMProcessPayload payload) {
        if (index < processes.length) {
            LLMProcess processor = processes[index++];
            processor.doProcess(payload, this);
        }
    }
}
