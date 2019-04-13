package org.sonarsource.plugins.dependencies.matchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassMatcher implements LanguageMatcher {

    private Pattern pattern;
    private Matcher matcher;

    public ClassMatcher() {
        pattern = Pattern.compile("(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)+)((\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*) | \\*)");
    }

    @Override
    public Matcher getMatcher(String text) {
        matcher = pattern.matcher(text);
        return matcher;
    }

    @Override
    public boolean matches() {
        return matcher.find();
    }

    public String findMatched() {
        return matcher.group("package");
    }
}
