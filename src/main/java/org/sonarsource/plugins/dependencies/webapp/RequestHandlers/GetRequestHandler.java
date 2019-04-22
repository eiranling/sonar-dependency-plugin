package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.WebService;

@ServerSide
public abstract class GetRequestHandler implements RequestHandler {
    public void define(WebService.NewController controller) {
        WebService.NewAction action = controller.createAction("get")
                .setDescription("Gets the dependencies of a file in a project")
                .setHandler(this);

        action.createParam("componentKey")
                .setRequired(true)
                .setExampleValue("projectKey:component")
                .setDescription("The key of the to find dependencies of");
    }
}
