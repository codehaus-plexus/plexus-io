package org.codehaus.plexus.components.io.resources;

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
    
    private FileAttributes overrideFileAttributes;
    
    private FileAttributes overrideDirAttributes;

    public PlexusIoFileResourceCollection()
    {
    }
    
    public PlexusIoFileResourceCollection( Logger logger )
    {
        super( logger );
    }
    
    public void setOverrideAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode )
    {
        overrideFileAttributes = new FileAttributes( gid, groupName, uid, userName, new char[]{} );
        overrideFileAttributes.setOctalMode( fileMode );
        
        overrideDirAttributes = new FileAttributes( gid, groupName, uid, userName, new char[]{} );
        overrideDirAttributes.setOctalMode( dirMode );
    }
    
    /**
     * Sets the file collections base directory.
     */
    public void setBaseDir( File baseDir )
    {
        this.baseDir = baseDir;
    }

    /**
     * Returns the file collections base directory.
     */
    public File getBaseDir()
    {
        return baseDir;
    }

    /**
     * Returns, whether symbolic links should be followed.
     * Defaults to true.
     */
    public boolean isFollowingSymLinks()
    {
        return isFollowingSymLinks;
    }

    /**
     * Returns, whether symbolic links should be followed.
     * Defaults to true.
     */
    public void setFollowingSymLinks( boolean pIsFollowingSymLinks )
    {
        isFollowingSymLinks = pIsFollowingSymLinks;
    }

    private void addResources( List list, String[] resources, Map attributesByPath ) throws IOException
    {
        final File dir = getBaseDir();
        for ( int i = 0; i < resources.length; i++ )
        {
            String name = resources[i];
            String sourceDir = name.replace( '\\', '/' );
            
            File f = new File( dir, sourceDir );
            PlexusIoResourceAttributes attrs = (PlexusIoResourceAttributes) attributesByPath.get( f.getAbsolutePath() );
            if ( f.isDirectory() )
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideDirAttributes, attrs );
            }
            else
            {
                attrs = PlexusIoResourceAttributeUtils.mergeAttributes( overrideFileAttributes, attrs );
            }
            
            PlexusIoFileResource resource = new PlexusIoFileResource( f, name, attrs );
            if ( isSelected( resource ) )
            {
                list.add( resource );
            }
        }
    }

    public Iterator getResources() throws IOException
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

        Map attributesByPath = PlexusIoResourceAttributeUtils.getFileAttributesByPath( getBaseDir() );
        
        final List result = new ArrayList();
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
