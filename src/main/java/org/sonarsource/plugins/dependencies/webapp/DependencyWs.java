package org.sonarsource.plugins.dependencies.webapp;

import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.GetDependenciesHandler;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.ListDependenciesHandler;

public class DependencyWs implements WebService {

    private GetDependenciesHandler getDependenciesHandler;
    private ListDependenciesHandler listDependenciesHandler;

    public DependencyWs(GetDependenciesHandler getDependenciesHandler,
                        ListDependenciesHandler listDependenciesHandler) {
        this.getDependenciesHandler = getDependenciesHandler;
        this.listDependenciesHandler = listDependenciesHandler;
    }

    public void define(Context context) {
        NewController controller = context.createController("api/dependencies");

        getDependenciesHandler.define(controller);
        listDependenciesHandler.define(controller);

        controller.done();
    }
}
