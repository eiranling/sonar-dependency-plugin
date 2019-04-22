package org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.LocalConnector;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.measures.ComponentRequest;

import java.util.List;

@ServerSide
public class MeasuresComponentRequest extends LocalApiCaller {

    public Measures.ComponentWsResponse componentRequest(LocalConnector connector, List<String> metricKeys, String componentKey) {
        WsClient client = generateClient(connector);

        ComponentRequest request = new ComponentRequest();
        request.setMetricKeys(metricKeys);
        request.setComponent(componentKey);

        return client.measures().component(request);
    }
}
