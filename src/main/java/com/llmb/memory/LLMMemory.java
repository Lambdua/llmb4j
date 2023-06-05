package com.llmb.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * llm的记忆存储
 *
 * @author LiangTao
 * @date 2023年06月05 10:01
 **/
public class LLMMemory extends HashMap<String, Object> {
    private final LLMMemoryLoader memoryLoader;



    public LLMMemory(LLMMemoryLoader memoryLoader) {
        this.memoryLoader = memoryLoader;
    }

    public LLMMemory() {
        this.memoryLoader = new LLMMemoryLoader() {
        };
    }

    public LLMMemory(Map<String, Object> m) {
        super(m);
        this.memoryLoader = new LLMMemoryLoader() {
        };
    }

    @Override
    public Object get(Object key) {
        Object o = super.get(key);
        if (o == null) {
            Object value = memoryLoader.getLLMMemory(key.toString());
            if (value != null) {
                put(key.toString(), value);
                return value;
            }
        }
        return o;
    }


    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key) || memoryLoader.containsKey(key.toString());
    }
}
