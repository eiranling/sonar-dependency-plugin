package org.sonarsource.plugins.dependencies.compute;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CustomMetrics implements Metrics {

    public static final Metric<String> CONNECTED_DEPENDENCIES = new Metric.Builder("dependencies", "dependencies", Metric.ValueType.STRING)
            .setDescription("The classes that this is linked to.")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(CONNECTED_DEPENDENCIES);
        return metrics;
    }
}
