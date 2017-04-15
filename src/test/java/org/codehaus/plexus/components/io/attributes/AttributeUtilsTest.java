package org.codehaus.plexus.components.io.attributes;

import junit.framework.TestCase;
import org.codehaus.plexus.util.Os;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Kristian Rosenvold
 */
public class AttributeUtilsTest
    extends TestCase
{
    public void testMiscPatterns()
        throws Exception
    {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions( 0124 );
        assertTrue( permissions.contains( PosixFilePermission.OWNER_EXECUTE ) );
        assertTrue(permissions.contains( PosixFilePermission.GROUP_WRITE ));
        assertTrue(permissions.contains( PosixFilePermission.OTHERS_READ ));
    }

    public void testMorePatterns()
        throws Exception
    {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions( 0241 );
        assertTrue(permissions.contains( PosixFilePermission.OWNER_WRITE ));
        assertTrue(permissions.contains( PosixFilePermission.GROUP_READ ));
        assertTrue(permissions.contains( PosixFilePermission.OTHERS_EXECUTE ));
    }

    public void testEvenMorePatterns()
        throws Exception
    {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions( 0412 );
        assertTrue(permissions.contains( PosixFilePermission.OWNER_READ ));
        assertTrue(permissions.contains( PosixFilePermission.GROUP_EXECUTE ));
        assertTrue(permissions.contains( PosixFilePermission.OTHERS_WRITE ));
    }

    public void test777()
        throws Exception
    {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions( 0777 );
        assertTrue( permissions.size() == 9);
    }
      
    public void testChmodBackAndForth()
        throws IOException
    {
        if ( Os.isFamily( Os.FAMILY_WINDOWS )) return;
        final File bxx = File.createTempFile( "bxx", "ff" );
        AttributeUtils.chmod( bxx, 0422 );
        PlexusIoResourceAttributes firstAttrs = new FileAttributes( bxx, new HashMap<Integer, String>(  ), new HashMap<Integer, String>(  ) );
        assertTrue( firstAttrs.isOwnerReadable() );
        assertFalse( firstAttrs.isOwnerWritable() );
        assertFalse( firstAttrs.isOwnerExecutable() );
        AttributeUtils.chmod( bxx, 0777 );
        PlexusIoResourceAttributes secondAttrs = new FileAttributes( bxx, new HashMap<Integer, String>(  ), new HashMap<Integer, String>(  ) );
        assertTrue( secondAttrs.isOwnerReadable() );
        assertTrue( secondAttrs.isOwnerWritable() );
        assertTrue( secondAttrs.isOwnerExecutable() );
    }
}
