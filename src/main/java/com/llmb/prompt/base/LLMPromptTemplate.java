package com.llmb.prompt.base;

import com.llmb.memory.LLMMemory;

/**
 * prompt生成模板工具
 * @param <M> 生成的消息类型
 * @param <T> 模板类型
 *
 * @author LiangTao
 * @date 2023年05月25 16:37
 **/
public interface LLMPromptTemplate<M extends LLMMessage> {

    /**
     * 根据模板生成消息
     * @author liangtao
     * @date 2023/5/31
     * @param template 模板
     * @param argsPayload 模板参数,根据record获取
     * @return M  生成的消息
     **/
    M toMsg(String template, Record argsPayload);

    /**
     * 根据模板生成消息
     * @author liangtao
     * @date 2023/5/31
     * @param template 模板
     * @param argsPayload 模板参数,根据map获取
     * @return M
     **/
    M toMsg(String template, LLMMemory argsPayload);


    /**
     * 将msg消息解析成对应的类型
     * @author liangtao
     * @date 2023/5/31
     * @param msg 消息
     * @return O
     **/
    <O> O parse(M msg);

    default <O> O parse(M msg,LLMOutputParse<M,O> outputParse){
        return outputParse.parse(msg);
    }

}
