package org.sonarsource.plugins.dependencies.matchers;

import java.util.regex.Matcher;

public interface LanguageMatcher {
    Matcher getMatcher();
    boolean matches(String text);
}
