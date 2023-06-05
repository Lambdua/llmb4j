package com.llmb.log;

import com.llmb.util.LLmConstants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * @author LiangTao
 * @date 2023年06月01 15:18
 **/
public class LogTest {

    @Test
    public void chatLogTest(){
        Logger log = LLmConstants.llmLogger;
        MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.INPUT.type);

        log.info("测试输入内容");
        MDC.put(LLmConstants.llmLogTypeKey, LLmLogStyle.OUTPUT.type);
        log.info("测试输出内容");
    }

    @Test
    public void defaultRandomTest(){
        Logger log = LLmConstants.llmLogger;
        for (int i = 0; i < 100; i++) {
            log.info("测试输入内容");
            log.info("测试输出内容");
        }
    }


    @Test
    public void commLogTest(){
        Logger log= LoggerFactory.getLogger(LogTest.class.getName());
        log.info("test");
        log.debug("test");
        log.error("test");
        log.warn("test");
        log.trace("test");
    }
}
