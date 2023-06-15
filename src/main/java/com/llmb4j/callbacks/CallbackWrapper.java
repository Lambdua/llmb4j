package com.llmb4j.callbacks;

import com.llmb4j.common.AgentAction;
import com.llmb4j.common.AgentFinish;
import com.llmb4j.common.LLMResult;
import com.llmb4j.prompt.base.RoleMessage;

import java.util.*;

/**
 * @author LiangTao
 * @date 2023年06月13 13:52
 **/
public class CallbackWrapper extends BaseCallbackHandler{
    private List<BaseCallbackHandler> callbacks;

    public CallbackWrapper(List<BaseCallbackHandler> callbacks) {
        this.callbacks = Optional.ofNullable(callbacks).orElse(new ArrayList<>());
    }

    public void addCallback(BaseCallbackHandler callback){
        callbacks.add(callback);
    }

    @Override
    public void onLlmStart(Map<String, Object> serialized, List<String> prompts, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onLlmStart(serialized, prompts, runId, parentRunId, kwargs));
    }

    @Override
    public void onChatModelStart(Map<String, Object> serialized, List<List<RoleMessage>> messages, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onChatModelStart(serialized, messages, runId, parentRunId, kwargs));
    }

    @Override
    public void onChainStart(Map<String, Object> serialized, Map<String, Object> inputs, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onChainStart(serialized, inputs, runId, parentRunId, kwargs));
    }

    @Override
    public void onToolStart(Map<String, Object> serialized, String inputStr, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onToolStart(serialized, inputStr, runId, parentRunId, kwargs));
    }

    @Override
    public void onChainEnd(Map<String, Object> outputs, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onChainEnd(outputs, runId, parentRunId, kwargs));
    }

    @Override
    public void onChainError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onChainError(error, runId, parentRunId, kwargs));
    }

    @Override
    public void onAgentAction(AgentAction action, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onAgentAction(action, runId, parentRunId, kwargs));
    }

    @Override
    public void onAgentFinish(AgentFinish finish, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onAgentFinish(finish, runId, parentRunId, kwargs));
    }

    @Override
    public void onLlmNewToken(String token, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onLlmNewToken(token, runId, parentRunId, kwargs));
    }

    @Override
    public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onLlmEnd(response, runId, parentRunId, kwargs));
    }

    @Override
    public void onLlmError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onLlmError(error, runId, parentRunId, kwargs));
    }

    @Override
    public void on_text(String text, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.on_text(text, runId, parentRunId, kwargs));
    }

    @Override
    public void onToolEnd(String output, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onToolEnd(output, runId, parentRunId, kwargs));
    }

    @Override
    public void onToolError(Exception error, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        callbacks.forEach(callback -> callback.onToolError(error, runId, parentRunId, kwargs));
    }
}
