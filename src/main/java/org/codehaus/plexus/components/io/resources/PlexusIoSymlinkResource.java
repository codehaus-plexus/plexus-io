package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;

import java.io.File;
import java.io.IOException;

public class PlexusIoSymlinkResource
    extends PlexusIoFileResource
    implements PlexusIoSymlink
{
    private final File symnlinkDestination;

    public PlexusIoSymlinkResource( File symlinkFile, PlexusIoResourceAttributes symlinkAttrs,
                                    File symnlinkDestination )
    {
        super( symlinkFile, symlinkAttrs );
        this.symnlinkDestination = symnlinkDestination;
    }

    public PlexusIoSymlinkResource( File symlinkFile, PlexusIoResourceAttributes symlinkAttrs )
    {
        super( symlinkFile, symlinkAttrs );
        this.symnlinkDestination = null;
    }

    public PlexusIoSymlinkResource( File symlinkfile, String name, PlexusIoResourceAttributes attrs,
                                    File symnlinkDestination )
    {
        super( symlinkfile, name, attrs );
        this.symnlinkDestination = symnlinkDestination;
    }

    public PlexusIoSymlinkResource( File symlinkfile, String name, PlexusIoResourceAttributes attrs )
    {
        super( symlinkfile, name, attrs );
        this.symnlinkDestination = null;
    }

    public String getSymlinkDestination()
        throws IOException
    {
        return symnlinkDestination == null ? SymlinkUtils.readSymbolicLink( getFile() ).getPath() : symnlinkDestination.getPath();
    }
}
