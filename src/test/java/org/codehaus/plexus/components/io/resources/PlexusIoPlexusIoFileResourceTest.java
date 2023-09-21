package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SymlinkUtils;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

public class PlexusIoPlexusIoFileResourceTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testRealSymlink() throws IOException {
        final File file = new File("src/test/resources/symlinks/src/symDir");
        PlexusIoResourceAttributes attrs = FileAttributes.uncached(file);
        assertTrue(attrs.isSymbolicLink());
        PlexusIoResource r = ResourceFactory.createResource(file);
        assertTrue(r.isSymbolicLink());
        assertTrue(r.isDirectory());
        final File target = SymlinkUtils.readSymbolicLink(file);
        assertTrue(target.getName().endsWith("targetDir"));
        assertTrue(r instanceof SymlinkDestinationSupplier);
        assertEquals("targetDir/", ((SymlinkDestinationSupplier) r).getSymlinkDestination());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testSymSymlinkFile() throws IOException {
        final File file = new File("src/test/resources/symlinks/src/symSymR");
        PlexusIoResource r = ResourceFactory.createResource(file);
        assertTrue(r.isSymbolicLink());
        assertEquals(38, r.getSize());
        PlexusIoResource rL = ((PlexusIoSymlinkResource) r).getLink();
        assertFalse(rL instanceof PlexusIoSymlinkResource);
        PlexusIoResource rT = ((PlexusIoSymlinkResource) r).getTarget();
        assertTrue(rT instanceof PlexusIoSymlinkResource);
        PlexusIoResource rTT = ((PlexusIoSymlinkResource) rT).getTarget();
        assertFalse(rTT instanceof PlexusIoSymlinkResource);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testSymlinkFile() throws IOException {
        final File file = new File("src/test/resources/symlinks/src/symR");
        PlexusIoResource r = ResourceFactory.createResource(file);
        assertTrue(r.isSymbolicLink());
        assertEquals(38, r.getSize());

        final File file2 = new File("src/test/resources/symlinks/src/symSymR");
        PlexusIoResource r2 = ResourceFactory.createResource(file2);
        assertTrue(r2.isSymbolicLink());
        assertEquals(38, r2.getSize());
        PlexusIoResource r3 = ((PlexusIoSymlinkResource) r2).getTarget();
    }
}
