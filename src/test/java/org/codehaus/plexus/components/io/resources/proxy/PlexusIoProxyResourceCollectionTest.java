package org.codehaus.plexus.components.io.resources.proxy;

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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.components.io.resources.Stream;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link PlexusIoProxyResourceCollection}.
 */
public class PlexusIoProxyResourceCollectionTest {
    private final String[] SAMPLE_INCLUDES = {"junk.*", "test/**", "dir*/file.xml"};

    private final String[] SAMPLE_EXCLUDES = {"*.junk", "somwhere/**"};

    @Test
    public void testGetDefaultFileSelector() throws Exception {
        PlexusIoProxyResourceCollection resCol = new PlexusIoProxyResourceCollection(null);

        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();

        resCol.setIncludes(SAMPLE_INCLUDES);
        resCol.setExcludes(SAMPLE_EXCLUDES);

        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();
    }

    class CloseableIterator implements Iterator<PlexusIoResource>, Closeable {
        boolean next = true;

        boolean closed = false;

        public void close() throws IOException {
            closed = true;
        }

        public boolean hasNext() {
            if (next) {
                next = false;
                return true;
            }
            return false;
        }

        public PlexusIoResource next() {
            return new AbstractPlexusIoResource("fud", 123, 22, true, false, false) {
                @Nonnull
                public InputStream getContents() throws IOException {
                    return null;
                }

                public URL getURL() throws IOException {
                    return null;
                }
            };
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testClosing() throws IOException {
        final CloseableIterator closeableIterator = new CloseableIterator();
        PlexusIoProxyResourceCollection resCol =
                new PlexusIoProxyResourceCollection(new AbstractPlexusIoResourceCollection() {
                    public Iterator<PlexusIoResource> getResources() throws IOException {
                        return closeableIterator;
                    }

                    public Stream stream() {
                        throw new UnsupportedOperationException();
                    }

                    public boolean isConcurrentAccessSupported() {
                        return true;
                    }
                });
        Iterator<PlexusIoResource> resources1 = resCol.getResources();
        resources1.hasNext();
        resources1.next();
        assertFalse(resources1.hasNext());
        ((Closeable) resources1).close();
        assertTrue(closeableIterator.closed);
    }
}
