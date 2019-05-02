package org.sonarsource.plugins.dependencies.service;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.dependencies.br.BRClassReader;
import org.sonarsource.plugins.dependencies.br.BRDependencyFinder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@ScannerSide
public class ClassDependencyService {

    private Map<InputFile, String> dependencyMap;
    private BRDependencyFinder finder;
    private BRClassReader reader;
    private List<String> declaredClasses;
    private Logger logger = Loggers.get(getClass());

    public ClassDependencyService(BRDependencyFinder finder, BRClassReader reader) {
        dependencyMap = new HashMap<>();
        declaredClasses = new ArrayList<>();
        this.reader = reader;
        this.finder = finder;
    }

    public void initializeDependencyMap(Iterable<InputFile> files) {
        for (InputFile file : files) dependencyMap.putIfAbsent(file, "");
    }

    public Map<InputFile, String> getDependencyMap() {
        return dependencyMap;
    }

    public void findDependencies() throws IOException {
        for (InputFile file : dependencyMap.keySet()) {
            try {
                dependencyMap.replace(file, finder.findDependencies(file));
                declaredClasses.addAll(reader.findDeclaredClasses(file));
            } catch (IOException e) {
                logger.error("Failed to find dependencies of " + file.filename());
                throw e;
            }
        }

        for (InputFile file : dependencyMap.keySet()) {
            List<String> dependentClasses = Arrays.asList(dependencyMap.get(file).split(";"));
            dependentClasses = dependentClasses.stream().filter(s -> declaredClasses.contains(s)).collect(Collectors.toList());
            dependencyMap.replace(file, String.join(";", dependentClasses));
        }
    }


}
