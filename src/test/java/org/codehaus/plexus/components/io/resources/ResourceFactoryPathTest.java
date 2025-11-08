package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceFactoryPathTest {

    @Test
    void testCreateResourceWithPath() throws IOException {
        Path tempPath = Files.createTempFile("test", ".txt");
        try {
            FileUtils.write(tempPath.toFile(), "test content", UTF_8);
            PlexusIoResource resource = ResourceFactory.createResource(tempPath);
            assertNotNull(resource);
            assertTrue(resource.isExisting());
        } finally {
            Files.deleteIfExists(tempPath);
        }
    }

    @Test
    void testCreateResourceWithPathAndName() throws IOException {
        Path tempPath = Files.createTempFile("test", ".txt");
        try {
            FileUtils.write(tempPath.toFile(), "test content", UTF_8);
            PlexusIoResource resource = ResourceFactory.createResource(tempPath, "custom-name.txt");
            assertNotNull(resource);
            assertTrue(resource.isExisting());
        } finally {
            Files.deleteIfExists(tempPath);
        }
    }
}
