package org.sonarsource.plugins.dependencies;

import org.sonar.api.Plugin;
import org.sonarsource.plugins.dependencies.compute.ComputeDependencyAverage;
import org.sonarsource.plugins.dependencies.compute.ComputeDependencyRating;
import org.sonarsource.plugins.dependencies.compute.CustomMetrics;
import org.sonarsource.plugins.dependencies.scanner.FindDependencyOnFilesSensor;
import org.sonarsource.plugins.dependencies.webapp.DependencyPageDefinition;
import org.sonarsource.plugins.dependencies.webapp.DependencyWs;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.GetDependenciesHandler;
import org.sonarsource.plugins.dependencies.webapp.RequestHandlers.ListDependenciesHandler;

public class SamplePlugin implements Plugin {

    public void define(Context context) {
        context.addExtensions(
                FindDependencyOnFilesSensor.class,
                CustomMetrics.class,
                ComputeDependencyAverage.class,
                ComputeDependencyRating.class,
                DependencyWs.class,
                DependencyPageDefinition.class,
                GetDependenciesHandler.class,
                ListDependenciesHandler.class);


    }
}
