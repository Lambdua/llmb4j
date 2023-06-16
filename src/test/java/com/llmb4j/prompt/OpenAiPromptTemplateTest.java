package com.llmb4j.prompt;

import cn.hutool.core.lang.Assert;
import com.llmb4j.models.openai.OpenAiPromptTemplate;
import com.llmb4j.models.openai.OpenAiRoleMessage;
import com.llmb4j.prompt.base.ChatRole;
import org.junit.Test;

import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月16 14:29
 **/
public class OpenAiPromptTemplateTest {


    @Test
    public void recordTemplateTest() {
        final String template = """
                这是一段测试文本，参数以{$param}的形式插入,然后通过template来进行参数注入。
                {$name}-{$age}-{$sex}-{$测试}
                """;
        final OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        record paramPayload(String param, String name, Integer age, Boolean 测试, ChatRole chatRole) {
        }

        paramPayload paramPayload = new paramPayload("record声明方式", "姓名", 18, true, ChatRole.AI);

        OpenAiRoleMessage msg = chatPromptTemplate.format(template, paramPayload);

        Assert.equals(ChatRole.AI, msg.getRole());

        String result = """
            这是一段测试文本，参数以record声明方式的形式插入,然后通过template来进行参数注入。
            姓名-18-{$sex}-true
            """;

        Assert.equals(result,msg.getContent() );


        final String tem2= """
                {"temperature": "22", "unit": "celsius", "description": "Sunny"}
                """;
        record functionPayload(ChatRole chatRole,String functionName) {
        }
        OpenAiRoleMessage weather = chatPromptTemplate.format(tem2, new functionPayload(ChatRole.FUNCTION, "weather"));
        Assert.equals(ChatRole.FUNCTION, weather.getRole());
        Assert.equals("weather", weather.getName());
        Assert.equals(tem2, weather.getContent());
    }

    @Test
    public void mapTemplateTest() {
        final String template = """
                这是一段测试文本，参数以{$param}的形式插入,然后通过template来进行参数注入。
                {$name}-{$age}-{$sex}-{$测试}
                """;
        final OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        OpenAiRoleMessage msg = chatPromptTemplate.format(template, Map.of("param", "map声明方式", "name", "姓名", "age", 18, "测试", true, "chatRole", ChatRole.AI));
        Assert.equals(ChatRole.AI, msg.getRole());
        String result = """
            这是一段测试文本，参数以map声明方式的形式插入,然后通过template来进行参数注入。
            姓名-18-{$sex}-true
            """;
        Assert.equals(result, msg.getContent());

        final String tem2= """
                {"temperature": "22", "unit": "celsius", "description": "Sunny"}
                """;
        OpenAiRoleMessage weather = chatPromptTemplate.format(tem2, Map.of("chatRole", ChatRole.FUNCTION, "functionName", "weather"));
        Assert.equals(ChatRole.FUNCTION, weather.getRole());
        Assert.equals("weather", weather.getName());
        Assert.equals(tem2, weather.getContent());
    }

}
