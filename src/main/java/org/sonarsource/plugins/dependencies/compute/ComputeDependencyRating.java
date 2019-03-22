package org.sonarsource.plugins.dependencies.compute;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import java.util.HashMap;
import java.util.Map;

public class ComputeDependencyRating implements MeasureComputer {

    private static final int B_THRESHOLD = 10;
    private static final int C_THRESHOLD = 15;
    private static final int D_THRESHOLD = 20;
    private static final int E_THRESHOLD = 100;

    private static final int RATING_A = 1;
    private static final int RATING_B = 2;
    private static final int RATING_C = 3;
    private static final int RATING_D = 4;
    private static final int RATING_E = 5;

    private static final Map<Integer, Integer> thresholdMap = new HashMap<Integer, Integer>();


    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        thresholdMap.put(RATING_E, E_THRESHOLD);
        thresholdMap.put(RATING_D, D_THRESHOLD);
        thresholdMap.put(RATING_C, C_THRESHOLD);
        thresholdMap.put(RATING_B, B_THRESHOLD);


        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setInputMetrics(CustomMetrics.CONNECTED_DEPENDENCIES.key())
                .setOutputMetrics(CustomMetrics.DEPENDENCIES_RATING.key())
                .build();
    }

    public void compute(MeasureComputerContext measureComputerContext) {
        Measure dependencies = measureComputerContext.getMeasure(CustomMetrics.CONNECTED_DEPENDENCIES.key());
        if (dependencies != null) {
            int rating = RATING_A;
            int maxThres = B_THRESHOLD;
            for (Integer val : thresholdMap.keySet()) {
                if (thresholdMap.get(val) > maxThres && dependencies.getIntValue() >= thresholdMap.get(val)) {
                    maxThres = thresholdMap.get(val);
                    rating = val;
                }
            }
            measureComputerContext.addMeasure(CustomMetrics.DEPENDENCIES_RATING.key(), rating);
        }
    }
}
