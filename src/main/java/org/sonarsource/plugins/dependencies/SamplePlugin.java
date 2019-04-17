package org.sonarsource.plugins.dependencies;

import org.sonar.api.Plugin;
import org.sonarsource.plugins.dependencies.compute.CustomMetrics;
import org.sonarsource.plugins.dependencies.scanner.FindDependencyOnFilesSensor;
import org.sonarsource.plugins.dependencies.webapp.DependencyWs;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.GetDependenciesHandler;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.ListDependenciesHandler;
import org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors.ComponentTreeMeasuresRequestBuilder;
import org.sonarsource.plugins.dependencies.webapp.util.ClientFactory;

public class SamplePlugin implements Plugin {

    public void define(Context context) {
        context.addExtensions(
                FindDependencyOnFilesSensor.class,
                CustomMetrics.class,
                DependencyWs.class,
                ClientFactory.class,
                GetDependenciesHandler.class,
                ComponentTreeMeasuresRequestBuilder.class,
                ListDependenciesHandler.class);

    }
}
