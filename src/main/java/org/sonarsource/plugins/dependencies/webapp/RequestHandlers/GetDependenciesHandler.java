package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.log.Loggers;
import org.sonarqube.ws.Common;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measures.ComponentRequest;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.MeasuresComponentRequest;

import java.util.Collections;

public class GetDependenciesHandler extends GetRequestHandler {

    private MeasuresComponentRequest componentRequest;

    public GetDependenciesHandler(MeasuresComponentRequest componentRequest) {
        this.componentRequest = componentRequest;
    }

    @Override
    public void handle(Request request, Response response) {
        Measures.ComponentWsResponse measures = componentRequest.componentRequest(request.localConnector(),
                Collections.singletonList("dependencies"),
                request.getParam("componentKey").getValue());

        String dependencies = "";
        // There should only be one element here but just in case.
        for (Measures.Measure measure  : measures.getComponent().getMeasuresList()) {
            if (measure.getMetric().equals("dependencies")) {
                dependencies = measure.getValue();
            }
        }

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .prop("name", measures.getComponent().getName())
                .prop("dependencies", dependencies)
                .endObject()
                .close();
    }
}
