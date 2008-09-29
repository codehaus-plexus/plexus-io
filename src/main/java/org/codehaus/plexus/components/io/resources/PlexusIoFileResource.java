package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;


/**
 * Implementation of {@link PlexusIoResource} for files.
 */
public class PlexusIoFileResource
    implements PlexusIoResourceWithAttributes
{
    private File file;

    private String name;

    private PlexusIoResourceAttributes attributes;

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource()
    {
        // Does nothing
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file )
    {
        this( file, file.getPath().replace( '\\', '/' ) );
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file, PlexusIoResourceAttributes attrs )
    {
        this( file, file.getPath().replace( '\\', '/' ), attrs );
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file, String name )
    {
        this.file = file;
        this.name = name;
    }
    
    public PlexusIoFileResource( File file, String name, PlexusIoResourceAttributes attrs )
    {
        this.file = file;
        this.name = name;
        this.attributes = attrs;
    }

    /**
     * Sets the resources file.
     */
    public void setFile( File file )
    {
        this.file = file;
    }

    /**
     * Returns the resources file.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Sets the resources name.
     */
    public void setName( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public long getLastModified()
    {
        return file.lastModified();
    }

    public boolean isFile()
    {
        return file.isFile();
    }

    public boolean isDirectory()
    {
        return file.isDirectory();
    }

    public boolean isExisting()
    {
        return file.exists();
    }

    public long getSize()
    {
        if ( !isExisting() )
        {
            return PlexusIoResource.UNKNOWN_RESOURCE_SIZE;
        }
        long result = file.length();
        return result == 0 ? PlexusIoResource.UNKNOWN_RESOURCE_SIZE : result;
    }

    public InputStream getContents() throws IOException
    {
        return new FileInputStream( getFile() );
    }

    public URL getURL() throws IOException
    {
        return getFile().toURI().toURL();
    }

    public PlexusIoResourceAttributes getAttributes()
    {
        return attributes;
    }

    public void setAttributes( PlexusIoResourceAttributes attributes )
    {
        this.attributes = attributes;
    }
}
