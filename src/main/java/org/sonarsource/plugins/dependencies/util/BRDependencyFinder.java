package org.sonarsource.plugins.dependencies.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaParserBuild;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.dependencies.matchers.ClassMatcher;
import org.sonarsource.plugins.dependencies.matchers.LanguageMatcher;

import java.io.IOException;
import java.util.HashSet;

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
