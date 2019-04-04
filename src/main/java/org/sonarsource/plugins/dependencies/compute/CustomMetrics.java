package org.sonarsource.plugins.dependencies.compute;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CustomMetrics implements Metrics {

    public static final Metric<Integer> CONNECTED_DEPENDENCIES = new Metric.Builder("dependencies", "dependencies", Metric.ValueType.INT)
            .setDescription("The number of dependencies the class is connected to.")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    public static final Metric<Integer> DEPENDENCIES_RATING = new Metric.Builder("dependencies_rating", "Class dependencies rating", Metric.ValueType.RATING)
            .setDescription("Rating based on number of dependencies a class is tied to")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(CONNECTED_DEPENDENCIES);
        metrics.add(DEPENDENCIES_RATING);
        return metrics;
    }
}
