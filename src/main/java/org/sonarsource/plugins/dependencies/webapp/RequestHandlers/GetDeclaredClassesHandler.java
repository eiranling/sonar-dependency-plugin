package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonarqube.ws.Measures;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.MeasuresComponentRequest;

import java.util.Collections;

public class GetDeclaredClassesHandler extends GetRequestHandler {

    private MeasuresComponentRequest componentRequest;

    public GetDeclaredClassesHandler(MeasuresComponentRequest componentRequest) {
        this.componentRequest = componentRequest;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        Measures.ComponentWsResponse measures = componentRequest
                .componentRequest(request.localConnector(),
                Collections.singletonList("declared_classes"),
                request.getParam("componentKey").getValue());

        String dependencies = "";
        for (Measures.Measure measure  : measures.getComponent().getMeasuresList()) {
            if (measure.getMetric().equals("declared_classes")) {
                dependencies = measure.getValue();
            }
        }

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .prop("name", measures.getComponent().getName())
                .prop("declared_classes", dependencies)
                .endObject()
                .close();
    }
}
