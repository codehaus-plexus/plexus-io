package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Implementation of {@link PlexusIoResourceCollection} for the set
 * of files in a common directory.
 */
public class PlexusIoFileResourceCollection extends AbstractPlexusIoResourceCollection
{
    /**
     * Role hint of this component
     */
    public static final String ROLE_HINT = "files";
    
    private File baseDir;

    private boolean isFollowingSymLinks = true;

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

    private void addResources( List list, String[] resources ) throws IOException
    {
        String prefix = getPrefix();
        if ( prefix != null  &&  prefix.length() == 0 )
        {
            prefix = null;
        }

        final File dir = getBaseDir();
        for ( int i = 0; i < resources.length; i++ )
        {
            String name = resources[i];
            String sourceDir = name.replace( '\\', '/' );
            PlexusIoFileResource resource = new PlexusIoFileResource( new File( dir, sourceDir ), name );
            if ( isSelected( resource ) )
            {
                if ( prefix != null )
                {
                    resource.setName( prefix + name );
                }
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

        final List result = new ArrayList();
        if ( isIncludingEmptyDirectories() )
        {
            String[] dirs = ds.getIncludedDirectories();
            addResources( result, dirs );
        }

        String[] files = ds.getIncludedFiles();
        addResources( result, files );
        return result.iterator();
    }
}
