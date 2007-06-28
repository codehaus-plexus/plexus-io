package org.codehaus.plexus.components.io.resources;


/**
 * Default implementation of {@link PlexusIoResource}.
 */
public abstract class AbstractPlexusIoResource implements PlexusIoResource
{
    private String name;

    private long lastModified, size;

    private boolean isFile, isDirectory, isExisting;

    /**
     * Creates a new instance with default settings.
     */
    public AbstractPlexusIoResource()
    {
    }

    /**
     * Creates a new instance. The settings are copied
     * from the given resource.
     */
    public AbstractPlexusIoResource( PlexusIoResource plexusIoResource )
    {
        setName( plexusIoResource.getName() );
        setLastModified( plexusIoResource.getLastModified() );
        setSize( plexusIoResource.getSize() );
        setFile( plexusIoResource.isFile() );
        setDirectory( plexusIoResource.isDirectory() );
        setExisting( plexusIoResource.isExisting() );
    }

    /**
     * Sets the date, when the resource was last modified.
     * @param Date of last modification, if known.
     *   Otherwise, {@link #UNKNOWN_MODIFICATION_DATE}.
     * @see java.io.File#lastModified()
     */
    public void setLastModified( long lastModified )
    {
        this.lastModified = lastModified;
    }

    public long getLastModified()
    {
        return lastModified;
    }

    /**
     * Sets the resources name, which may include path components,
     * like directory names, or something like that. The resources name
     * is expected to be a relative name and the path components must
     * be separated by {@link java.io.File#pathSeparator}
     */
    public void setName( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Sets the resources size.
     * @param size The resources size, if known. Otherwise returns
     *   {@link #UNKNOWN_RESOURCE_SIZE}.
     * @see java.io.File#length()
     */
    public void setSize( long size )
    {
        this.size = size;
    }

    public long getSize()
    {
        return size;
    }

    /**
     * Sets, whether the resource is a directory.
     */
    public void setDirectory( boolean isDirectory )
    {
        this.isDirectory = isDirectory;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    /**
     * Sets, whether the resource exists.
     */
    public void setExisting( boolean isExisting )
    {
        this.isExisting = isExisting;
    }

    public boolean isExisting()
    {
        return isExisting;
    }

    /**
     * Sets, whether the resource is a file.
     */
    public void setFile( boolean isFile )
    {
        this.isFile = isFile;
    }

    public boolean isFile()
    {
        return isFile;
    }
}
