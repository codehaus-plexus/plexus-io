package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class PlexusIoSymlinkResource
    extends PlexusIoFileResource
    implements SymlinkDestinationSupplier
{
    private final File symnlinkDestination;

    PlexusIoSymlinkResource( @Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs )
        throws IOException
    {
        super( symlinkfile, name, attrs);
        this.symnlinkDestination = null;
    }

    public String getSymlinkDestination()
        throws IOException
    {
        return symnlinkDestination == null ? SymlinkUtils.readSymbolicLink( getFile() ).getPath() : symnlinkDestination.getPath();
    }
}
