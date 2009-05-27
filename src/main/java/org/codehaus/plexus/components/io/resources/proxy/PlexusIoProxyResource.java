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
