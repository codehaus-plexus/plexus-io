package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;


/**
 * Abstract base class for compressed files, aka singleton
 * resource collections.
 */
public abstract class PlexusIoCompressedFileResourceCollection
    implements PlexusIoArchivedResourceCollection
{
    private File file;
    private String path;

    public File getFile()
    {
        return file;
    }

    public void setFile( File file )
    {
        this.file = file;
        
    }

    public String getPath()
    {
        return path;
    }

    public void setPath( String path )
    {
        this.path = path;
        
    }

    public Iterator getResources()
        throws IOException
    {
        final File f = getFile();
        final String p = (getPath() == null ? getName( f ) : getPath()).replace( '\\', '/' );
        if ( f == null )
        {
            throw new IOException( "No archive file is set." );
        }
        if ( ! f.isFile() )
        {
            throw new IOException( "The archive file " + f.getPath()
                                   + " does not exist or is no file." ); 
        }
        final PlexusIoResource resource = new PlexusIoFileResource(f, p){
            public InputStream getContents()
                throws IOException
            {
                return getInputStream( f );
            }
        };
        return Collections.singleton( resource ).iterator();
    }

    protected String getName( File file ) throws IOException {
        final String name = file.getPath();
        final String ext = getDefaultExtension();
        if ( ext != null  &&  ext.length() > 0  &&  name.endsWith( ext ) )
        {
            return name.substring( 0, name.length() - ext.length() );
        }
        return name;
    }

    protected abstract String getDefaultExtension();

    protected abstract InputStream getInputStream( File file ) throws IOException;

    public String getName( PlexusIoResource resource )
        throws IOException
    {
        return resource.getName();
    }

    public long getLastModified() throws IOException
    {
        File f = getFile();
        return f == null ? PlexusIoResource.UNKNOWN_MODIFICATION_DATE : f.lastModified();
    }
}
