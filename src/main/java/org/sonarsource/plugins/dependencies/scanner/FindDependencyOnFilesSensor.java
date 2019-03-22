package org.sonarsource.plugins.dependencies.scanner;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.IndexedFile;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;

import static org.sonarsource.plugins.dependencies.compute.CustomMetrics.CONNECTED_DEPENDENCIES;

public class FindDependencyOnFilesSensor implements Sensor {

    Logger logger = Loggers.get(FindDependencyOnFilesSensor.class);

    public FindDependencyOnFilesSensor() {
    }

    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Dependency finder");
    }

    public void execute(SensorContext sensorContext) {
        logger.info("Executing dependency sensor...");
        FileSystem fs = sensorContext.fileSystem();
        logger.info("Scanning in: "+fs.baseDir().getPath());
        Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage("java"));
        files.iterator().forEachRemaining(inputFile -> logger.info("Scanning: " + inputFile.filename()));
        for (InputFile file: files) {
            try {
                int dependencies = countImports(file.contents());
                logger.info("Found " + dependencies + " dependencies in "+file.filename());

                sensorContext.<Integer>newMeasure()
                        .forMetric(CONNECTED_DEPENDENCIES)
                        .on(file)
                        .withValue(dependencies)
                        .save();
            } catch (IOException e) {
                logger.error("Error reading file: "+file.filename());
            }
        }
    }

    private int countImports(String contents) {
        String[] statements = contents.split(";");
        int num_imports = 0;
        for (String statement : statements) {
            logger.info(statement);
            logger.info(Boolean.toString(matchesImportStatement(statement + ";")));
            if (matchesImportStatement(statement.replace("\n", "")+";")) {
                num_imports += 1;
            }
        }
        return num_imports;
    }

    private boolean matchesImportStatement(String statement) {
        return statement.matches("^import .+;$");
    }
}
