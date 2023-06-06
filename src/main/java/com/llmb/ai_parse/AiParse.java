package com.llmb.ai_parse;

import com.llmb.prompt.base.LLMMessage;
import com.llmb.prompt.base.LLMOutputParse;

/**
 * @author LiangTao
 * @date 2023年05月30 14:46
 **/
public interface AiParse<I extends LLMMessage, T> extends LLMOutputParse<I,T> {

    /**
     * 将指定的LLMMessage对象解析为指定的类型
     * @author liangtao
     * @date 2023/5/30
     * @param msg 指定的LLMMessage对象
     * @return T  解析后的对象
     **/


    /**
     * 将指定的字符串解析为指定的类型
     * @param msg 指定的字符串
     */
    T parseStr(String msg);


    /**
     * 告诉llm如何对输入内容进行格式化解析
     * @return 解析指令(prompt)
     */
    String getParseInstructions();
}
