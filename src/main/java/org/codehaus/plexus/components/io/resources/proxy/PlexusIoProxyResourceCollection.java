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

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceCollection;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceCollectionWithAttributes;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceWithAttributes;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link PlexusIoResourceCollection} for an archives contents.
 */
public class PlexusIoProxyResourceCollection
    extends AbstractPlexusIoResourceCollectionWithAttributes
{
    private PlexusIoResourceCollection src;


    public PlexusIoProxyResourceCollection( @Nonnull PlexusIoResourceCollection src )
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

    public void setDefaultAttributes( final int uid, final String userName, final int gid, final String groupName,
                                      final int fileMode, final int dirMode )
    {
        setDefaultFileAttributes( new SimpleResourceAttributes( uid, userName, gid, groupName, fileMode ) );

        setDefaultDirAttributes( new SimpleResourceAttributes( uid, userName, gid, groupName, dirMode ) );
    }

    public void setOverrideAttributes( final int uid, final String userName, final int gid, final String groupName,
                                       final int fileMode, final int dirMode )
    {
        setOverrideFileAttributes( new SimpleResourceAttributes( uid, userName, gid, groupName, fileMode ) );

        setOverrideDirAttributes( new SimpleResourceAttributes( uid, userName, gid, groupName, dirMode ) );
    }

    @Override
    public void setStreamTransformer( InputStreamTransformer streamTransformer )
    {
        if (src instanceof AbstractPlexusIoResourceCollection ){
            ((AbstractPlexusIoResourceCollection)src).setStreamTransformer( streamTransformer );
        }
        super.setStreamTransformer( streamTransformer );
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

	private String getNonEmptyPrfix(){
		String prefix = getPrefix();
		if ( prefix != null && prefix.length() == 0 )
		{
			return null;
		}
		return prefix;

	}
    public Iterator<PlexusIoResource> getResources()
        throws IOException
    {
        final List<PlexusIoResource> result = new ArrayList<PlexusIoResource>();
        final FileSelector fileSelector = getDefaultFileSelector();
        final String prefix = getNonEmptyPrfix();
        for ( final Iterator<PlexusIoResource> iter = getSrc().getResources(); iter.hasNext(); )
        {
            PlexusIoResource plexusIoResource = iter.next();

            PlexusIoResourceAttributes attrs = null;
            if ( plexusIoResource instanceof PlexusIoResourceWithAttributes )
            {
                attrs = ( (PlexusIoResourceWithAttributes) plexusIoResource ).getAttributes();
            }

            if ( plexusIoResource.isDirectory() )
            {
                attrs =
                    PlexusIoResourceAttributeUtils.mergeAttributes(
                        getOverrideDirAttributes(), attrs, getDefaultDirAttributes() );
            }
            else
            {
                attrs =
                    PlexusIoResourceAttributeUtils.mergeAttributes(
                        getOverrideFileAttributes(), attrs, getDefaultFileAttributes() );
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
                    plexusIoResource = new PlexusIoProxyResourceWithAttributes( plexusIoResource, attrs ){
						@Override public String getName() {
							return prefix + name;
						}
					};
                }
                else
                {
                    plexusIoResource = new PlexusIoProxyResourceWithAttributes( plexusIoResource, attrs ){
						@Override public String getName() {
							return prefix + name;
						}
					};
                }
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
}
