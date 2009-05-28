package org.codehaus.plexus.components.io.resources.proxy;

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceWithAttributes;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceWithAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlexusIoProxyResourceWithAttributes
    extends AbstractPlexusIoResourceWithAttributes
{

    private final PlexusIoResourceWithAttributes src;

    public PlexusIoProxyResourceWithAttributes( PlexusIoResourceWithAttributes plexusIoResource )
    {
        this.src = plexusIoResource;
        setName( src.getName() );
        setAttributes( src.getAttributes() );
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
