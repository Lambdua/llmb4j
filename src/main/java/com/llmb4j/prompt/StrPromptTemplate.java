package com.llmb4j.prompt;


import com.llmb4j.prompt.base.LLMInputParse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月16 13:54
 **/
@Slf4j
public class StrPromptTemplate  implements LLMInputParse<String> {

    @Override
    public String format(String template, Record argsPayload) {
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
        return template;
    }

    @Override
    public String format(String template, Map<String, Object> argsPayload) {
        for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            if (template.contains("{$" + paramName + "}")) {
                template = template.replace("{$" + paramName + "}", paramValue.toString());
            }
        }
        return template;
    }
}
