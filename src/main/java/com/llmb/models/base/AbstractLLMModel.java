package com.llmb.models.base;

import com.llmb.prompt.base.LLMMessage;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author LiangTao
 * @date 2023年06月01 11:46
 **/
@Slf4j
public abstract class AbstractLLMModel<C extends ChatConfig, MI extends LLMMessage<?>, MO extends LLMMessage<?>> implements LLMModel<C, MI, MO> {

    @Override
    public Flowable<MO> streamChat(C chatConfig, List<MI> chatMsgs) {
        if (chatConfig.isLogLLmMsg()) {
            log.info(String.format("%schatConfig---->%s", System.lineSeparator(), chatConfig));
            chatMsgs.forEach(chatMsg -> log.info(String.format("%schatRequest--->%s", System.lineSeparator(), chatMsg)));
        }
        Flowable<MO> flowable = doStreamChat(chatConfig, chatMsgs);
        StringBuilder sb = new StringBuilder();
        if (chatConfig.isLogLLmMsg()) {
            return flowable
                    .doOnNext(chatMsg -> sb.append(chatMsg.getMsg()))
                    .doOnComplete(() -> log.info(String.format("%schatResponse--->%s", System.lineSeparator(), sb)));
        }
        return flowable;
    }

    public abstract Flowable<MO> doStreamChat(C chatConfig, List<MI> chatMsgs);

    @Override
    public List<MO> fullChat(C chatConfig, List<MI> chatMsgs) {
        if (chatConfig.isLogLLmMsg()) {
            // log.info("{}chatConfig:{}",System.lineSeparator(),chatConfig);
            // chatMsgs.forEach(chatMsg -> log.info("{}chatRequest:{}",System.lineSeparator(),chatMsg));
        }
        List<MO> mos = doFullChat(chatConfig, chatMsgs);
        if (chatConfig.isLogLLmMsg()) {
            // mos.forEach(chatMsg -> log.info("{}chatResponse:{}",System.lineSeparator(),chatMsg));
        }
        return mos;
    }

    public abstract List<MO> doFullChat(C chatConfig, List<MI> chatMsgs);
}
