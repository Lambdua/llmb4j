package com.llmb.models.base;

import com.llmb.log.LLmLogStyle;
import com.llmb.prompt.base.LLMMessage;
import com.llmb.util.LLmConstants;
import io.reactivex.Flowable;
import org.slf4j.MDC;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月26 11:30
 **/
public interface LLMModel<C extends LLmConfig, MI extends LLMMessage<?>, MO extends LLMMessage<?>> {

    /**
     * 与llm交互的日志记录
     * @param logStyle 相关信息类型
     * @param msg 日志内容
     */
    default void log(LLmLogStyle logStyle,String msg) {
        try {
            MDC.put(LLmConstants.llmLogTypeKey, logStyle.type);
            LLmConstants.llmLogger.info(msg);
        } finally {
            MDC.remove(LLmConstants.llmLogTypeKey);
        }
    }


    /**
     * 获取llm的配置信息
     */
    C getConfig();

    /**
     * 与llm进行流式chat交互
     * @author liangtao
     * @date 2023/6/2
     * @param chatConfig 此次交互的配置信息
     * @param chatMsgs 交互的上下文
     * @return io.reactivex.Flowable<MO>
     **/
    default Flowable<MO> streamChat(C chatConfig, List<MI> chatMsgs) {
        if (chatConfig.isLogLLmMsg()) {
            log(LLmLogStyle.CONFIG,chatConfig.toString());
            chatMsgs.stream()
                    .map(LLMMessage::getMsgStr)
                    .reduce((a,b)-> a+System.lineSeparator()+b).ifPresent(msg->log(LLmLogStyle.INPUT,msg));
        }
        Flowable<MO> flowable = doStreamChat(chatConfig, chatMsgs);
        StringBuilder sb = new StringBuilder();
        if (chatConfig.isLogLLmMsg()) {
            return flowable
                    .doOnNext(chatMsg -> sb.append(chatMsg.getMsg()))
                    .doOnComplete(() ->log(LLmLogStyle.OUTPUT,sb.toString()+System.lineSeparator()));
        }
        return flowable;
    }

    /**
     * 与llm进行流式chat交互，具体实现由子类实现
     * @author liangtao
     * @date 2023/6/2
     * @param chatConfig 此次交互的配置信息
     * @param chatMsgs 交互的上下文
     * @return io.reactivex.Flowable<MO>
     **/
     Flowable<MO> doStreamChat(C chatConfig, List<MI> chatMsgs);


     /**
      * 与llm进行流式chat交互,使用默认配置信息
      * @author liangtao
      * @date 2023/6/2
      * @param chatMsgs 交互的上下文
      * @return io.reactivex.Flowable<MO>
      **/
    default Flowable<MO> streamChat(List<MI> chatMsgs) {
        return streamChat(getConfig(), chatMsgs);
    }

    /**
     * 与llm进行全量chat交互
     * @author liangtao
     * @date 2023/6/2
     * @param chatConfig 此次交互的配置信息
     * @param chatMsgs 交互的上下文
     * @return java.util.List<MO>  结果可能是多个
     **/
    default List<MO> fullChat(C chatConfig, List<MI> chatMsgs){
        if (chatConfig.isLogLLmMsg()) {
            log(LLmLogStyle.CONFIG,chatConfig.toString());
            chatMsgs.stream()
                    .map(LLMMessage::getMsgStr)
                    .reduce((a,b)-> a+System.lineSeparator()+b).ifPresent(msg->log(LLmLogStyle.INPUT,msg));
        }
        List<MO> mos = doFullChat(chatConfig, chatMsgs);
        if (chatConfig.isLogLLmMsg()) {
            for (MO mo : mos) {
                log(LLmLogStyle.OUTPUT,mo.getMsgStr());
            }
        }
        return mos;
    }

    /**
     * 与llm进行全量chat交互，具体实现由子类实现
     * @author liangtao
     * @date 2023/6/2
     * @param chatConfig 此次交互的配置信息
     * @param chatMsgs 交互的上下文
     * @return java.util.List<MO>
     **/
    List<MO> doFullChat(C chatConfig, List<MI> chatMsgs);


    /**
     * 与llm进行全量chat交互,使用默认配置信息
     * @author liangtao
     * @date 2023/6/2
     * @param chatMsgs 交互的上下文
     * @return java.util.List<MO>
     **/
    default List<MO> fullChat(List<MI> chatMsgs) {
        return fullChat(getConfig(), chatMsgs);
    }

}
