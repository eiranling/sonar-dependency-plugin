package org.sonarsource.plugins.dependencies.webapp.RequestHandlers;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;

import java.net.MalformedURLException;
import java.net.URL;

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
    public void handle(Request request, Response response) throws MalformedURLException {
        okhttp3.Request metricRequest = new okhttp3.Request.Builder()
                .url(request.getPath()+"../../measures/component_tree")
                .get().build();

        response.newJsonWriter()
                .beginObject()
                .prop("componentKey", request.mandatoryParam("componentKey"))
                .endObject()
                .close();
    }
}
