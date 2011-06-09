package org.codehaus.plexus.components.io.resources.proxy;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIOResourceCollectionWithAttributes;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceWithAttributes;

/**
 * Implementation of {@link PlexusIoResourceCollection} for an archives contents.
 */
public class PlexusIoProxyResourceCollection
    extends AbstractPlexusIoResourceCollection
    implements PlexusIOResourceCollectionWithAttributes
{
    private PlexusIoResourceCollection src;

    private SimpleResourceAttributes defaultFileAttributes;

    private SimpleResourceAttributes defaultDirAttributes;

    private PlexusIoResourceAttributes overrideFileAttributes;

    private PlexusIoResourceAttributes overrideDirAttributes;

    /**
     * Sets the archive to read.
     */
    public void setSrc( final PlexusIoResourceCollection src )
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
        final IncludeExcludeFileSelector fileSelector = new IncludeExcludeFileSelector();
        fileSelector.setIncludes( getIncludes() );
        fileSelector.setExcludes( getExcludes() );
        fileSelector.setCaseSensitive( isCaseSensitive() );
        fileSelector.setUseDefaultExcludes( isUsingDefaultExcludes() );
        return fileSelector;
    }

    public Iterator<PlexusIoResource> getResources()
        throws IOException
    {
        final List<PlexusIoResource> result = new ArrayList<PlexusIoResource>();
        final FileSelector fileSelector = getDefaultFileSelector();
        String prefix = getPrefix();
        if ( prefix != null && prefix.length() == 0 )
        {
            prefix = null;
        }
        for ( final Iterator iter = getSrc().getResources(); iter.hasNext(); )
        {
            PlexusIoResource plexusIoResource = (PlexusIoResource) iter.next();

            PlexusIoResourceAttributes attrs = null;
            if ( plexusIoResource instanceof PlexusIoResourceWithAttributes )
            {
                attrs = ( (PlexusIoResourceWithAttributes) plexusIoResource ).getAttributes();
            }

            if ( plexusIoResource.isDirectory() )
            {
                attrs =
                    PlexusIoResourceAttributeUtils.mergeAttributes( overrideDirAttributes, attrs, defaultDirAttributes );
            }
            else
            {
                attrs =
                    PlexusIoResourceAttributeUtils.mergeAttributes( overrideFileAttributes, attrs,
                                                                    defaultFileAttributes );
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
                final String name = plexusIoResource.getName();

                if ( plexusIoResource instanceof PlexusIoResourceWithAttributes )
                {
                    plexusIoResource = new PlexusIoProxyResourceWithAttributes( plexusIoResource, attrs );
                }
                else
                {
                    plexusIoResource = new PlexusIoProxyResourceWithAttributes( plexusIoResource, attrs );
                }

                ( (AbstractPlexusIoResource) plexusIoResource ).setName( prefix + name );
            }

            result.add( plexusIoResource );
        }
        return result.iterator();
    }

    public String getName( final PlexusIoResource resource )
        throws IOException
    {
        String name = resource.getName();
        final FileMapper[] mappers = getFileMappers();
        if ( mappers != null )
        {
            for ( FileMapper mapper : mappers )
            {
                name = mapper.getMappedFileName( name );
            }
        }
        /*
         * The prefix is applied when creating the resource. return PrefixFileMapper.getMappedFileName( getPrefix(),
         * name );
         */
        return name;
    }

    public long getLastModified()
        throws IOException
    {
        return src.getLastModified();
    }

    public void setDefaultAttributes( final int uid, final String userName, final int gid, final String groupName,
                                      final int fileMode, final int dirMode )
    {
        defaultFileAttributes = new SimpleResourceAttributes( new Integer( uid), userName, new Integer( gid), groupName, fileMode );
        defaultFileAttributes.setOctalMode( fileMode );

        defaultDirAttributes = new SimpleResourceAttributes( new Integer( uid), userName,new Integer(  gid), groupName, dirMode );
        defaultDirAttributes.setOctalMode( dirMode );
    }

    public void setOverrideAttributes( final int uid, final String userName, final int gid, final String groupName,
                                       final int fileMode, final int dirMode )
    {
        overrideFileAttributes = new SimpleResourceAttributes(  new Integer( uid), userName, new Integer( gid), groupName, fileMode );

        overrideDirAttributes = new SimpleResourceAttributes(  new Integer( uid), userName, new Integer( gid), groupName, dirMode );
    }
}