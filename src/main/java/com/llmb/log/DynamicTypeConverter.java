package com.llmb.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.slf4j.MDC;

/**
 * @author LiangTao
 * @date 2023年06月02 13:55
 **/
public class DynamicTypeConverter extends CompositeConverter<ILoggingEvent> {
    @Override
    protected String transform(ILoggingEvent event, String in) {
        String chatType = MDC.get("chatType");
        return ChatStyle.fromType(chatType).type;
    }
}
