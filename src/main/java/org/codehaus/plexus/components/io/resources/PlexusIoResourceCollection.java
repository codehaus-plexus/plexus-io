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
import java.util.Iterator;

/**
 * A resource collection is a set of {@link PlexusIoResource} instances.
 */
public interface PlexusIoResourceCollection extends Iterable<PlexusIoResource>
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
     * @return An iterator
     * @throws java.io.IOException .
     */
    Iterator<PlexusIoResource> getResources() throws IOException;

    /**
     * Returns the resources as a stream.
     * @return A stream for functional iteration
     */
    public Stream stream();



        /**
         * Returns the resources suggested name. This is used for
         * integrating file mappers.
         * @param resource A resource, which has been obtained by
         *   calling {@link #getResources()}.
         * @return The resource name. If it is a file, it should be normalized to platform separators
         */
    String getName( PlexusIoResource resource );

    /**
     * Returns the collections last modification time. For a
     * collection of files, this might be the last modification
     * time of the file, which has been modified at last. For an
     * archive file, this might be the modification time of the
     * archive file.
     * @return {@link PlexusIoResource#UNKNOWN_MODIFICATION_DATE},
     *   if the collections last modification time is unknown,
     *   otherwise the last modification time in milliseconds.
     * @throws java.io.IOException .
     */
    long getLastModified() throws IOException;

    /**
     * Returns an input stream for the provided resource, with stream transformers applied
     * @param resource The resources
     * @return A possibly transformed resource
     * @throws IOException when something goes bad
     */
    InputStream getInputStream( PlexusIoResource resource ) throws IOException;

    /**
     * Resolves the supplide resource into a "real" resource. Resolving
     * means applying input transformations
     * Returns an input stream for the provided resource, with stream transformers applied
     * @param resource The resources
     * @return A possibly transformed resource
     * @throws IOException when something goes bad
     */
    PlexusIoResource resolve( PlexusIoResource resource ) throws IOException;

    /**
     * Indicates if this collection supports concurrent access to its resources.
     * <p>Some resource collections (like tar files) may not support efficient random access
     * or seek operation so implementations that represent such collections may not be able
     * to provide concurrent access to its resources. If implementation returns {@code false},
     * then it is not safe to access its methods and resources in concurrent fashion.
     * For example it is not safe to read from two resources in two concurrent threads,
     * to read a resource and iterate over the iterator returned by {@link #getResources()}
     * in two concurrent threads, etc.
     * <p>Please note that this method indicates concurrent support only for the collection,
     * not for the individual resources. This means there is no guarantee that
     * the resources returned by {@link #resolve(PlexusIoResource)} or the input stream
     * returned by {@link #getInputStream(PlexusIoResource)} are thread-safe,
     * even if {@code true} is returned.
     * @return {@code true} if this collection supports concurrent access,
     *   otherwise {@code false}
     */
    boolean isConcurrentAccessSupported();

}