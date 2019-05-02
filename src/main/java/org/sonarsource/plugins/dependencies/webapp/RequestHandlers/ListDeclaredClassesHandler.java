package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonarqube.ws.Measures;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.MeasuresComponentTreeRequestor;

import java.util.Map;

public class ListDeclaredClassesHandler extends ListRequestHandler {

    private MeasuresComponentTreeRequestor componentTreeRequestor;

    public ListDeclaredClassesHandler(MeasuresComponentTreeRequestor componentTreeRequestor) {
        this.componentTreeRequestor = componentTreeRequestor;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        Map<String, String> params = handleParams(request);
        params.put("metricKeys", "declared_classes"); // We only care about dependencies here

        // makes the request to the web API to retrieve
        // the dependencies metric stored in the measures/component_tree endpoint.
        Measures.ComponentTreeWsResponse treeResponse = componentTreeRequestor.componentTreeRequest(
                request.localConnector(),
                params);

        constructResponse(response, treeResponse, "declaredClasses");
    }

    @Override
    protected void setDescription(WebService.NewAction action) {
        action.setDescription("Lists the declared classes in a all file components");
    }
}
