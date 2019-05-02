package org.sonarsource.plugins.dependencies.br;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.HashSet;

@ScannerSide
public class BRDependencyFinder {

    private Logger logger = Loggers.get("DependencyFinder");

    public BRDependencyFinder() {
    }

    public String findDependencies(InputFile file) throws IOException {
        try {
            HashSet<String> imports = new HashSet<>();
            CompilationUnit unit = StaticJavaParser.parse(file.contents());
            for (ClassOrInterfaceType type: unit.findAll(ClassOrInterfaceType.class)) {
                imports.add(type.getNameAsString());
            }

            return String.join(";", imports);
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
        }
    }


}
