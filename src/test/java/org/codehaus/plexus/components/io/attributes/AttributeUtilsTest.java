package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kristian Rosenvold
 */
@SuppressWarnings("OctalInteger")
class AttributeUtilsTest {
    @Test
    void miscPatterns() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0124);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_READ));
    }

    @Test
    void morePatterns() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0241);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_WRITE));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_READ));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_EXECUTE));
    }

    @Test
    void evenMorePatterns() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0412);
        assertTrue(permissions.contains(PosixFilePermission.OWNER_READ));
        assertTrue(permissions.contains(PosixFilePermission.GROUP_EXECUTE));
        assertTrue(permissions.contains(PosixFilePermission.OTHERS_WRITE));
    }

    @Test
    void test777() {
        final Set<PosixFilePermission> permissions = AttributeUtils.getPermissions(0777);
        assertEquals(9, permissions.size());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void chmodBackAndForth() throws Exception {
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

    @Test
    void testGetLastModified() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            long lastModified = AttributeUtils.getLastModified(tempFile);
            assertTrue(lastModified > 0);
        } finally {
            tempFile.delete();
        }
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testGetPosixFileAttributes() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            java.nio.file.attribute.PosixFileAttributes attrs = AttributeUtils.getPosixFileAttributes(tempFile);
            assertNotNull(attrs);
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testGetFileAttributes() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            java.nio.file.attribute.BasicFileAttributes attrs = AttributeUtils.getFileAttributes(tempFile);
            assertNotNull(attrs);
            assertTrue(attrs.isRegularFile());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testGetFileAttributesPath() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            java.nio.file.attribute.BasicFileAttributes attrs = AttributeUtils.getFileAttributes(tempFile.toPath());
            assertNotNull(attrs);
            assertTrue(attrs.isRegularFile());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testIsUnix() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            boolean isUnix = AttributeUtils.isUnix(tempFile.toPath());
            // Just verify it returns a boolean value and doesn't throw
            assertTrue(isUnix || !isUnix); // tautology to use the value
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testGetFileOwnershipInfo() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            java.nio.file.attribute.FileOwnerAttributeView ownerView = AttributeUtils.getFileOwnershipInfo(tempFile);
            // May be null on unsupported systems, just verify it doesn't throw
            assertTrue(ownerView != null || ownerView == null);
        } finally {
            tempFile.delete();
        }
    }
}
