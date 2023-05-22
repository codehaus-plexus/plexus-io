package org.codehaus.plexus.components.io.resources;

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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.codehaus.plexus.components.io.functions.PlexusIoResourceConsumer;

/**
 * Default implementation of {@link PlexusIoFileResourceCollection} for
 * zip files, tar files, etc.
 * @author jwi
 *
 */
public abstract class AbstractPlexusIoArchiveResourceCollection extends AbstractPlexusIoResourceCollection
        implements PlexusIoArchivedResourceCollection {

    private File file;

    protected AbstractPlexusIoArchiveResourceCollection() {}

    /**
     * Sets the zip file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns the zip file
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns an iterator over the archives entries.
     * @return An iterator, may be java.io.Closeable
     * @throws java.io.IOException an IOException, doh
     */
    protected abstract Iterator<PlexusIoResource> getEntries() throws IOException;

    public Iterator<PlexusIoResource> getResources() throws IOException {
        return new FilteringIterator();
    }

    class FilteringIterator implements Iterator<PlexusIoResource>, Closeable {
        final Iterator<PlexusIoResource> it = getEntries();

        PlexusIoResource next;

        public FilteringIterator() throws IOException {}

        boolean doNext() {
            while (it.hasNext()) {
                PlexusIoResource candidate = it.next();
                try {
                    if (isSelected(candidate)) {
                        next = candidate;
                        return true;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }

        public boolean hasNext() {
            return doNext();
        }

        public PlexusIoResource next() {
            if (next == null) doNext();
            PlexusIoResource res = next;
            next = null;
            return res;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void close() throws IOException {
            if (it instanceof Closeable) {
                ((Closeable) it).close();
            }
        }
    }

    public Stream stream() {
        return new Stream() {
            public void forEach(PlexusIoResourceConsumer resourceConsumer) throws IOException {

                final Iterator<PlexusIoResource> it = getEntries();
                while (it.hasNext()) {
                    final PlexusIoResource res = it.next();
                    if (isSelected(res)) {
                        resourceConsumer.accept(res);
                    }
                }
                if (it instanceof Closeable) {
                    ((Closeable) it).close();
                }
            }
        };
    }

    public long getLastModified() throws IOException {
        File f = getFile();
        return f == null ? PlexusIoResource.UNKNOWN_MODIFICATION_DATE : f.lastModified();
    }
}
