package org.sonarsource.plugins.dependencies.webapp.util;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.sonar.api.server.ServerSide;

import java.io.IOException;

@ServerSide
public class ClientFactory {
    public OkHttpClient generate() {
        return new OkHttpClient.Builder().build();
    }
}