package org.sonarsource.plugins.dependencies.webapp;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class DependencyPageDefinition implements PageDefinition {

    public void define(Context context) {
        context.addPage(Page.builder("dependencies/graph")
        .setName("Dependency Graph").setScope(Page.Scope.COMPONENT)
                .setComponentQualifiers(Page.Qualifier.PROJECT).build());

        context.addPage(Page.builder("dependencies/measures-history")
                .setName("Measures History").setScope(Page.Scope.COMPONENT)
                .setComponentQualifiers(Page.Qualifier.PROJECT).build());
    }
}
