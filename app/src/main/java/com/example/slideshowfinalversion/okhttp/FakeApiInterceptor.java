package com.example.slideshowfinalversion.okhttp;

import android.content.Context;

import com.example.slideshowfinalversion.utils.Assets;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FakeApiInterceptor implements Interceptor {

    private Context context;

    public FakeApiInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return new Response.Builder()
                .code(200)
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), Assets.readString(context, "server.json")))
                .build();
    }
}
