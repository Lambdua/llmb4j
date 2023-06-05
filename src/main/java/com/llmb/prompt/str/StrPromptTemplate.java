package com.llmb.prompt.str;

import com.llmb.memory.LLMMemory;
import com.llmb.prompt.base.AbstractStrPromptTemplate;
import com.llmb.prompt.base.LLMStrInputParse;
import com.llmb.prompt.base.LLmStrOutputParse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月30 15:45
 **/
@Slf4j
public class StrPromptTemplate extends AbstractStrPromptTemplate<StrMessage> {
    @Override
    public LLMStrInputParse<StrMessage> createDefaultInputParse() {
        return new StrInputParse();
    }

    @Override
    public LLmStrOutputParse<StrMessage> createDefaultOutputParse() {
        return StrMessage::getMsg;
    }


    public static class StrInputParse implements LLMStrInputParse<StrMessage> {
        @Override
        public StrMessage toMsg(String target, Record argsPayload) {
            Class<? extends Record> aClass = argsPayload.getClass();
            for (RecordComponent recordComponent : aClass.getRecordComponents()) {
                Method accessor = recordComponent.getAccessor();
                accessor.setAccessible(true);
                String paramName = recordComponent.getName();
                try {
                    if (target.contains("{$" + paramName + "}")) {
                        target = target.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("获取参数值失败", e);
                }

            }
            return new StrMessage(target);
        }

        @Override
        public StrMessage toMsg(String target, LLMMemory argsPayload) {
            for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (target.contains("{$" + paramName + "}")) {
                    target = target.replace("{$" + paramName + "}", paramValue.toString());
                }
            }
            return new StrMessage(target);
        }
    }
}
