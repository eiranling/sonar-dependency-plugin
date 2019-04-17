package org.sonarsource.plugins.dependencies.webapp.util;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;


public class ClientFactory {
    public static OkHttpClient generate() {
        return new OkHttpClient.Builder().build();
    }
}
