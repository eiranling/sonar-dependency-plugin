package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.text.JsonWriter;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.measures.ComponentTreeRequest;

import java.util.HashMap;
import java.util.Map;

@ServerSide
public abstract class ListRequestHandler implements RequestHandler {
    /**
     * Defines the action and valid parameters the endpoint handles.
     * @param controller the controller passed in from a webservice.
     */
    public void define(WebService.NewController controller) {
        WebService.NewAction action = controller.createAction("list")
                .setHandler(this);

        setDescription(action);

        action.createParam("baseComponentKey")
                .setDescription("Base component key")
                .setExampleValue("projectId")
                .setRequired(true);

        action.addPagingParams(100, 500);
        action.addSearchQuery("Class", "name", "path", "componentKey");

        action.createParam("qualifier").setRequired(false)
                .setDescription("The qualifier for the components to return.")
                .setExampleValue("FIL");
    }

    protected Map<String, String> handleParams(Request request) {
        Map<String, String> result = new HashMap<>();

        for (String paramKey : request.getParams().keySet()) {
            result.put(paramKey, request.getParam(paramKey).getValue());
        }

        return result;
    }

    /**
     * Constructs a generic response from a tree response
     * @param response response to write the json to
     * @param treeResponse response back from the server
     * @param name name
     */
    protected void constructResponse(Response response, Measures.ComponentTreeWsResponse treeResponse, String name) {
        // Writes the JSON object to the response object
        JsonWriter writer = response.newJsonWriter().beginObject()
                .name(name)
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

        // Only need to close the writer to send the response.
        writer.endArray().endObject().close();
    }

    /**
     * Abstract function to allow all request handlers to define their own description
     * @param action the action
     */
    protected abstract void setDescription(WebService.NewAction action);
}
