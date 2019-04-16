package org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors;

import okhttp3.Request;

import java.util.Map;

public interface Requestor {
    void addParam(String key, String value);
    void setParams(Map<String, String> params);
    Map<String, String> getParams();

    Request build(Request.Builder reqBuilder);
}
