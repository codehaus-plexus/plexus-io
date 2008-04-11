package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;


/**
 * Implementation of {@link PlexusIoResourceCollection} for an archives
 * contents.
 */
public class PlexusIoProxyResourceCollection extends AbstractPlexusIoResourceCollection
{
    private PlexusIoResourceCollection src;

    /**
     * Sets the archive to read.
     */
    public void setSrc( PlexusIoResourceCollection src )
    {
        this.src = src;
    }

    /**
     * Returns the archive to read.
     */
    public PlexusIoResourceCollection getSrc()
    {
        return src;
    }

    protected FileSelector getDefaultFileSelector()
    {
        IncludeExcludeFileSelector fileSelector = new IncludeExcludeFileSelector();
        fileSelector.setIncludes( getIncludes() );
        fileSelector.setExcludes( getExcludes() );
        fileSelector.setCaseSensitive( isCaseSensitive() );
        fileSelector.setUseDefaultExcludes( isUsingDefaultExcludes() );
        return fileSelector;
    }

    public Iterator getResources() throws IOException
    {
        final List result = new ArrayList();
        final FileSelector fileSelector = getDefaultFileSelector();
        String prefix = getPrefix();
        if ( prefix != null  &&  prefix.length() == 0 )
        {
            prefix = null;
        }
        for ( Iterator iter = getSrc().getResources();  iter.hasNext();  )
        {
            PlexusIoResource plexusIoResource = (PlexusIoResource) iter.next();
            if ( !fileSelector.isSelected( plexusIoResource ) )
            {
                continue;
            }
            if ( !isSelected( plexusIoResource ) )
            {
                continue;
            }
            if ( plexusIoResource.isDirectory() && !isIncludingEmptyDirectories() )
            {
                continue;
            }
            if ( prefix != null )
            {
                final PlexusIoResource r = plexusIoResource;
                AbstractPlexusIoResource resourceImpl = new AbstractPlexusIoResource( plexusIoResource )
                {
                    public InputStream getContents() throws IOException
                    {
                        return r.getContents();
                    }

                    public URL getURL() throws IOException
                    {
                        return r.getURL();
                    }
                };
                resourceImpl.setName( prefix + plexusIoResource.getName() );
                plexusIoResource = resourceImpl;
            }
            result.add( plexusIoResource );
        }
        return result.iterator();
    }

    public String getName( PlexusIoResource resource )
        throws IOException
    {
        String name = resource.getName();
        final FileMapper[] mappers = getFileMappers();
        if ( mappers != null )
        {
            for ( int i = 0;  i < mappers.length;  i++ )
            {
                name = mappers[i].getMappedFileName( name );
            }
        }
        /*
         * The prefix is applied when creating the resource.
         * return PrefixFileMapper.getMappedFileName( getPrefix(), name );
         */
        return name;
    }

    public long getLastModified()
        throws IOException
    {
        return src.getLastModified();
    }
}
