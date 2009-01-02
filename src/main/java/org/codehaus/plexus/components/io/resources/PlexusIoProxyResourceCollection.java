package org.codehaus.plexus.components.io.resources;

/*
 * Copyright 2007 The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;


/**
 * Implementation of {@link PlexusIoResourceCollection} for an archives
 * contents.
 */
public class PlexusIoProxyResourceCollection
    extends AbstractPlexusIoResourceCollection
    implements PlexusIOResourceCollectionWithAttributes
{
    private PlexusIoResourceCollection src;

    private PlexusIoResourceAttributes overrideFileAttributes;

    private PlexusIoResourceAttributes overrideDirAttributes;
    
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
            
            PlexusIoResourceAttributes attrs = null;
            if ( plexusIoResource instanceof PlexusIoResourceWithAttributes )
            {
                attrs = ((PlexusIoResourceWithAttributes)plexusIoResource).getAttributes();
            }

            if ( plexusIoResource.isDirectory() )
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideDirAttributes, attrs );
            }
            else
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideFileAttributes, attrs );
            }
            
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
    
    public void setOverrideAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode )
    {
        overrideFileAttributes = new SimpleResourceAttributes( uid, userName, gid, groupName, fileMode );
        
        overrideDirAttributes = new SimpleResourceAttributes( uid, userName, gid, groupName, dirMode );
    }
}