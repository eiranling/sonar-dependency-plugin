package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;

public class GetDependenciesHandler implements RequestHandler {

    public GetDependenciesHandler() {

    }

    public void define(WebService.NewController controller) {
        WebService.NewAction action = controller.createAction("get")
                .setDescription("Gets the dependencies of a file in a project")
                .setHandler(this);
        action.createParam("class")
                .setRequired(true)
                .setExampleValue("Foo.java");

        action.createParam("projectId")
                .setRequired(true)
                .setExampleValue("my:project");
    }

    public void handle(Request request, Response response) throws Exception {

    }
}
