package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.util.Iterator;


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
     * @throws IOException 
     */
    Iterator getResources() throws IOException;

    /**
     * Returns the resources suggested name. This is used for
     * integrating file mappers.
     * @param resource A resource, which has been obtained by
     *   calling {@link #getResources()}.
     */
    String getName( PlexusIoResource resource ) throws IOException;
}
