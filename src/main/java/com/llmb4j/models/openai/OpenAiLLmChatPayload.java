package com.llmb4j.models.openai;

import com.llmb4j.models.base.BaseLLMChatPayload;
import com.llmb4j.models.openai.completion.chat.ChatFunction;

import java.util.List;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月15 10:36
 **/
public class OpenAiLLmChatPayload extends BaseLLMChatPayload {


    /**
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower
     * values like 0.2 will make it more focused and deterministic.<br>
     * We generally recommend altering this or top_p but not both.
     */
    public Double temperature;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens
     * with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.<br>
     * We generally recommend altering this or temperature but not both.
     */
    public  Double topP;

    /**
     * How many chat completion chatCompletionChoices to generate for each input message.
     */
    public  Integer n;

    /**
     * The maximum number of tokens allowed for the generated answer. By default, the number of tokens the model can return will
     * be (4096 - prompt tokens).
     */
    public  Integer maxTokens;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
     * increasing the model's likelihood to talk about new topics.
     */
    public  Double presencePenalty;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far,
     * decreasing the model's likelihood to repeat the same line verbatim.
     */
    public  Double frequencyPenalty;

    /**
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100
     * to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will
     * vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100
     * should result in a ban or exclusive selection of the relevant token.
     */
    public Map<String, Integer> logitBias;

    /**
     * A list of the available functions.
     */
    public    List<ChatFunction> functions;

    /**
     * Controls how the model responds to function calls, as specified in the <a href="https://platform.openai.com/docs/api-reference/chat/create#chat/create-function_call">OpenAI documentation</a>.
     */
    public  String functionCall;

}
