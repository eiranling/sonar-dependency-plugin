package org.sonarsource.plugins.dependencies.webapp;

import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.GetDeclaredClassesHandler;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.ListDeclaredClassesHandler;

public class DeclaredClassesWs implements WebService {

    private GetDeclaredClassesHandler getDeclaredClassesHandler;
    private ListDeclaredClassesHandler listDeclaredClassesHandler;

    public DeclaredClassesWs(GetDeclaredClassesHandler getDeclaredClassesHandler,
                             ListDeclaredClassesHandler listDeclaredClassesHandler) {
        this.getDeclaredClassesHandler = getDeclaredClassesHandler;
        this.listDeclaredClassesHandler = listDeclaredClassesHandler;
    }

    @Override
    public void define(Context context) {
        NewController controller = context.createController("api/declared_classes");

        getDeclaredClassesHandler.define(controller);
        listDeclaredClassesHandler.define(controller);

        controller.done();
    }
}
