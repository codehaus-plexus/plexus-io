package org.codehaus.plexus.components.io.resources.proxy;

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlexusIoProxyResource
    extends AbstractPlexusIoResource
{

    private final PlexusIoResource src;

    public PlexusIoProxyResource( PlexusIoResource plexusIoResource )
    {
        this.src = plexusIoResource;
        setName( src.getName() );
    }

    public long getLastModified()
    {
        return src.getLastModified();
    }

    public long getSize()
    {
        return src.getSize();
    }

    public boolean isDirectory()
    {
        return src.isDirectory();
    }

    public boolean isExisting()
    {
        return src.isExisting();
    }

    public boolean isFile()
    {
        return src.isFile();
    }

    public URL getURL()
        throws IOException
    {
        return src.getURL();
    }

    public InputStream getContents()
        throws IOException
    {
        return src.getContents();
    }

}
