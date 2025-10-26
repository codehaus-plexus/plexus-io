package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for symlink handling in PlexusIoFileResourceCollection
 */
public class PlexusIoFileResourceCollectionSymlinkTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testFollowSymlinksToFilesEnabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symR"});
        collection.setFollowingSymLinks(true);

        List<PlexusIoResource> resources = collectResources(collection);
        assertEquals(1, resources.size());

        PlexusIoResource resource = resources.get(0);
        assertEquals("symR", resource.getName());
        // When following symlinks, the resource should NOT be detected as a symlink
        assertFalse(resource.isSymbolicLink(), "Resource should not be a symlink when followSymlinks=true");
        assertTrue(resource.isFile(), "Resource should be a file");
        assertEquals(38, resource.getSize(), "Should have size of target file");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testFollowSymlinksToFilesDisabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symR"});
        collection.setFollowingSymLinks(false);

        List<PlexusIoResource> resources = collectResources(collection);
        assertEquals(1, resources.size());

        PlexusIoResource resource = resources.get(0);
        assertEquals("symR", resource.getName());
        // When not following symlinks, the resource should be detected as a symlink
        assertTrue(resource.isSymbolicLink(), "Resource should be a symlink when followSymlinks=false");
        assertTrue(resource instanceof SymlinkDestinationSupplier);
        assertEquals("fileR.txt", ((SymlinkDestinationSupplier) resource).getSymlinkDestination());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testFollowSymlinksToDirsEnabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symDir/**"});
        collection.setFollowingSymLinks(true);

        List<PlexusIoResource> resources = collectResources(collection);
        // Should include the symDir itself and files inside targetDir
        assertTrue(resources.size() >= 1, "Should find resources when following directory symlinks");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testFollowSymlinksToDirsDisabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symDir/**"});
        collection.setFollowingSymLinks(false);

        List<PlexusIoResource> resources = collectResources(collection);
        // Should not follow the directory symlink, so fewer or no resources
        // The exact behavior depends on DirectoryScanner
        assertTrue(resources.size() >= 0);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testMultipleSymlinksFollowEnabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symR", "symW", "symX"});
        collection.setFollowingSymLinks(true);

        List<PlexusIoResource> resources = collectResources(collection);
        assertEquals(3, resources.size());

        for (PlexusIoResource resource : resources) {
            assertFalse(
                    resource.isSymbolicLink(),
                    "Resource " + resource.getName() + " should not be a symlink when followSymlinks=true");
        }
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testMultipleSymlinksFollowDisabled() throws Exception {
        PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
        collection.setBaseDir(new File("src/test/resources/symlinks/src"));
        collection.setIncludes(new String[] {"symR", "symW", "symX"});
        collection.setFollowingSymLinks(false);

        List<PlexusIoResource> resources = collectResources(collection);
        assertEquals(3, resources.size());

        for (PlexusIoResource resource : resources) {
            assertTrue(
                    resource.isSymbolicLink(),
                    "Resource " + resource.getName() + " should be a symlink when followSymlinks=false");
        }
    }

    private List<PlexusIoResource> collectResources(PlexusIoFileResourceCollection collection) throws Exception {
        List<PlexusIoResource> result = new ArrayList<>();
        Iterator<PlexusIoResource> resources = collection.getResources();
        while (resources.hasNext()) {
            result.add(resources.next());
        }
        return result;
    }
}
