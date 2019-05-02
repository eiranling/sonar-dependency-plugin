package org.sonarsource.plugins.dependencies.compute;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.ArrayList;
import java.util.List;

public class CustomMetrics implements Metrics {

    public static final Metric<String> CONNECTED_DEPENDENCIES = new Metric.Builder("dependencies", "dependencies", Metric.ValueType.STRING)
            .setDescription("The classes that this is linked to.")
            .setDirection(Metric.DIRECTION_NONE)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    public static final Metric<String> DECLARED_CLASSES = new Metric.Builder("declared_classes", "Declared Classes", Metric.ValueType.STRING)
            .setDescription("Classes defined in the file")
            .setDirection(Metric.DIRECTION_NONE)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(CONNECTED_DEPENDENCIES);
        metrics.add(DECLARED_CLASSES);
        return metrics;
    }
}
