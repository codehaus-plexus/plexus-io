package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PlexusIoFileResource with DeferredFileOutputStream scenarios.
 * This specifically tests the fix for issue #118 where temp files were deleted
 * before they could be accessed, causing FileNotFoundException.
 */
class PlexusIoFileResourceDeferredTest {

    @TempDir
    File tempDir;

    @Test
    void fileResourceWithTransformerCanReadContentsMultipleTimes() throws Exception {
        // Create a test file with content larger than typical buffer size
        File testFile = new File(tempDir, "test-file.txt");
        byte[] largeContent = new byte[10000]; // 10KB
        for (int i = 0; i < largeContent.length; i++) {
            largeContent[i] = (byte) ('A' + (i % 26));
        }
        Files.write(testFile.toPath(), largeContent);

        // Create a transformer that modifies the content
        InputStreamTransformer transformer = (resource, inputStream) -> {
            // Simple transformer that reads and returns the same content
            return inputStream;
        };

        // Create PlexusIoFileResource with transformer
        PlexusIoFileResource resource =
                new PlexusIoFileResource(testFile, testFile.getName(), new FileAttributes(testFile), null, transformer);

        // First read - this should work and not delete the temp file
        try (InputStream is1 = resource.getContents()) {
            byte[] read1 = IOUtils.toByteArray(is1);
            assertEquals(largeContent.length, read1.length);
        }

        // Second read - this should also work if the temp file wasn't prematurely deleted
        // This is the key test - without the fix, this would throw FileNotFoundException
        try (InputStream is2 = resource.getContents()) {
            byte[] read2 = IOUtils.toByteArray(is2);
            assertEquals(largeContent.length, read2.length);
        }
    }

    @Test
    void fileResourceWithTransformerLargeFile() throws Exception {
        // Create a large file that exceeds the DeferredFileOutputStream threshold (5MB)
        File testFile = new File(tempDir, "large-test-file.bin");
        byte[] chunk = new byte[1024 * 1024]; // 1MB chunks
        for (int i = 0; i < chunk.length; i++) {
            chunk[i] = (byte) i;
        }

        // Write 6MB to exceed the 5MB threshold
        try (OutputStream os = Files.newOutputStream(testFile.toPath())) {
            for (int i = 0; i < 6; i++) {
                os.write(chunk);
            }
        }

        InputStreamTransformer transformer = (resource, inputStream) -> inputStream;

        PlexusIoFileResource resource =
                new PlexusIoFileResource(testFile, testFile.getName(), new FileAttributes(testFile), null, transformer);

        // Verify we can read the content - this tests that the temp file
        // created by DeferredFileOutputStream is properly accessible
        long size = resource.getSize();
        assertTrue(size > 5_000_000, "File should be larger than 5MB threshold");

        try (InputStream is = resource.getContents()) {
            assertNotNull(is);
            byte[] firstBytes = new byte[1024];
            int read = is.read(firstBytes);
            assertEquals(1024, read);
        }
    }

    @Test
    void fileResourceWithTransformerSmallFile() throws Exception {
        // Test with a small file that stays in memory (below 5MB threshold)
        File testFile = new File(tempDir, "small-test-file.txt");
        String content = "Hello, World!";
        Files.write(testFile.toPath(), content.getBytes(StandardCharsets.UTF_8));

        InputStreamTransformer transformer = (resource, inputStream) -> inputStream;

        PlexusIoFileResource resource =
                new PlexusIoFileResource(testFile, testFile.getName(), new FileAttributes(testFile), null, transformer);

        // Multiple reads should work for small files too
        for (int i = 0; i < 3; i++) {
            try (InputStream is = resource.getContents()) {
                String readContent = IOUtils.toString(is, StandardCharsets.UTF_8);
                assertEquals(content, readContent);
            }
        }
    }
}
