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
    public void handle(Request request, Response response) {
        WsClient client = WsClientFactories.getLocal().newClient(request.localConnector());

        ComponentRequest componentRequest = new ComponentRequest()
                .setComponent(request.getParam("componentKey").getValue())
                .setMetricKeys(Lists.newArrayList("dependencies"));

        Measures.ComponentWsResponse measures = client.measures()
                .component(componentRequest);

        Loggers.get(getClass()).info(Boolean.toString(measures.hasMetrics()));
        Loggers.get(getClass()).info(measures.getComponent().getKey());
        Loggers.get(getClass()).info(measures.getField(measures.getDescriptorForType().findFieldByName("value")).toString());
        Loggers.get(getClass()).info(request.getParam("componentKey").getValue());

        Common.Metric dependencies = null;
        for (Common.Metric metric : measures.getMetrics().getMetricsList()) {
            if (metric.getKey().equals("dependencies")) {
                Loggers.get(getClass()).info(metric.getBestValue());
                dependencies = metric;
            }
            Loggers.get(getClass()).info(metric.getName());
            Loggers.get(getClass()).info(metric.getKey());
        }

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .prop("bestVal", dependencies.getBestValue())
                .prop("worstVal", dependencies.getWorstValue())
                .prop("description", dependencies.getDescription())
                .prop("allFieldsKey", dependencies.getAllFields().keySet().toString())
                .prop("allFieldsVal", dependencies.getAllFields().values().toString())
                .endObject()
                .close();
    }
}
