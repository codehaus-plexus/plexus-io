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

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ResourceFactory}
 */
class ResourceFactoryTest {

    @Test
    void testCreateResourceWithFile() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            PlexusIoResource resource = ResourceFactory.createResource(tempFile);
            assertNotNull(resource);
            assertTrue(resource.isFile());
            assertFalse(resource.isDirectory());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testCreateResourceWithFileAndName() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            PlexusIoResource resource = ResourceFactory.createResource(tempFile, "custom-name.txt");
            assertNotNull(resource);
            assertEquals("custom-name.txt", resource.getName());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testCreateResourceWithContentSupplier() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            ContentSupplier contentSupplier = () -> new java.io.ByteArrayInputStream("test content".getBytes());
            PlexusIoResource resource = ResourceFactory.createResource(
                    tempFile, "test.txt", contentSupplier, PlexusIoResourceAttributeUtils.getFileAttributes(tempFile));
            assertNotNull(resource);
            assertEquals("test.txt", resource.getName());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testCreateResourceWithInputStreamTransformer() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            InputStreamTransformer transformer = (resource, inputStream) -> inputStream;
            PlexusIoResource resource = ResourceFactory.createResource(tempFile, transformer);
            assertNotNull(resource);
            assertTrue(resource.isFile());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testCreateResourceWithAllParameters() throws Exception {
        File tempFile = File.createTempFile("test", ".tmp");
        try {
            ContentSupplier contentSupplier = () -> new java.io.ByteArrayInputStream("test".getBytes());
            InputStreamTransformer transformer = (resource, inputStream) -> inputStream;
            PlexusIoResource resource =
                    ResourceFactory.createResource(tempFile, "test.txt", contentSupplier, transformer);
            assertNotNull(resource);
            assertEquals("test.txt", resource.getName());
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testCreateResourceWithDirectory() throws Exception {
        File tempDir = java.nio.file.Files.createTempDirectory("test").toFile();
        try {
            PlexusIoResource resource = ResourceFactory.createResource(tempDir);
            assertNotNull(resource);
            assertTrue(resource.isDirectory());
            assertFalse(resource.isFile());
        } finally {
            tempDir.delete();
        }
    }
}
