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
import java.util.Iterator;

import org.codehaus.plexus.components.io.attributes.FileAttributes;


/**
 * A resource collection is a set of {@link PlexusIoResource} instances.
 */
public interface PlexusIoResourceCollection
{
    /**
     * Role of the ResourceCollection component.
     */
    public static final String ROLE = PlexusIoResourceCollection.class.getName();

    /**
     * Role hint of the default resource collection, which is a set
     * of files in a base directory.
     */
    public static final String DEFAULT_ROLE_HINT = "default";

    /**
     * Returns an iterator over the resources in the collection.
     */
    Iterator getResources() throws IOException;

    /**
     * Returns the resources suggested name. This is used for
     * integrating file mappers.
     * @param resource A resource, which has been obtained by
     *   calling {@link #getResources()}.
     */
    String getName( PlexusIoResource resource ) throws IOException;

    /**
     * Returns the collections last modification time. For a
     * collection of files, this might be the last modification
     * time of the file, which has been modified at last. For an
     * archive file, this might be the modification time of the
     * archive file.
     * @return {@link PlexusIoResource#UNKNOWN_MODIFICATION_DATE},
     *   if the collections last modification time is unknown,
     *   otherwise the last modification time in milliseconds.
     */
    long getLastModified() throws IOException;

}