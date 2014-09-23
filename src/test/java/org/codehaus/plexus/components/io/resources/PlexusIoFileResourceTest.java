package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.components.io.attributes.Java7AttributeUtils;
import org.codehaus.plexus.components.io.attributes.Java7FileAttributes;
import org.codehaus.plexus.components.io.attributes.Java7Reflector;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

import junit.framework.TestCase;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;

public class PlexusIoFileResourceTest extends TestCase {

    public void testRealSymlink()
        throws IOException
    {
        if (!Java7Reflector.isAtLeastJava7()) return;
        final File file = new File( "src/test/resources/symlinks/src/symDir" );
        PlexusIoResourceAttributes attrs = Java7FileAttributes.uncached( file );
        PlexusIoFileResource r = new PlexusIoFileResource( file,  attrs);
        assertTrue(r.isSymbolicLink());
        final File target = SymlinkUtils.readSymbolicLink( file );
        assertTrue(target.getName().endsWith( "targetDir" ));
    }
}