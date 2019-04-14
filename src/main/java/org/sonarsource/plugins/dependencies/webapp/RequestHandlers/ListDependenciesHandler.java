package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.text.JsonWriter;

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
        response.newJsonWriter().beginObject()
                .name("dependencies")
                .beginArray().endArray().endObject().close();
    }
}
