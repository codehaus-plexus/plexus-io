package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlexusIoURLResource extends AbstractPlexusIoResource
{
    private URL url;

    
    public InputStream getContents() throws IOException
    {
        return getURL().openStream();
    }

    public URL getURL() throws IOException
    {
        return url;
    }

    public void setURL( URL pUrl )
    {
        url = pUrl;
    }

}
