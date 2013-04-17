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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Implementation of {@link PlexusIoResourceCollection} for the set
 * of files in a common directory.
 */
public class PlexusIoFileResourceCollection 
    extends AbstractPlexusIoResourceCollection
    implements PlexusIOResourceCollectionWithAttributes
{
    /**
     * Role hint of this component
     */
    public static final String ROLE_HINT = "files";
    
    private File baseDir;

    private boolean isFollowingSymLinks = true;
    
    private FileAttributes defaultFileAttributes;
    
    private FileAttributes defaultDirAttributes;
    
    private FileAttributes overrideFileAttributes;
    
    private FileAttributes overrideDirAttributes;

    public PlexusIoFileResourceCollection()
    {
    }
    
    public PlexusIoFileResourceCollection( Logger logger )
    {
        super( logger );
    }
    
    public void setDefaultAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode )
    {
        defaultFileAttributes = new FileAttributes( uid, userName, gid, groupName, new char[]{} );
        if ( fileMode >= 0 )
        {
            defaultFileAttributes.setOctalMode( fileMode );
        }
        
        defaultDirAttributes = new FileAttributes( uid, userName, gid, groupName, new char[]{} );
        if ( dirMode >= 0 )
        {
            defaultDirAttributes.setOctalMode( dirMode );
        }
    }
    
    public void setOverrideAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode )
    {
        overrideFileAttributes = new FileAttributes( uid, userName, gid, groupName, new char[]{} );
        if ( fileMode >= 0 )
        {
            overrideFileAttributes.setOctalMode( fileMode );
        }
        
        overrideDirAttributes = new FileAttributes( uid, userName, gid, groupName, new char[]{} );
        if ( dirMode >= 0 )
        {
            overrideDirAttributes.setOctalMode( dirMode );
        }
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
    @SuppressWarnings( { "UnusedDeclaration" } )
    public void setFollowingSymLinks( boolean pIsFollowingSymLinks )
    {
        isFollowingSymLinks = pIsFollowingSymLinks;
    }

    private void addResources( List<PlexusIoResource> list, String[] resources, Map<String, PlexusIoResourceAttributes> attributesByPath ) throws IOException
    {
        final File dir = getBaseDir();
        for ( String name : resources )
        {
            String sourceDir = name.replace( '\\', '/' );

            File f = new File( dir, sourceDir );

            PlexusIoResourceAttributes attrs =
                (PlexusIoResourceAttributes) attributesByPath.get( name.length() > 0 ? name : "." );
            if ( attrs == null )
            {
                attrs = (PlexusIoResourceAttributes) attributesByPath.get( f.getAbsolutePath() );
            }

            if ( f.isDirectory() )
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideDirAttributes, attrs,
                                                                        defaultDirAttributes );
            }
            else
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideFileAttributes, attrs,
                                                                        defaultFileAttributes );
            }

            PlexusIoFileResource resource = new PlexusIoFileResource( f, name, attrs );
            if ( isSelected( resource ) )
            {
                list.add( resource );
            }
        }
    }

    public Iterator<PlexusIoResource> getResources() throws IOException
    {
        final DirectoryScanner ds = new DirectoryScanner();
        final File dir = getBaseDir();
        ds.setBasedir( dir );
        final String[] inc = getIncludes();
        if ( inc != null  &&  inc.length > 0 )
        {
            ds.setIncludes( inc );
        }
        final String[] exc = getExcludes();
        if ( exc != null  &&  exc.length > 0 )
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

        Map<String, PlexusIoResourceAttributes> attributesByPath = PlexusIoResourceAttributeUtils.getFileAttributesByPath( getBaseDir() );

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
