package org.sonarsource.plugins.dependencies.br;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scanner.ScannerSide;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@ScannerSide
public class BRClassReader {

    public BRClassReader() {

    }

    public List<String> findDeclaredClasses(InputFile file) throws IOException {
        List<String> result;

        CompilationUnit unit = StaticJavaParser.parse(file.contents());
        result = unit.findAll(ClassOrInterfaceDeclaration.class).stream()
                .map(NodeWithSimpleName::getNameAsString)
                .collect(Collectors.toList());

        return result;
    }


}
