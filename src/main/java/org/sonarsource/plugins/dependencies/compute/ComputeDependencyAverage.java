package org.sonarsource.plugins.dependencies.compute;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

public class ComputeDependencyAverage implements MeasureComputer {
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setOutputMetrics(CustomMetrics.CONNECTED_DEPENDENCIES.key())
                .build();
    }

    public void compute(MeasureComputerContext measureComputerContext) {
        if (measureComputerContext.getComponent().getType() != Component.Type.FILE) {
            int sum = 0;
            int count = 0;
            for (Measure measure : measureComputerContext.getChildrenMeasures(CustomMetrics.CONNECTED_DEPENDENCIES.key())) {
                sum += measure.getIntValue();
                count++;
            }
            int average = count == 0 ? 0 : sum / count;
            if (count != 0) {
                measureComputerContext.addMeasure(CustomMetrics.CONNECTED_DEPENDENCIES.key(), average);
            }
        }
    }
}
