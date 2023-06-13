package com.llmb4j.output.base;

import com.llmb4j.prompt.base.PromptValue;

/**
 * @author LiangTao
 * @date 2023年06月06 14:59
 **/
public interface BaseOutputParser<T> {

    /**
     * 分析 LLM 调用的输出。一种接受字符串（假定语言模型的输出）并将其解析为某种结构的方法。
     * @author liangtao
     * @date 2023/6/6
     * @param str 文本
     * @return T  结构化的对象
     **/
    T parse(String str);

    /**
     * 根据指定的prompt提示，解析LLM调用的输出。
     * @author liangtao
     * @date 2023/6/6
     * @param completion 文本
     * @param prompt 提示
     * @return O
     **/
    <O> O parseWithPrompt(String completion, PromptValue prompt);


    /**
     * 返回格式化的说明，用于在用户请求帮助时显示。例如，如果解析器是一个模型，则此方法可能返回模型的输入格式说明。
     * @author liangtao
     * @date 2023/6/6
     * @return java.lang.String
     **/
    String getFormatInstructions();


    /**
     * 返回此解析器的类型。例如，如果解析器是一个模型，则此方法可能返回“模型”。
     * @author liangtao
     * @date 2023/6/6
     * @return java.lang.String
     **/
    String type();

}
