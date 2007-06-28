package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class PlexusIoZipFileResourceCollection extends AbstractPlexusIoArchiveResourceCollection
{
    /**
     * The zip file resource collections role hint.
     */
    public static final String ROLE_HINT = "zipFile";

    /**
     * The zip file resource collections role hint for jar files.
     */
    public static final String JAR_ROLE_HINT = "jarFile";

    protected Iterator getEntries() throws IOException
    {
        final File f = getFile();
        if ( f == null )
        {
            throw new IOException( "The zip file has not been set." );
        }
        final URL url = new URL( "jar:" + f.toURI().toURL() + "!/");
        final ZipFile zipFile = new ZipFile( f );
        final Enumeration en = zipFile.entries();
        return new Iterator(){
            public boolean hasNext()
            {
                return en.hasMoreElements();
            }
            public Object next()
            {
                final ZipEntry entry = (ZipEntry) en.nextElement();
                final PlexusIoURLResource res = new PlexusIoURLResource(){
                    public URL getURL() throws IOException
                    {
                        return new URL( url, entry.getName() );
                    }
                };
                final boolean dir = entry.isDirectory();
                res.setName( entry.getName() );
                res.setDirectory( dir );
                res.setExisting( true );
                res.setFile( !dir );
                long l = entry.getTime();
                res.setLastModified( l == -1 ? PlexusIoResource.UNKNOWN_MODIFICATION_DATE : l );
                res.setSize( dir ? PlexusIoResource.UNKNOWN_RESOURCE_SIZE : entry.getSize() );
                return res;
            }
            public void remove()
            {
                throw new UnsupportedOperationException( "Removing isn't implemented." );
            }
        };
    }
}
