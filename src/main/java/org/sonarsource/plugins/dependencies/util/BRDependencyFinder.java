package org.sonarsource.plugins.dependencies.util;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

import java.io.IOException;
import java.util.ArrayList;

import static org.sonarsource.plugins.dependencies.compute.CustomMetrics.CONNECTED_DEPENDENCIES;

public class BRDependencyFinder {

    private InputFile file;
    private SensorContext context;

    public BRDependencyFinder(InputFile file, SensorContext context) {
        this.file = file;
        this.context = context;
    }

    public void execute() {
        //logger.info("Found file: "+file.filename());
        try {
            String dependencies = findDependencies(file);
            // logger.info("Found " + dependencies + " dependencies in "+file.filename());

            context.<String>newMeasure()
                    .forMetric(CONNECTED_DEPENDENCIES)
                    .on(file)
                    .withValue(dependencies)
                    .save();
        } catch (IOException e) {
            // logger.error("Error reading file: "+file.filename());
        }
    }

    public String findDependencies(InputFile file) throws IOException {
        try {
            String[] statements = file.contents().split("[;\\n]");

            int imports = 0;
            for (String statement : statements) {
                if (matchesImportStatement(statement)) {
                    imports++;
                }
            }

            return imports;
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }


}
