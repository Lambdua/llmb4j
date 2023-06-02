package com.llmb;

import ch.qos.logback.classic.PatternLayout;
import com.llmb.log.ChatStyle;
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
        Logger log = LoggerFactory.getLogger("aiChat");
        PatternLayout.defaultConverterMap.put("clr","com.llmb.log.ColorConverter");
        PatternLayout.defaultConverterMap.put("dynamic","com.llmb.log.DynamicTypeConverter");
        MDC.put("chatType", ChatStyle.INPUT.type);
        log.info("测试输入内容");
        MDC.put("chatType", ChatStyle.OUTPUT.type);
        log.info("测试输出内容");
    }
    public static void main(String[] args) {
        Logger log= LoggerFactory.getLogger(LogTest.class.getName());
        log.info("test");
        log.debug("test");
        log.error("test");
        log.warn("test");
        log.trace("test");


    }
}
