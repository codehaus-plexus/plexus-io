package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.codehaus.plexus.util.Os;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kristian Rosenvold
 */
@SuppressWarnings("OctalInteger")
public class AttributeUtilsTest {
    @Test
    void testMiscPatterns() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0124);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_READ));
    }

    @Test
    void testMorePatterns() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0241);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_READ));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_EXECUTE));
    }

    @Test
    void testEvenMorePatterns() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0412);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_READ));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_WRITE));
    }

    @Test
    void test777() throws Exception {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0777);
        assertEquals(9, permissions.size());
    }

    @Test
    void testChmodBackAndForth() throws IOException {
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
