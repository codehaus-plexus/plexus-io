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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.components.io.fileselectors.FileInfo;


/**
 * A resource is a file-like entity. It may be an actual file,
 * an URL, a zip entry, or something like that.
 */
public interface PlexusIoResource extends FileInfo
{
    /**
     * Unknown resource size.
     */
    public static final long UNKNOWN_RESOURCE_SIZE = -1;

    /**
     * Unknown modification date
     */
    public static final long UNKNOWN_MODIFICATION_DATE = 0;

    /**
     * Returns the date, when the resource was last modified, if known.
     * Otherwise, returns {@link #UNKNOWN_MODIFICATION_DATE}.
     * @see java.io.File#lastModified()
     */
    long getLastModified();

    /**
     * Returns, whether the resource exists.
     */
    boolean isExisting();

    /**
     * Returns the resources size, if known. Otherwise returns
     * {@link #UNKNOWN_RESOURCE_SIZE}.
     */
    long getSize();


    /**
     * Returns, whether the {@link FileInfo} refers to a file.
     */
    boolean isFile();

    /**
     * Returns, whether the {@link FileInfo} refers to a directory.
     */
    boolean isDirectory();

    /**
     * Creates an {@link java.io.InputStream}, which may be used to read
     * the files contents. This is useful, if the file selector
     * comes to a decision based on the files contents.
     *
     * Please note that this InputStream is unbuffered. Clients should wrap this in a
     * BufferedInputStream or attempt reading reasonably large chunks (8K+).
     */
    InputStream getContents() throws IOException;

    /**
     * Returns an {@link URL}, which may be used to reference the
     * resource, if possible.
     * @return An URL referencing the resource, if possible, or null.
     *   In the latter case, you are forced to use {@link #getInputStream()}.
     */
    URL getURL() throws IOException;
}