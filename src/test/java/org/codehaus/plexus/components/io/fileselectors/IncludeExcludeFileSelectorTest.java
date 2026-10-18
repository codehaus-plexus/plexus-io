package org.codehaus.plexus.components.io.fileselectors;

import javax.annotation.Nonnull;

import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncludeExcludeFileSelectorTest {

    /**
     * {@link IncludeExcludeFileSelector#isSelected} matches names that use the platform separator,
     * so every exclude pattern, including the default excludes, must use it too. Using a backslash
     * here reproduces the Windows behaviour on any platform.
     */
    @Test
    void excludePatternsUseTheGivenSeparator() {
        String[] patterns = IncludeExcludeFileSelector.asExcludePatterns(
                new String[] {"bar/*", "foo/"}, new String[] {"**/.svn/**", "**/.git"}, '\\');

        assertArrayEquals(new String[] {"bar\\*", "foo\\**", "**\\.svn\\**", "**\\.git"}, patterns);
    }

    @Test
    void defaultExcludesUseTheGivenSeparator() {
        String[] patterns = IncludeExcludeFileSelector.asExcludePatterns(null, FileUtils.getDefaultExcludes(), '\\');

        for (String pattern : patterns) {
            assertFalse(pattern.contains("/"), pattern);
        }
    }

    @Test
    void defaultExcludesApplyWithAndWithoutExcludes() {
        IncludeExcludeFileSelector selector = new IncludeExcludeFileSelector();

        selector.setExcludes(null);
        assertFalse(selector.isSelected(resource(".svn/entries")));
        assertFalse(selector.isSelected(resource("foo/.git/config")));
        assertTrue(selector.isSelected(resource("foo/bar.txt")));

        selector.setExcludes(new String[] {"**/*.gif"});
        assertFalse(selector.isSelected(resource(".svn/entries")));
        assertFalse(selector.isSelected(resource("foo/x.gif")));
        assertTrue(selector.isSelected(resource("foo/bar.txt")));

        selector.setUseDefaultExcludes(false);
        assertTrue(selector.isSelected(resource(".svn/entries")));
        assertFalse(selector.isSelected(resource("foo/x.gif")));
    }

    private static AbstractPlexusIoResource resource(String name) {
        return new AbstractPlexusIoResource(name, 0, 0, true, false, true) {
            @Nonnull
            public InputStream getContents() {
                throw new IllegalStateException("Not implemented");
            }

            public URL getURL() {
                throw new IllegalStateException("Not implemented");
            }
        };
    }
}
