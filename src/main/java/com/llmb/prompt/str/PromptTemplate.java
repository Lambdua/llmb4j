package com.llmb.prompt.str;

import com.llmb.memory.LLMMemory;
import com.llmb.prompt.base.AbstractPromptTemplate;
import com.llmb.prompt.base.LLMInputParse;
import com.llmb.prompt.base.LLMOutputParse;
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
public class PromptTemplate extends AbstractPromptTemplate<StrMessage> {
    @Override
    public LLMInputParse<StrMessage> createDefaultInputParse() {
        return new StrInputParse();
    }

    @Override
    public LLMOutputParse<StrMessage,String> createDefaultOutputParse() {
        return StrMessage::getMsg;
    }


    public static class StrInputParse implements LLMInputParse<StrMessage> {
        @Override
        public StrMessage toMsg(String template, Record argsPayload) {
            Class<? extends Record> aClass = argsPayload.getClass();
            for (RecordComponent recordComponent : aClass.getRecordComponents()) {
                Method accessor = recordComponent.getAccessor();
                accessor.setAccessible(true);
                String paramName = recordComponent.getName();
                try {
                    if (template.contains("{$" + paramName + "}")) {
                        template = template.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("获取参数值失败", e);
                }

            }
            return new StrMessage(template);
        }

        @Override
        public StrMessage toMsg(String template, LLMMemory argsPayload) {
            for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (template.contains("{$" + paramName + "}")) {
                    template = template.replace("{$" + paramName + "}", paramValue.toString());
                }
            }
            return new StrMessage(template);
        }
    }
}
