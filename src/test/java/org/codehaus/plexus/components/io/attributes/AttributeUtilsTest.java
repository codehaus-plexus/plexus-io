package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.codehaus.plexus.util.Os;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
public class AttributeUtilsTest {
    @Test
    public void testMiscPatterns() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0124);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_READ));
    }

    @Test
    public void testMorePatterns() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0241);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_READ));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_EXECUTE));
    }

    @Test
    public void testEvenMorePatterns() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0412);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_READ));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_WRITE));
    }

    @Test
    public void test777() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0777);
        assertTrue(permissions.size() == 9);
    }

    @Test
    public void testChmodBackAndForth() throws IOException {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) return;
        final File bxx = File.createTempFile("bxx", "ff");
        AttributeUtils.chmod(bxx, 0422);
        PlexusIoResourceAttributes firstAttrs = new FileAttributes(bxx);
        assertTrue(firstAttrs.isOwnerReadable());
        assertFalse(firstAttrs.isOwnerWritable());
        assertFalse(firstAttrs.isOwnerExecutable());
        AttributeUtils.chmod(bxx, 0777);
        PlexusIoResourceAttributes secondAttrs = new FileAttributes(bxx);
        assertTrue(secondAttrs.isOwnerReadable());
        assertTrue(secondAttrs.isOwnerWritable());
        assertTrue(secondAttrs.isOwnerExecutable());
    }
}
