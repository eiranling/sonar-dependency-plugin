package org.sonarsource.plugins.dependencies;

import org.sonar.api.Plugin;
import org.sonarsource.plugins.dependencies.br.BRClassReader;
import org.sonarsource.plugins.dependencies.br.BRDependencyFinder;
import org.sonarsource.plugins.dependencies.compute.CustomMetrics;
import org.sonarsource.plugins.dependencies.scanner.FindDependencyOnFilesSensor;
import org.sonarsource.plugins.dependencies.service.ClassDependencyService;
import org.sonarsource.plugins.dependencies.service.DeclaredClassService;
import org.sonarsource.plugins.dependencies.webapp.DependencyPageDefinition;
import org.sonarsource.plugins.dependencies.webapp.DependencyWs;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.GetDependenciesHandler;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.ListDependenciesHandler;

public class SamplePlugin implements Plugin {

    public void define(Context context) {
        context.addExtensions(
                //Metrics
                CustomMetrics.class,

                // Scanner classes
                FindDependencyOnFilesSensor.class,
                ClassDependencyService.class,
                DeclaredClassService.class,
                BRClassReader.class,
                BRDependencyFinder.class,

                // Webservice API classes
                DependencyWs.class,
                GetDependenciesHandler.class,
                ListDependenciesHandler.class,

                // Dependency Graph Page
                DependencyPageDefinition.class
        );

    }
}
