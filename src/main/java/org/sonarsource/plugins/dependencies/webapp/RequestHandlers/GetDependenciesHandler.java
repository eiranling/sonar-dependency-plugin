package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.log.Loggers;
import org.sonarqube.ws.Common;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.Users;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measures.ComponentRequest;
import org.sonarqube.ws.client.measures.ComponentTreeRequest;
import org.sonarsource.plugins.dependencies.webapp.util.ClientFactory;

import java.io.IOException;

public class GetDependenciesHandler implements RequestHandler {


    public GetDependenciesHandler() {

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
        WsClient client = WsClientFactories.getLocal().newClient(request.localConnector());

        ComponentRequest componentRequest = new ComponentRequest()
                .setComponent(request.getParam("componentKey").getValue())
                .setMetricKeys(Lists.newArrayList("dependencies"));

        Measures.ComponentWsResponse measures = client.measures()
                .component(componentRequest);

        for (Common.Metric metric : measures.getMetrics().getMetricsList()) {
            if (metric.getKey().equals("dependencies")) {
                Loggers.get(getClass()).info(metric.getBestValue());
            }
        }

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .endObject()
                .close();
    }
}
