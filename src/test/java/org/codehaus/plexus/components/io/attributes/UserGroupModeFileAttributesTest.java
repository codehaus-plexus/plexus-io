package org.codehaus.plexus.components.io.attributes;

/*
 * Copyright 2025 The Codehaus Foundation.
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link UserGroupModeFileAttributes}
 */
class UserGroupModeFileAttributesTest {

    @Test
    void testConstructorAndToString() throws Exception {
        // Create a base FileAttributes
        File tempFile = File.createTempFile("plexus-test", ".tmp");
        try {
            FileAttributes base = new FileAttributes(tempFile);

            // Create UserGroupModeFileAttributes with various values
            UserGroupModeFileAttributes attrs =
                    new UserGroupModeFileAttributes(1000, "testuser", 2000, "testgroup", 0644, base);

            // Verify the object is created
            assertNotNull(attrs);

            // Verify toString contains expected information
            String toString = attrs.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("testuser"));
            assertTrue(toString.contains("testgroup"));
            assertTrue(toString.contains("1000"));
            assertTrue(toString.contains("2000"));
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testWithNullUserAndGroup() throws Exception {
        File tempFile = File.createTempFile("plexus-test", ".tmp");
        try {
            FileAttributes base = new FileAttributes(tempFile);

            // Create with null user and group names
            UserGroupModeFileAttributes attrs = new UserGroupModeFileAttributes(null, null, null, null, 0755, base);

            // Verify toString handles nulls properly
            String toString = attrs.toString();
            assertNotNull(toString);
            // Should show empty strings for null names
            assertTrue(toString.contains("user:"));
            assertTrue(toString.contains("group:"));
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testInheritsFromBase() throws Exception {
        File tempFile = File.createTempFile("plexus-test", ".tmp");
        try {
            FileAttributes base = new FileAttributes(tempFile);

            UserGroupModeFileAttributes attrs =
                    new UserGroupModeFileAttributes(1000, "testuser", 2000, "testgroup", 0755, base);

            // Verify inherited properties
            assertEquals(base.isSymbolicLink(), attrs.isSymbolicLink());
            assertEquals(base.isRegularFile(), attrs.isRegularFile());
            assertEquals(base.isDirectory(), attrs.isDirectory());
        } finally {
            tempFile.delete();
        }
    }
}
