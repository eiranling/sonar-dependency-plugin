package org.sonarsource.plugins.dependencies.service;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scanner.ScannerSide;
import org.sonarsource.plugins.dependencies.br.BRClassReader;

import java.io.IOException;
import java.util.List;

@ScannerSide
public class DeclaredClassService {

    private BRClassReader classReader;

    public DeclaredClassService(BRClassReader classReader) {
        this.classReader = classReader;
    }

    public String execute(InputFile file) throws IOException {
        List<String> classes = classReader.findDeclaredClasses(file);
        return String.join(";", classes);
    }


}
