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
        super( plexusIoResource );
        this.src = plexusIoResource;
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
