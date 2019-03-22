package org.sonarsource.plugins.dependencies.webapp;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class DependencyPageDefinition implements PageDefinition {

    public static final String PARAM_CLASS_NAME = "className";

    public void define(Context context) {
        context.addPage(Page.builder("sonartest/test")
        .setName("Dependencies").setScope(Page.Scope.COMPONENT).build());
    }
}
