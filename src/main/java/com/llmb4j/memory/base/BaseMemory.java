package com.llmb4j.memory.base;

import java.util.List;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月06 14:14
 **/
public interface BaseMemory {

    /**
     * 此内存类将动态加载的输入键集合
     * @author liangtao
     * @date 2023/6/6
     * @return java.util.List<java.lang.String>
     **/
   List<String> memoryVariables();

    /**
     * 返回给定链文本输入的键值对。如果无，则返回所有内存
     * @author liangtao
     * @date 2023/6/6
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> loadMemoryVariables(Map<String, Object> inputs);

    /**
     * 将此模型运行的上下文保存到内存中。
     * @author liangtao
     * @date 2023/6/6
     * @param inputs 输入内容,其中包含用户内容key-v
     * @param outputs 输出内容,其中包含模型输出key-v
     **/
   void saveChatHistory(Map<String, Object> inputs, Map<String, String> outputs);

   void clear();
}
