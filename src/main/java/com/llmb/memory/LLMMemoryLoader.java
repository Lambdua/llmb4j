package com.llmb.memory;

import com.llmb.prompt.base.LLMMessage;

import java.util.Collections;
import java.util.List;

/**
 * llm的记忆加载器
 * @author LiangTao
 * @date 2023年06月05 10:03
 **/
public interface LLMMemoryLoader {

    /**
     * 加载llm的内存
     * @return com.llmb.memory.LLMMemory
     **/
    default LLMMemory loadLLMMemory(){
        return new LLMMemory(this);
    }

    default  List<LLMMessage> getHistoryMsg(String sessionId){
        return Collections.emptyList();
    }

    default  void  addHistoryMsg(String sessionId,LLMMessage ...msgs){
        //do nothing
    }

    default  void removeHistoryMsg(String sessionId,LLMMessage ...msgs){
        //do nothing
    }





    default Object getLLMMemory(String key){
        return null;
    }

    default boolean containsKey(String key){
        return false;
    }


}
