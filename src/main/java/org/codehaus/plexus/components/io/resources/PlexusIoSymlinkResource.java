package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceCollection.identityTransformer;

public class PlexusIoSymlinkResource
    extends PlexusIoFileResource
    implements PlexusIoSymlink
{
    private final File symnlinkDestination;

    public PlexusIoSymlinkResource( @Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs )
    {
        super( symlinkfile, name, attrs, identityTransformer );
        this.symnlinkDestination = null;
    }

    public String getSymlinkDestination()
        throws IOException
    {
        return symnlinkDestination == null ? SymlinkUtils.readSymbolicLink( getFile() ).getPath() : symnlinkDestination.getPath();
    }
}
