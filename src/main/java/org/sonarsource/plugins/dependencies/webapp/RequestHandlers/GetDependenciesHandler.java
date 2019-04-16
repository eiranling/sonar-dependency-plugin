package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.sonar.api.internal.google.gson.stream.JsonReader;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.ComponentTreeMeasuresRequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GetDependenciesHandler implements RequestHandler {

    private OkHttpClient client;

    public GetDependenciesHandler(OkHttpClient client) {
        this.client = client;
    }

    public void define(WebService.NewController controller) {
        WebService.NewAction action = controller.createAction("get")
                .setDescription("Gets the dependencies of a file in a project")
                .setHandler(this);

        action.createParam("componentKey")
                .setRequired(true)
                .setExampleValue("projectKey:component")
                .setDescription("The key of the to find dependencies of");
    }

    @Override
    public void handle(Request request, Response response) throws IOException {
        request.getParam("componentKey");

        HttpUrl baseUrl = HttpUrl.parse(request.getPath());

        if (baseUrl == null) throw new MalformedURLException();

        okhttp3.Request.Builder metricReqBuilder = new okhttp3.Request.Builder().get();

        okhttp3.Request metricRequest = new ComponentTreeMeasuresRequestBuilder(new okhttp3.HttpUrl.Builder()
                .scheme(baseUrl.scheme()).host(baseUrl.host()).port(baseUrl.port())
                ).build(metricReqBuilder);

        okhttp3.Response metricResponse = client.newCall(metricRequest).execute();
        ResponseBody body = metricResponse.body();
        if (body == null) {
            response.newJsonWriter().beginObject().prop("error", "Whoops, something went wrong fetching metrics.").endObject().close();
            return;
        }

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .endObject()
                .close();
    }
}
