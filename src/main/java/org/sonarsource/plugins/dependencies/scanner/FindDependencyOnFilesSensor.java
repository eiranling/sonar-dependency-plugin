package org.sonarsource.plugins.dependencies.scanner;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.sonarsource.plugins.dependencies.compute.CustomMetrics.CONNECTED_DEPENDENCIES;

public class FindDependencyOnFilesSensor implements Sensor {

    private Logger logger = Loggers.get(FindDependencyOnFilesSensor.class);

    public FindDependencyOnFilesSensor() {
    }

    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Dependency finder");
    }

    public void execute(SensorContext sensorContext) {
        logger.info("Executing dependency sensor...");
        FileSystem fs = sensorContext.fileSystem();
        logger.info("Scanning in: "+fs.baseDir().getPath());
        Iterable<InputFile> files = fs.inputFiles(fs.predicates().all()); /*fs.predicates().hasLanguage("java")*/
        files.iterator().forEachRemaining(inputFile -> logger.info("Scanning: " + inputFile.filename()));
        for (InputFile file: files) {
            logger.info("Found file: "+file.filename());
            try {
                int dependencies = getPackageDependencies(file.contents());
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

    private int getPackageDependencies(String contents) {
        try {
            String[] statements = contents.split("[;\\n]");
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

    private boolean matchesImportStatement(String statement) {
        //
        //(?<package>.+)\.(\*|[a-zA-Z_$][a-zA-Z\d_$]*)
        return statement.matches("^import .+$");
    }
}
