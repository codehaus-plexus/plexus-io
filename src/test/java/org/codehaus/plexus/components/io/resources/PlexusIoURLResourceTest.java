package org.codehaus.plexus.components.io.resources;

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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link PlexusIoURLResource}
 */
class PlexusIoURLResourceTest {

    @Test
    void testGetContents() throws Exception {
        // Create a test file
        File testFile = File.createTempFile("plexus-url-test", ".txt");
        try {
            java.nio.file.Files.write(testFile.toPath(), "test content".getBytes());

            // Create a concrete implementation for testing
            PlexusIoURLResource resource = new PlexusIoURLResource("test", 0, 12, true, false, true) {
                @Override
                public URL getURL() throws IOException {
                    return testFile.toURI().toURL();
                }
            };

            // Test getContents
            try (InputStream is = resource.getContents()) {
                assertNotNull(is);
                byte[] bytes = new byte[12];
                int read = is.read(bytes);
                assertEquals(12, read);
                assertEquals("test content", new String(bytes));
            }
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testGetDescriptionForError() {
        PlexusIoURLResource resource = new PlexusIoURLResource("test", 0, 0, true, false, true) {
            @Override
            public URL getURL() throws IOException {
                return new URL("http://example.com/test");
            }
        };

        try {
            String description = resource.getDescriptionForError(new URL("http://example.com/test"));
            assertEquals("http://example.com/test", description);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    void testGetDescriptionForErrorWithNull() {
        PlexusIoURLResource resource = new PlexusIoURLResource("test", 0, 0, true, false, true) {
            @Override
            public URL getURL() throws IOException {
                return null;
            }
        };

        String description = resource.getDescriptionForError(null);
        assertEquals("url=null", description);
    }

    @Test
    void testGetContentsWithIOException() throws Exception {
        PlexusIoURLResource resource = new PlexusIoURLResource("test", 0, 0, true, false, true) {
            @Override
            public URL getURL() throws IOException {
                return new URL("http://invalid-url-that-does-not-exist.invalid/file");
            }
        };

        // Expect IOException to be thrown with proper description
        IOException exception = assertThrows(IOException.class, () -> {
            resource.getContents();
        });
        assertNotNull(exception.getMessage());
    }
}
