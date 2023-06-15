package com.llmb4j.models.base;

import com.llmb4j.callbacks.BaseCallbackHandler;
import lombok.Setter;

import java.util.List;

/**
 * llm请求的荷载参数基类
 * @author LiangTao
 * @date 2023年06月15 09:48
 **/
@Setter
public class BaseLLMPayload {

    /**
     * 模型名称
     */
    public String modelName;


    /**
     * 日志打印
     */
    public  boolean verbose;

    /**
     * 回调处理器
     */
    public  BaseCallbackHandler callbackHandler;


    /**
     * 是否流式请求
     */
    public boolean stream;

    /**
     * 响应停止词
     */
    public List<String> stop;


    /**
     * 代表您的最终用户的唯一标识符
     */
    public String userIdentity;

}
