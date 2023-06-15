package com.llmb4j.models.openai.api;

import com.llmb4j.models.openai.base.OpenAiResponse;
import com.llmb4j.models.openai.completion.CompletionRequest;
import com.llmb4j.models.openai.completion.CompletionResult;
import com.llmb4j.models.openai.completion.chat.ChatCompletionRequest;
import com.llmb4j.models.openai.completion.chat.ChatCompletionResult;
import com.llmb4j.models.openai.model.Model;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface OpenAiApi {

    @GET("v1/models")
    Single<OpenAiResponse<Model>> listModels();

    @GET("/v1/models/{model_id}")
    Single<Model> getModel(@Path("model_id") String modelId);

    @POST("/v1/completions")
    Single<CompletionResult> createCompletion(@Body CompletionRequest request);

    @Streaming
    @POST("/v1/completions")
    Call<ResponseBody> createCompletionStream(@Body CompletionRequest request);

    @POST("/v1/chat/completions")
    Single<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

    @Streaming
    @POST("/v1/chat/completions")
	Call<ResponseBody> createChatCompletionStream(@Body ChatCompletionRequest request);


}
