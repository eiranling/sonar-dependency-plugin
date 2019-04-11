package org.sonarsource.plugins.dependencies.matchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassMatcher implements LanguageMatcher {

    private Matcher matcher;

    public ClassMatcher(String input) {
        matcher = Pattern.compile("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)+\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*").matcher(input);
    }

    @Override
    public Matcher getMatcher() {
        return matcher;
    }

    @Override
    public boolean matches(String text) {
        return matcher.find();
    }
}
