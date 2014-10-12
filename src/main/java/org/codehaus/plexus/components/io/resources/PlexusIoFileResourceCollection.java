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

import org.codehaus.plexus.components.io.attributes.Java7FileAttributes;
import org.codehaus.plexus.components.io.attributes.Java7Reflector;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.filemappers.PrefixFileMapper;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link PlexusIoResourceCollection} for the set
 * of files in a common directory.
 */
public class PlexusIoFileResourceCollection
    extends AbstractPlexusIoResourceCollectionWithAttributes
{
    /**
     * Role hint of this component
     */
    public static final String ROLE_HINT = "files";

    private File baseDir;

    private boolean isFollowingSymLinks = true;

    public PlexusIoFileResourceCollection()
    {
    }

    /**
     * @param baseDir The base directory of the file collection
     */
    public void setBaseDir( File baseDir )
    {
        this.baseDir = baseDir;
    }

    /**
     * @return Returns the file collections base directory.
     */
    public File getBaseDir()
    {
        return baseDir;
    }

    /**
     * @return Returns, whether symbolic links should be followed.
     * Defaults to true.
     */
    public boolean isFollowingSymLinks()
    {
        return isFollowingSymLinks;
    }

    /**
     * @param pIsFollowingSymLinks whether symbolic links should be followed
     */
    @SuppressWarnings({ "UnusedDeclaration" })
    public void setFollowingSymLinks( boolean pIsFollowingSymLinks )
    {
        isFollowingSymLinks = pIsFollowingSymLinks;
    }

    public void setDefaultAttributes( final int uid, final String userName, final int gid, final String groupName,
                                      final int fileMode, final int dirMode )
    {
        setDefaultFileAttributes( createDefaults(uid, userName, gid, groupName, fileMode) );

        setDefaultDirAttributes( createDefaults(uid, userName, gid, groupName, dirMode) );
    }

    public void setOverrideAttributes( final int uid, final String userName, final int gid, final String groupName,
                                       final int fileMode, final int dirMode )
    {
        setOverrideFileAttributes( createDefaults(uid, userName, gid, groupName, fileMode) );

        setOverrideDirAttributes( createDefaults(uid, userName, gid, groupName, dirMode) );
    }

    private static PlexusIoResourceAttributes createDefaults( final int uid, final String userName, final int gid,
                                                              final String groupName, final int mode )
    {
        return new SimpleResourceAttributes( uid, userName, gid, groupName, mode >= 0 ? mode : -1 );
    }


    @Override
    public void setPrefix(String prefix) {
        char nonSeparator = File.separatorChar == '/' ?'\\' : '/';
        super.setPrefix(StringUtils.replace( prefix, nonSeparator, File.separatorChar));
    }

    private void addResources( List<PlexusIoResource> result, String[] resources,
                               Map<String, PlexusIoResourceAttributes> attributesByPath )
        throws IOException
    {

        final File dir = getBaseDir();
        for ( String name : resources )
        {
            String sourceDir = name.replace( '\\', '/' );

            File f = new File( dir, sourceDir );

            PlexusIoResourceAttributes attrs = attributesByPath.get( name.length() > 0 ? name : "." );
            if ( attrs == null )
            {
                attrs = attributesByPath.get( f.getAbsolutePath() );
            }
            if ( attrs == null )
            {
                attrs = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
            }

            if ( f.isDirectory() )
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( getOverrideDirAttributes(), attrs,
                                                                        getDefaultDirAttributes() );
            }
            else
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( getOverrideFileAttributes(), attrs,
                                                                        getDefaultFileAttributes() );
            }

            PlexusIoFileResource resource = PlexusIoFileResource.fileOnDisk(f, name, attrs);
            if ( isSelected( resource ) )
            {
                result.add( resource );
            }
        }
    }


    private void addResourcesJava7( List<PlexusIoResource> result, String[] resources )
        throws IOException
    {

        final File dir = getBaseDir();
        final HashMap<Integer, String> cache1 = new HashMap<Integer, String>();
        final HashMap<Integer, String> cache2 = new HashMap<Integer, String>();
        for ( String name : resources )
        {
            String sourceDir = name.replace( '\\', '/' );
            File f = new File( dir, sourceDir );

            PlexusIoResourceAttributes attrs = new Java7FileAttributes( f, cache1, cache2 );
            if ( f.isDirectory() )
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( getOverrideDirAttributes(), attrs,
                                                                        getDefaultDirAttributes() );
            }
            else
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( getOverrideFileAttributes(), attrs,
                                                                        getDefaultFileAttributes() );
            }

            PlexusIoFileResource resource = PlexusIoFileResource.fileOnDisk(f, name, attrs);
            if ( isSelected( resource ) )
            {
                result.add( resource );
            }
        }
    }

    public Iterator<PlexusIoResource> getResources()
        throws IOException
    {
        final DirectoryScanner ds = new DirectoryScanner();
        final File dir = getBaseDir();
        ds.setBasedir( dir );
        final String[] inc = getIncludes();
        if ( inc != null && inc.length > 0 )
        {
            ds.setIncludes( inc );
        }
        final String[] exc = getExcludes();
        if ( exc != null && exc.length > 0 )
        {
            ds.setExcludes( exc );
        }
        if ( isUsingDefaultExcludes() )
        {
            ds.addDefaultExcludes();
        }
        ds.setCaseSensitive( isCaseSensitive() );
        ds.setFollowSymlinks( isFollowingSymLinks() );
        ds.scan();

        if ( Java7Reflector.isAtLeastJava7() )
        {
            final List<PlexusIoResource> result = new ArrayList<PlexusIoResource>();
            if ( isIncludingEmptyDirectories() )
            {
                String[] dirs = ds.getIncludedDirectories();
                addResourcesJava7( result, dirs );
            }

            String[] files = ds.getIncludedFiles();
            addResourcesJava7( result, files );
            return result.iterator();
        }
        else
        {
            Map<String, PlexusIoResourceAttributes> attributesByPath =
                PlexusIoResourceAttributeUtils.getFileAttributesByPath( getBaseDir() );

            final List<PlexusIoResource> result = new ArrayList<PlexusIoResource>();
            if ( isIncludingEmptyDirectories() )
            {
                String[] dirs = ds.getIncludedDirectories();
                addResources( result, dirs, attributesByPath );
            }

            String[] files = ds.getIncludedFiles();
            addResources( result, files, attributesByPath );
            return result.iterator();
        }
    }
}
