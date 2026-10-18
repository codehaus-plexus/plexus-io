package org.codehaus.plexus.components.io.fileselectors;

/*
 * Copyright 2007 The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Named;

import java.io.File;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.MatchPatterns;
import org.codehaus.plexus.util.SelectorUtils;

/**
 * This file selector uses a set of patterns for including/excluding
 * files.
 */
@Named(IncludeExcludeFileSelector.ROLE_HINT)
public class IncludeExcludeFileSelector implements FileSelector {
    /**
     * The include/exclude file selectors role-hint: "standard".
     */
    public static final String ROLE_HINT = "standard";

    private static final MatchPatterns ALL_INCLUDES = MatchPatterns.from(getCanonicalName("**/*"));

    private static final MatchPatterns ZERO_EXCLUDES = MatchPatterns.from();

    private boolean isCaseSensitive = true;

    private boolean useDefaultExcludes = true;

    private String[] includes;

    private String[] excludes;

    private MatchPatterns computedIncludes = ALL_INCLUDES;

    private MatchPatterns computedExcludes = ZERO_EXCLUDES;

    /**
     * Tests whether or not a name matches against at least one exclude
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         exclude pattern, or <code>false</code> otherwise.
     */
    protected boolean isExcluded(@Nonnull String name) {
        return computedExcludes.matches(name, isCaseSensitive);
    }

    /**
     * Sets the list of include patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param includes A list of include patterns.
     *                 May be <code>null</code>, indicating that all files
     *                 should be included. If a non-<code>null</code>
     *                 list is given, all elements must be
     *                 non-<code>null</code>.
     */
    public void setIncludes(@Nullable String[] includes) {
        this.includes = includes;
        if (includes == null) {
            computedIncludes = ALL_INCLUDES;
        } else {
            String[] cleaned;
            cleaned = new String[includes.length];
            for (int i = 0; i < includes.length; i++) {
                cleaned[i] = asPattern(includes[i]);
            }
            computedIncludes = MatchPatterns.from(cleaned);
        }
    }

    private static @Nonnull String getCanonicalName(@Nonnull String pName) {
        return getCanonicalName(pName, File.separatorChar);
    }

    private static @Nonnull String getCanonicalName(@Nonnull String pName, char separatorChar) {
        return pName.replace('/', separatorChar).replace('\\', separatorChar);
    }

    private static String asPattern(@Nonnull String pPattern) {
        return asPattern(pPattern, File.separatorChar);
    }

    private static String asPattern(@Nonnull String pPattern, char separatorChar) {
        String pattern = getCanonicalName(pPattern.trim(), separatorChar);
        if (pattern.endsWith(String.valueOf(separatorChar))) {
            pattern += "**";
        }
        return pattern;
    }

    /**
     * Returns the list of include patterns to use.
     *
     * @return A list of include patterns.
     *         May be <code>null</code>, indicating that all files
     *         should be included. If a non-<code>null</code>
     *         list is given, all elements must be
     *         non-<code>null</code>.
     */
    public @Nullable String[] getIncludes() {
        return includes;
    }

    /**
     * Sets the list of exclude patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param excludes A list of exclude patterns.
     *                 May be <code>null</code>, indicating that no files
     *                 should be excluded. If a non-<code>null</code> list is
     *                 given, all elements must be non-<code>null</code>.
     */
    public void setExcludes(@Nullable String[] excludes) {
        this.excludes = excludes;
        final String[] defaultExcludes = useDefaultExcludes ? FileUtils.getDefaultExcludes() : new String[] {};
        computedExcludes = MatchPatterns.from(asExcludePatterns(excludes, defaultExcludes, File.separatorChar));
    }

    /**
     * Converts the user's exclude patterns and the default excludes into patterns
     * that use the given separator, the same separator {@link #isSelected(FileInfo)}
     * uses for the names it matches.
     */
    static String[] asExcludePatterns(
            @Nullable String[] excludes, @Nonnull String[] defaultExcludes, char separatorChar) {
        final int count = excludes == null ? 0 : excludes.length;
        final String[] patterns = new String[count + defaultExcludes.length];
        for (int i = 0; i < count; i++) {
            patterns[i] = asPattern(excludes[i], separatorChar);
        }
        for (int i = 0; i < defaultExcludes.length; i++) {
            patterns[count + i] = asPattern(defaultExcludes[i], separatorChar);
        }
        return patterns;
    }

    /**
     * Returns the list of exclude patterns to use.
     *
     * @return A list of exclude patterns.
     *         May be <code>null</code>, indicating that no files
     *         should be excluded. If a non-<code>null</code> list is
     *         given, all elements must be non-<code>null</code>.
     */
    public @Nullable String[] getExcludes() {
        return excludes;
    }

    /**
     * Tests, whether the given pattern is matching the given name.
     * @param pattern The pattern to match
     * @param name The name to test
     * @param isCaseSensitive Whether the pattern is case sensitive.
     * @return True, if the pattern matches, otherwise false
     */
    protected boolean matchPath(@Nonnull String pattern, @Nonnull String name, boolean isCaseSensitive) {
        return SelectorUtils.matchPath(pattern, name, isCaseSensitive);
    }

    /**
     * Tests whether or not a name matches against at least one include
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         include pattern, or <code>false</code> otherwise.
     */
    protected boolean isIncluded(@Nonnull String name) {
        return computedIncludes.matches(name, isCaseSensitive);
    }

    public boolean isSelected(@Nonnull FileInfo fileInfo) {
        final String name = getCanonicalName(fileInfo.getName());
        return isIncluded(name) && !isExcluded(name);
    }

    /**
     * Returns, whether the include/exclude patterns are case sensitive.
     * @return True, if the patterns are case sensitive (default), or false.
     */
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    /**
     * Sets, whether the include/exclude patterns are case sensitive.
     * @param caseSensitive True, if the patterns are case sensitive (default), or false.
     */
    public void setCaseSensitive(boolean caseSensitive) {
        isCaseSensitive = caseSensitive;
    }

    /**
     * Returns, whether to use the default excludes, as specified by
     * {@link FileUtils#getDefaultExcludes()}.
     */
    public boolean isUseDefaultExcludes() {
        return useDefaultExcludes;
    }

    /**
     * Sets, whether to use the default excludes, as specified by
     * {@link FileUtils#getDefaultExcludes()}.
     */
    public void setUseDefaultExcludes(boolean pUseDefaultExcludes) {
        useDefaultExcludes = pUseDefaultExcludes;
        setExcludes(excludes);
    }
}
