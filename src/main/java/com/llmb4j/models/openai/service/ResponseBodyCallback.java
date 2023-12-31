package com.llmb4j.models.openai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llmb4j.models.openai.base.OpenAiError;
import com.llmb4j.models.openai.base.OpenAiHttpException;
import io.reactivex.FlowableEmitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Callback to parse Server Sent Events (SSE) from raw InputStream and
 * emit the events with io.reactivex.FlowableEmitter to allow streaming of
 * SSE.
 */
public class ResponseBodyCallback implements Callback<ResponseBody> {
    private static final ObjectMapper mapper = OpenAiService.defaultObjectMapper();

    private final FlowableEmitter<SSE> emitter;
    private final boolean emitDone;

    public ResponseBodyCallback(FlowableEmitter<SSE> emitter, boolean emitDone) {
        this.emitter = emitter;
        this.emitDone = emitDone;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        try {
            if (!response.isSuccessful()) {
                HttpException e = new HttpException(response);
                ResponseBody errorBody = response.errorBody();

                if (errorBody == null) {
                    throw e;
                } else {
                    OpenAiError error = mapper.readValue(
                            errorBody.string(),
                            OpenAiError.class
                    );
                    throw new OpenAiHttpException(error, e, e.code());
                }
            }

            InputStream in = response.body().byteStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                SSE sse = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data:")) {
                        String data = line.substring(5).trim();
                        sse = new SSE(data);
                    } else if (line.equals("") && sse != null) {
                        if (sse.isDone()) {
                            if (emitDone) {
                                emitter.onNext(sse);
                            }
                            break;
                        }

                        emitter.onNext(sse);
                        sse = null;
                    } else {
                        throw new SSEFormatException("Invalid sse format! " + line);
                    }
                }
            }
            emitter.onComplete();

        } catch (Throwable t) {
            onFailure(call, t);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        emitter.onError(t);
    }
}
