package org.sonarsource.plugins.dependencies.scanner;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.dependencies.br.BRClassReader;
import org.sonarsource.plugins.dependencies.br.BRDependencyFinder;
import org.sonarsource.plugins.dependencies.service.ClassDependencyService;

import java.io.IOException;

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
        Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage("java")); /**/
        files.iterator().forEachRemaining(inputFile -> logger.info("Scanning: " + inputFile.filename()));
        ClassDependencyService service = new ClassDependencyService(files, new BRDependencyFinder(), new BRClassReader());
        try {
            service.findDependencies();
        } catch (IOException e) {
            logger.error("Failed to find dependencies", e);
        }

        for (InputFile file : files) {
            logger.info("Found file: " + file.filename());
            String dependencies = service.getDependencyMap().get(file);
            logger.info("Found " + dependencies.split(";").length + " dependencies in " + file.filename());

            sensorContext.<String>newMeasure()
                    .forMetric(CONNECTED_DEPENDENCIES)
                    .on(file)
                    .withValue(dependencies)
                    .save();
        }
    }
}
