package org.codehaus.plexus.components.io.resources;

import org.apache.commons.io.output.DeferredFileOutputStream;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PlexusIoSymlinkResource
    extends PlexusIoFileResource
    implements SymlinkDestinationSupplier
{
    private final String symLinkDestination;
    private final PlexusIoResource targetResource;

    PlexusIoSymlinkResource( @Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs )
        throws IOException
    {
        super( symlinkfile, name, attrs );
        Path path = symlinkfile.toPath();
        Path linkPath = java.nio.file.Files.readSymbolicLink( path );
        symLinkDestination = linkPath.toString();
        targetResource = ResourceFactory.createResource( path.resolveSibling( linkPath ).toFile() );
    }

    public String getSymlinkDestination()
        throws IOException
    {
        return targetResource.getName();
    }

    public PlexusIoResource getTarget()
    {
        return targetResource;
    }

    public PlexusIoResource getLink() throws IOException
    {
        return new PlexusIoFileResource( getFile(), getName(), getAttributes() );
    }

    @Override
    public long getSize()
    {
        DeferredFileOutputStream dfos = getDfos();
        if ( dfos == null )
        {
            return targetResource.getSize();
        }
        else if ( dfos.isInMemory() )
        {
            return dfos.getByteCount();
        }
        else
        {
            return dfos.getFile().length();
        }
    }

    @Override
    public boolean isDirectory()
    {
        return targetResource.isDirectory();
    }

    @Override
    public boolean isExisting()
    {
        return targetResource.isExisting();
    }

    @Override
    public boolean isFile()
    {
        return targetResource.isFile();
    }

    @Override
    public long getLastModified()
    {
        return targetResource.getLastModified();
    }

    @Nonnull
    @Override
    public PlexusIoResourceAttributes getAttributes()
    {
        return super.getAttributes();
    }
}
