package org.codehaus.plexus.components.io.resources;

/*
 * Copyright 2007 The Codehaus Foundation.
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlexusIoFileResourceCollectionTest {
    private static final File SYMLINKS = new File("src/test/resources/symlinks/src");

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void followedSymlinkToFileIsCollectedAsFile() throws Exception {
        PlexusIoFileResourceCollection collection = collection(true, "symR");

        PlexusIoResource resource = single(collection);

        assertFalse(resource.isSymbolicLink(), "symR should not be reported as a symbolic link");
        assertInstanceOf(PlexusIoFileResource.class, resource);
        assertEquals("This file is a source. r r r filemode\n", contentOf(resource));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void unfollowedSymlinkToFileIsCollectedAsSymlink() throws Exception {
        PlexusIoFileResourceCollection collection = collection(false, "symR");

        PlexusIoResource resource = single(collection);

        assertTrue(resource.isSymbolicLink(), "symR should be reported as a symbolic link");
        assertInstanceOf(PlexusIoSymlinkResource.class, resource);
        assertEquals("fileR.txt", ((PlexusIoSymlinkResource) resource).getSymlinkDestination());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void followedSymlinkToDirectoryIsTraversed() throws Exception {
        PlexusIoFileResourceCollection collection = collection(true, "symDir/**");

        List<PlexusIoResource> resources = all(collection);

        assertEquals(asList("symDir", "symDir/targetFile.txt"), names(resources));
        for (PlexusIoResource resource : resources) {
            assertFalse(resource.isSymbolicLink(), resource.getName() + " should not be reported as a symbolic link");
        }
        assertInstanceOf(PlexusIoFileResource.class, resources.get(1));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void unfollowedSymlinkToDirectoryIsNotTraversed() throws Exception {
        PlexusIoFileResourceCollection collection = collection(false, "symDir/**");

        List<PlexusIoResource> resources = all(collection);

        assertEquals(asList("symDir"), names(resources));
        assertTrue(resources.get(0).isSymbolicLink());
    }

    /**
     * A dangling symbolic link has no attributes to follow. Collecting it must not fail the whole scan.
     */
    @Test
    @DisabledOnOs(OS.WINDOWS)
    void danglingSymlinkDoesNotBreakCollection(@TempDir Path tempDir) throws Exception {
        Files.write(tempDir.resolve("aRegularFile.txt"), "content\n".getBytes(UTF_8));
        Files.createSymbolicLink(tempDir.resolve("dangling"), Paths.get("noSuchFile.txt"));

        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(tempDir.toFile());
        collection.setFollowingSymLinks(true);

        List<String> names = new ArrayList<>();
        for (Iterator<PlexusIoResource> it = collection.getResources(); it.hasNext(); ) {
            names.add(it.next().getName());
        }

        assertTrue(names.contains("aRegularFile.txt"), "regular files must still be collected, got " + names);
    }

    private static PlexusIoFileResourceCollection collection(boolean followSymlinks, String include) {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(SYMLINKS);
        collection.setFollowingSymLinks(followSymlinks);
        collection.setIncludes(new String[] {include});
        return collection;
    }

    private static List<PlexusIoResource> all(PlexusIoFileResourceCollection collection) throws Exception {
        List<PlexusIoResource> resources = new ArrayList<>();
        for (Iterator<PlexusIoResource> it = collection.getResources(); it.hasNext(); ) {
            resources.add(it.next());
        }
        return resources;
    }

    private static PlexusIoResource single(PlexusIoFileResourceCollection collection) throws Exception {
        List<PlexusIoResource> resources = all(collection);
        assertEquals(1, resources.size(), "expected exactly one resource, got " + resources);
        return resources.get(0);
    }

    private static List<String> names(List<PlexusIoResource> resources) {
        List<String> names = new ArrayList<>();
        for (PlexusIoResource resource : resources) {
            names.add(resource.getName());
        }
        return names;
    }

    private static String contentOf(PlexusIoResource resource) throws Exception {
        try (InputStream in = resource.getContents()) {
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            return new String(out.toByteArray(), UTF_8);
        }
    }
}
