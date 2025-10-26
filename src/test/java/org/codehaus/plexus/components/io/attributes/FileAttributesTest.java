package org.codehaus.plexus.components.io.attributes;

/*
 * Copyright 2011 The Codehaus Foundation.
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
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Kristian Rosenvold
 */
public class FileAttributesTest {
    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testGetPosixFileAttributes() throws Exception {
        File file = new File(".");
        PlexusIoResourceAttributes fa = new FileAttributes(file);
        assertNotNull(fa);
    }

    @Test
    void testFileAttributesHandlesIOException() throws IOException {
        // Test that FileAttributes can be constructed for a regular file
        // even if ownership information is not available (e.g., WSL2 mapped network drives)
        File tempFile = Files.createTempFile("plexus-io-test", ".tmp").toFile();
        try {
            // This should not throw even if ownership info is unavailable
            PlexusIoResourceAttributes fa = new FileAttributes(tempFile);
            assertNotNull(fa);
            // The attributes object should be usable even if userName/groupName are null
            assertNotNull(fa.toString());
        } finally {
            tempFile.delete();
        }
    }
}
