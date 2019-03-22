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
        controller.createAction("list")
                .setDescription("List the dependencies for all classes")
                .setHandler(this);
    }

    public void handle(Request request, Response response) throws Exception {
        JsonWriter writer = response.newJsonWriter().beginObject().name("dependencies").beginArray();

        writer.endArray();
        writer.endObject().close();
    }
}
