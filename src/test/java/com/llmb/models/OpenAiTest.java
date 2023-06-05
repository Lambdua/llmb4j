package com.llmb.models;

import cn.hutool.core.lang.Assert;
import com.llmb.models.openai.OpenAiLLM;
import com.llmb.prompt.chat.ChatMessage;
import com.llmb.prompt.chat.ChatPromptTemplate;
import com.llmb.prompt.chat.ChatRole;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author LiangTao
 * @date 2023年05月30 10:46
 **/
@Slf4j
public class OpenAiTest {
    @Test
    public void streamTest()  {
        OpenAiLLM openAiLLM = new OpenAiLLM();
        ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        ChatMessage request = chatPromptTemplate.toMsg(template, new payload("小明", ChatRole.USER));
        Flowable<ChatMessage> chatMessageFlowable = openAiLLM.streamChat(Collections.singletonList(request));
        List<ChatMessage> chatMessages = chatMessageFlowable.toList().blockingGet();
        Assert.notEmpty(chatMessages);
    }

    @Test
    public void completionTest() {
        OpenAiLLM openAiLLM = new OpenAiLLM();
        ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) { }
        ChatMessage request = chatPromptTemplate.toMsg(template, new payload("小明", ChatRole.USER));
        List<ChatMessage> chatCompletion = openAiLLM.fullChat(Collections.singletonList(request));
        Assert.notEmpty(chatCompletion);
    }

}
