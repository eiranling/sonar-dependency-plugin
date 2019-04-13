package org.sonarsource.plugins.dependencies.util;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.dependencies.matchers.ClassMatcher;
import org.sonarsource.plugins.dependencies.matchers.LanguageMatcher;

import java.io.IOException;
import java.util.ArrayList;

import static org.sonarsource.plugins.dependencies.compute.CustomMetrics.CONNECTED_DEPENDENCIES;

public class BRDependencyFinder {

    private InputFile file;
    private SensorContext context;
    private Logger logger = Loggers.get("DependencyFinder");

    public BRDependencyFinder(InputFile file, SensorContext context) {
        this.file = file;
        this.context = context;
    }

    public void execute() {
        logger.info("Found file: "+file.filename());
        try {
            String dependencies = findDependencies(file);
            logger.info("Found " + dependencies.split(";").length + " dependencies in "+file.filename());

            context.<String>newMeasure()
                    .forMetric(CONNECTED_DEPENDENCIES)
                    .on(file)
                    .withValue(dependencies)
                    .save();
        } catch (IOException e) {
            logger.error("Error reading file: "+file.filename());
        }
    }

    private String findDependencies(InputFile file) throws IOException {
        try {
            String[] statements = file.contents().split("[;\\n]");

            LanguageMatcher matcher = new ClassMatcher();

            StringBuilder imports = new StringBuilder();
            for (String statement : statements) {
                matcher.getMatcher(statement);
                if (matcher.matches()) {
                    imports.append(matcher.findMatched()).append(";");
                }
            }

            return imports.toString();
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }


}
