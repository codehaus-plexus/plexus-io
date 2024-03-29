package org.codehaus.plexus.components.io.filemappers;

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
import javax.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.components.io.fileselectors.AllFilesFileSelector;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.testing.PlexusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test case for implementations of {@link FileSelector}.
 */
@PlexusTest
public class FileSelectorTest {

    @Inject
    PlexusContainer container;

    protected void testFileSelector(FileSelector pSelector, String[] pInput, boolean[] pOutput) throws IOException {
        for (int i = 0; i < pInput.length; i++) {
            final String name = pInput[i];
            AbstractPlexusIoResource resource = new AbstractPlexusIoResource(name, 0, 0, true, false, true) {
                @Nonnull
                public InputStream getContents() {
                    throw new IllegalStateException("Not implemented");
                }

                public URL getURL() {
                    throw new IllegalStateException("Not implemented");
                }
            };
            boolean result = pSelector.isSelected(resource);
            if (result != pOutput[i]) {
                fail("Test fails for selector " + pSelector.getClass().getName()
                        + " and input " + name + ": Expected "
                        + pOutput[i]);
            }
        }
    }

    protected static final String[] SAMPLES = new String[] {"foo/x.gif", "foo/y.png", "bar/x.gif"};

    protected void testFileSelector(AllFilesFileSelector pSelector) throws Exception {
        final boolean[] trues = getAllTrues();
        testFileSelector(pSelector, SAMPLES, trues);
    }

    private boolean[] getAllTrues() {
        final boolean[] trues = new boolean[SAMPLES.length];
        Arrays.fill(trues, true);
        return trues;
    }

    @Test
    void testAllFilesFileSelector() throws Exception {
        testFileSelector(new AllFilesFileSelector());
        testFileSelector((AllFilesFileSelector) container.lookup(FileSelector.class));
        testFileSelector((AllFilesFileSelector) container.lookup(FileSelector.class, AllFilesFileSelector.ROLE_HINT));
    }

    protected boolean[] getIncludeGifs(String[] pSamples) {
        boolean[] result = new boolean[pSamples.length];
        for (int i = 0; i < pSamples.length; i++) {
            result[i] = pSamples[i].endsWith(".gif");
        }
        return result;
    }

    protected boolean[] getExcludeBar(String[] pSamples, boolean[] pResult) {
        for (int i = 0; i < pSamples.length; i++) {
            if (pSamples[i].startsWith("bar/")) {
                pResult[i] = false;
            }
        }
        return pResult;
    }

    protected void testFileSelector(IncludeExcludeFileSelector pSelector) throws Exception {
        testFileSelector(pSelector, SAMPLES, getAllTrues());
        pSelector.setIncludes(new String[] {"**/*.gif"});
        testFileSelector(pSelector, SAMPLES, getIncludeGifs(SAMPLES));
        pSelector.setExcludes(new String[] {"bar/*"});
        testFileSelector(pSelector, SAMPLES, getExcludeBar(SAMPLES, getIncludeGifs(SAMPLES)));
        pSelector.setIncludes(null);
        testFileSelector(pSelector, SAMPLES, getExcludeBar(SAMPLES, getAllTrues()));
    }

    @Test
    void testIncludeExcludeFileSelector() throws Exception {
        testFileSelector(new IncludeExcludeFileSelector());
        testFileSelector((IncludeExcludeFileSelector)
                container.lookup(FileSelector.class, IncludeExcludeFileSelector.ROLE_HINT));
    }

    @Test
    void testIncludeExcludeFileSelector_SetExcludes() {
        IncludeExcludeFileSelector selector = new IncludeExcludeFileSelector();

        // Test that the setExcludes method does not modify the excludes.
        selector.setExcludes(SAMPLES);
        String[] sltrExcludes = selector.getExcludes();
        assertEquals(SAMPLES.length, sltrExcludes.length);
        for (int i = 0; i < sltrExcludes.length; ++i) {
            assertEquals(SAMPLES[i], sltrExcludes[i]);
        }
    }
}
