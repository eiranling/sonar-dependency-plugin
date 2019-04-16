package org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors;

import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

public class ComponentTreeMeasuresRequestBuilder implements Requestor {

    private Map<String, String> params;
    private HttpUrl.Builder builder;

    public ComponentTreeMeasuresRequestBuilder(HttpUrl.Builder builder) {
        params = new HashMap<>();
        this.builder = builder.addPathSegments("api/measures/component_tree");
    }

    @Override
    public void addParam(String key, String value) {
        params.put(key, value);
        builder.addQueryParameter(key, value);
    }

    @Override
    public void setParams(Map<String, String> params) {
        params.keySet().forEach(builder::removeAllQueryParameters);
        this.params = params;
        params.keySet().forEach(key -> builder.addQueryParameter(key, params.get(key)));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Request build(Request.Builder reqBuilder) {
        return reqBuilder.url(builder.build()).build();
    }
}
