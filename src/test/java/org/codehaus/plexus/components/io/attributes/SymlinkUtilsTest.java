/*
 * Copyright 2016 Plexus developers.
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

package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SymlinkUtilsTest {
    File target = new File("target/symlinkCapabilities");

    String expected = "This is a filed that we'll be symlinking to\n";

    @BeforeEach
    public void setup() throws IOException {
        FileUtils.deleteDirectory(target);
        Files.createDirectories(target.toPath());
    }

    @Test
    void testName() throws Exception {}

    @Test
    void create_read_symbolic_link_to_file() throws Exception {
        File symlink = new File(target, "symlinkToTarget");
        File relativePath = createTargetFile(target);
        SymlinkUtils.createSymbolicLink(symlink, relativePath);
        assertEquals(expected, FileUtils.readFileToString(symlink));
        assertEquals(new File("actualFile"), SymlinkUtils.readSymbolicLink(new File(target, "symlinkToTarget")));
    }

    @Test
    void create_read_symbolic_link_to_directory() throws Exception {
        File subDir = new File(target, "aSubDir");
        createTargetFile(subDir);
        File symlink = new File(target, "symlinkToDir");
        SymlinkUtils.createSymbolicLink(symlink, new File("aSubDir"));
        assertEquals(expected, FileUtils.readFileToString(new File(symlink, "actualFile")));
        assertEquals(new File("aSubDir"), SymlinkUtils.readSymbolicLink(new File(target, "symlinkToDir")));
    }

    private File createTargetFile(File target) throws IOException {
        File relativePath = new File("actualFile");
        File actualFile = new File(target, relativePath.getPath());
        FileUtils.write(actualFile, expected);
        return relativePath;
    }
}
