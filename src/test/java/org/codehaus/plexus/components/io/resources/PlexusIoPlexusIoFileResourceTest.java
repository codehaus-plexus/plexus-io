package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

import junit.framework.TestCase;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;
import org.codehaus.plexus.util.Os;

public class PlexusIoPlexusIoFileResourceTest
    extends TestCase {

    public void testRealSymlink()
        throws IOException
    {
        if ( Os.isFamily( Os.FAMILY_WINDOWS ) )
            return;
        final File file = new File( "src/test/resources/symlinks/src/symDir" );
        PlexusIoResourceAttributes attrs = FileAttributes.uncached( file );
        assertTrue( attrs.isSymbolicLink() );
        PlexusIoFileResource r = new PlexusIoFileResource( file, "symDir", attrs );
        assertTrue( r.isSymbolicLink() );
        assertTrue( r.isDirectory() );
        final File target = SymlinkUtils.readSymbolicLink( file );
        assertTrue( target.getName().endsWith( "targetDir" ) );
    }
}