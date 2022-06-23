package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;

import java.io.File;
import java.io.IOException;

import static org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils.getFileAttributes;
import static org.codehaus.plexus.components.io.resources.PlexusIoFileResource.getName;

/**
 * @author Kristian Rosenvold
 */
public class ResourceFactory
{
    public static PlexusIoResource createResource( File f )
        throws IOException
    {
        return createResource( f, getName( f ), null, null, getFileAttributes( f ) );
    }

    public static PlexusIoResource createResource( File f, String name )
        throws IOException
    {
        return createResource( f, name, null, null, getFileAttributes( f ) );
    }

    public static PlexusIoResource createResource( File f, String name, final ContentSupplier contentSupplier,
                                                   PlexusIoResourceAttributes attributes )
        throws IOException
    {
        return createResource( f, name, contentSupplier, null, attributes );
    }

    public static PlexusIoResource createResource( File f, InputStreamTransformer inputStreamTransformer )
        throws IOException
    {
        return createResource( f, getName( f ), null, inputStreamTransformer, getFileAttributes( f ) );
    }

    public static PlexusIoResource createResource( File f, String name, final ContentSupplier contentSupplier,
                                                   InputStreamTransformer inputStreamTransformer )
        throws IOException
    {
        return createResource( f, name, contentSupplier, inputStreamTransformer, getFileAttributes( f ) );
    }

    public static PlexusIoResource createResource( File f, String name, final ContentSupplier contentSupplier,
                                                   InputStreamTransformer inputStreamTransformer,
                                                   PlexusIoResourceAttributes attributes )
        throws IOException
    {
        boolean symbolicLink = attributes.isSymbolicLink();
        return symbolicLink ? new PlexusIoSymlinkResource( f, name, attributes )
            :  new PlexusIoFileResource( f, name, attributes,
                    new FileAttributes( f, true ), contentSupplier, inputStreamTransformer );
    }

}
