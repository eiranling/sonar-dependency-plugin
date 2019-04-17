package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import com.google.common.collect.Lists;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.text.JsonWriter;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measures.ComponentTreeRequest;

public class ListDependenciesHandler implements RequestHandler {

    public ListDependenciesHandler() {

    }

    public void define(WebService.NewController controller) {
        WebService.NewAction action = controller.createAction("list")
                .setDescription("List the dependencies for all classes")
                .setHandler(this);

        action.createParam("baseComponentKey")
                .setDescription("Base component key")
                .setExampleValue("projectId")
                .setRequired(true);

        action.addPagingParams(100, 500);
        action.addSearchQuery("Class", "name", "path", "componentKey");

    }

    @Override
    public void handle(Request request, Response response) {
        WsClient client = WsClientFactories.getLocal().newClient(request.localConnector());

        ComponentTreeRequest treeRequest = new ComponentTreeRequest()
                .setComponent(request.getParam("baseComponentKey").getValue())
                .setMetricKeys(Lists.newArrayList("dependencies"));

        Measures.ComponentTreeWsResponse treeResponse = client.measures().componentTree(treeRequest);

        JsonWriter writer = response.newJsonWriter().beginObject()
                .name("dependencies")
                .beginArray();

        for (Measures.Component component : treeResponse.getComponentsList()) {
            writer.beginObject()
                    .prop("componentId", component.getId())
                    .prop("componentKey", component.getKey())
                    .prop("path", component.getPath())
                    .prop("name", component.getName());
            for (Measures.Measure measure : component.getMeasuresList()) {
                writer.prop(measure.getMetric(), measure.getValue());
            }
            writer.endObject();
        }

        writer.endArray().endObject().close();
    }
}
