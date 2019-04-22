package org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.LocalConnector;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.measures.ComponentTreeRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@ServerSide
public class MeasuresComponentTreeRequestor extends LocalApiCaller {

    public Measures.ComponentTreeWsResponse componentTreeRequest(LocalConnector connector, Map<String, String> params) {
        WsClient client = generateClient(connector);

        ComponentTreeRequest request = new ComponentTreeRequest();
        request.setComponent(params.get("baseComponentKey"));
        request.setMetricKeys(Collections.singletonList(params.get("metricKeys")));
        request.setP(params.getOrDefault("p", ""));
        request.setPs(params.getOrDefault("ps", ""));
        request.setQ(params.getOrDefault("q", ""));
        request.setQualifiers(Collections.singletonList(params.getOrDefault("qualifiers", "")));

        return client.measures().componentTree(request);
    }
}
