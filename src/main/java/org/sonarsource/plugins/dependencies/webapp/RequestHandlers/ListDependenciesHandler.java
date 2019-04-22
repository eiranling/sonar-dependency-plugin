package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonarqube.ws.Measures;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.MeasuresComponentTreeRequestor;

import java.util.Map;

public class ListDependenciesHandler extends ListRequestHandler {

    private MeasuresComponentTreeRequestor componentTreeRequestor;

    public ListDependenciesHandler(MeasuresComponentTreeRequestor componentTreeRequestor) {
        this.componentTreeRequestor = componentTreeRequestor;
    }

    /**
     * Function to handle the request made to the specific endpoint
     * @param request the request
     * @param response the response object to write json data to.
     */
    @Override
    public void handle(Request request, Response response) {
        Map<String, String> params = handleParams(request);
        params.put("metricKeys", "dependencies"); // We only care about dependencies here

        // makes the request to the web API to retrieve
        // the dependencies metric stored in the measures/component_tree endpoint.
        Measures.ComponentTreeWsResponse treeResponse = componentTreeRequestor.componentTreeRequest(
                request.localConnector(),
                params);

        constructResponse(response, treeResponse, "dependencies");

    }

    @Override
    protected void setDescription(WebService.NewAction action) {
        action.setDescription("Lists the child components and their dependencies");
    }
}
