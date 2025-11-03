package org.codehaus.plexus.components.io.attributes;

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
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils.getFileAttributes;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OctalInteger")
class PlexusIoResourceAttributeUtilsTest {

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void getAttributesForThisTestClass() throws Exception {
        URL resource = Thread.currentThread()
                .getContextClassLoader()
                .getResource(getClass().getName().replace('.', '/') + ".class");

        if (resource == null) {
            throw new IllegalStateException("SOMETHING IS VERY WRONG. CANNOT FIND THIS TEST CLASS IN THE CLASSLOADER.");
        }

        File f = new File(resource.getPath().replaceAll("%20", " "));

        Map<String, PlexusIoResourceAttributes> attrs = PlexusIoResourceAttributeUtils.getFileAttributesByPath(f, true);

        PlexusIoResourceAttributes fileAttrs = attrs.get(f.getAbsolutePath());

        System.out.println("Got attributes for: " + f.getAbsolutePath() + fileAttrs);

        assertNotNull(fileAttrs);
        assertTrue(fileAttrs.isOwnerReadable());
        assertEquals(System.getProperty("user.name"), fileAttrs.getUserName());
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void directory() throws Exception {
        URL resource = Thread.currentThread()
                .getContextClassLoader()
                .getResource(getClass().getName().replace('.', '/') + ".class");

        if (resource == null) {
            throw new IllegalStateException("SOMETHING IS VERY WRONG. CANNOT FIND THIS TEST CLASS IN THE CLASSLOADER.");
        }

        File f = new File(resource.getPath().replaceAll("%20", " "));
        final File aDir = f.getParentFile().getParentFile().getParentFile();

        Commandline commandLine = new Commandline("chmod");
        commandLine.addArguments(new String[] {"763", f.getAbsolutePath()});

        CommandLineUtils.executeCommandLine(commandLine, null, null);
        Map<String, PlexusIoResourceAttributes> attrs =
                PlexusIoResourceAttributeUtils.getFileAttributesByPath(aDir, true);

        PlexusIoResourceAttributes fileAttrs = attrs.get(f.getAbsolutePath());

        assertTrue(fileAttrs.isGroupReadable());
        assertTrue(fileAttrs.isGroupWritable());
        assertFalse(fileAttrs.isGroupExecutable());

        assertTrue(fileAttrs.isOwnerExecutable());
        assertTrue(fileAttrs.isOwnerReadable());
        assertTrue(fileAttrs.isOwnerWritable());

        assertTrue(fileAttrs.isWorldExecutable());
        assertFalse(fileAttrs.isWorldReadable());
        assertTrue(fileAttrs.isWorldWritable());

        assertNotNull(fileAttrs);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void srcResource() throws Exception {
        File dir = new File("src/test/resources/symlinks");
        final Map<String, PlexusIoResourceAttributes> fileAttributesByPathScreenScrape =
                PlexusIoResourceAttributeUtils.getFileAttributesByPath(dir, true);
        assertNotNull(fileAttributesByPathScreenScrape);
        PlexusIoResourceAttributes pr = null;
        for (String s : fileAttributesByPathScreenScrape.keySet()) {
            if (s.endsWith("targetFile.txt")) pr = fileAttributesByPathScreenScrape.get(s);
        }
        assertNotNull(pr);

        assertTrue(pr.getOctalMode() > 0);
    }

    @Test
    void nonExistingDirectory() {
        File dir = new File("src/test/noSuchDirectory");
        assertThrows(
                NoSuchFileException.class, () -> PlexusIoResourceAttributeUtils.getFileAttributesByPath(dir, true));
    }

    @Test
    void mergeAttributesWithNullBase() {
        PlexusIoResourceAttributes override = new SimpleResourceAttributes(1001, "myUser", 1001, "test", 0);
        PlexusIoResourceAttributes defaults = new SimpleResourceAttributes(1000, "defaultUser", 1000, "defaultTest", 0);

        PlexusIoResourceAttributes attributes;
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, null, defaults);

        assertEquals(Integer.valueOf(1001), attributes.getGroupId());
        assertEquals(Integer.valueOf(1001), attributes.getUserId());
    }

    @Test
    void mergeAttributesWithNullOverrideGroup() {
        final PlexusIoResourceAttributes override = new SimpleResourceAttributes(1001, "myUser", -1, null, 0);
        final PlexusIoResourceAttributes defaults =
                new SimpleResourceAttributes(1000, "defaultUser", 1000, "defaultGroup", 0);

        PlexusIoResourceAttributes attributes =
                PlexusIoResourceAttributeUtils.mergeAttributes(override, null, defaults);

        assertEquals(attributes.getGroupId(), Integer.valueOf(1000));
        assertEquals(attributes.getUserId(), Integer.valueOf(1001));
    }

    @Test
    void mergeAttributesOverride() {
        final PlexusIoResourceAttributes blank = new SimpleResourceAttributes();
        final PlexusIoResourceAttributes invalid = new SimpleResourceAttributes(-1, null, -1, null, -1);
        final PlexusIoResourceAttributes override =
                new SimpleResourceAttributes(1111, "testUser", 2222, "testGroup", 0777);
        final PlexusIoResourceAttributes defaults =
                new SimpleResourceAttributes(3333, "defaultUser", 4444, "defaultGroup", 0444);
        final PlexusIoResourceAttributes base = new SimpleResourceAttributes(5555, "baseUser", 6666, "baseGroup", 0111);

        // When override is null, base is returned verbatim
        assertNull(PlexusIoResourceAttributeUtils.mergeAttributes(null, null, null));

        assertNull(PlexusIoResourceAttributeUtils.mergeAttributes(null, null, defaults));

        assertSame(base, PlexusIoResourceAttributeUtils.mergeAttributes(null, base, null));

        assertSame(base, PlexusIoResourceAttributeUtils.mergeAttributes(null, base, defaults));

        // Test cases when override is non-null
        PlexusIoResourceAttributes attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, null, null);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, base, null);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, null, defaults);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, base, defaults);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, blank, null);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(override, invalid, null);

        assertEquals(Integer.valueOf(1111), attributes.getUserId());
        assertEquals("testUser", attributes.getUserName());
        assertEquals(Integer.valueOf(2222), attributes.getGroupId());
        assertEquals("testGroup", attributes.getGroupName());
        assertEquals(0777, attributes.getOctalMode());

        // Test cases when override has only blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(blank, base, null);

        assertEquals(Integer.valueOf(5555), attributes.getUserId());
        assertEquals("baseUser", attributes.getUserName());
        assertEquals(Integer.valueOf(6666), attributes.getGroupId());
        assertEquals("baseGroup", attributes.getGroupName());
        assertEquals(0111, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(invalid, base, null);

        assertEquals(Integer.valueOf(5555), attributes.getUserId());
        assertEquals("baseUser", attributes.getUserName());
        assertEquals(Integer.valueOf(6666), attributes.getGroupId());
        assertEquals("baseGroup", attributes.getGroupName());
        assertEquals(0111, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(blank, base, defaults);

        assertEquals(Integer.valueOf(5555), attributes.getUserId());
        assertEquals("baseUser", attributes.getUserName());
        assertEquals(Integer.valueOf(6666), attributes.getGroupId());
        assertEquals("baseGroup", attributes.getGroupName());
        assertEquals(0111, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(invalid, base, defaults);

        assertEquals(Integer.valueOf(5555), attributes.getUserId());
        assertEquals("baseUser", attributes.getUserName());
        assertEquals(Integer.valueOf(6666), attributes.getGroupId());
        assertEquals("baseGroup", attributes.getGroupName());
        assertEquals(0111, attributes.getOctalMode());
    }

    @Test
    void fileAttributesGeneric() throws Exception {
        PlexusIoResourceAttributes attrs = getFileAttributes(new File("src/test/resources/symlinks/src/fileW.txt"));
        assertFalse(attrs.isSymbolicLink());
        assertTrue(StringUtils.isNotEmpty(attrs.getUserName()));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void fileAttributes() throws Exception {
        PlexusIoResourceAttributes attrs = getFileAttributes(new File("src/test/resources/symlinks/src/fileW.txt"));
        assertFalse(attrs.isSymbolicLink());
        assertTrue(StringUtils.isNotEmpty(attrs.getUserName()));
        assertTrue(StringUtils.isNotEmpty(attrs.getGroupName()));
        assertNotNull(attrs.getGroupId());
        assertNotNull(attrs.getUserId());
    }

    @Test
    void mergeAttributesDefault() {
        final PlexusIoResourceAttributes blank = new SimpleResourceAttributes(null, null, null, null, 0);
        final PlexusIoResourceAttributes invalid = new SimpleResourceAttributes(-1, null, -1, null, -1);
        final PlexusIoResourceAttributes defaults =
                new SimpleResourceAttributes(3333, "defaultUser", 4444, "defaultGroup", 0444);

        PlexusIoResourceAttributes attributes;

        // Test cases when override and base have blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(blank, blank, defaults);

        assertEquals(Integer.valueOf(3333), attributes.getUserId());
        assertEquals("defaultUser", attributes.getUserName());
        assertEquals(Integer.valueOf(4444), attributes.getGroupId());
        assertEquals("defaultGroup", attributes.getGroupName());
        // 0 is a borderline case, for backwards compatibility it is not overridden by value from defaults
        assertEquals(0, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(invalid, blank, defaults);

        assertEquals(Integer.valueOf(3333), attributes.getUserId());
        assertEquals("defaultUser", attributes.getUserName());
        assertEquals(Integer.valueOf(4444), attributes.getGroupId());
        assertEquals("defaultGroup", attributes.getGroupName());
        // 0 is a borderline case, for backwards compatibility it is not overridden by value from defaults
        // Not just that, but 0 is correct.
        assertEquals(0, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(blank, invalid, defaults);

        assertEquals(Integer.valueOf(3333), attributes.getUserId());
        assertEquals("defaultUser", attributes.getUserName());
        assertEquals(Integer.valueOf(4444), attributes.getGroupId());
        assertEquals("defaultGroup", attributes.getGroupName());
        assertEquals(0444, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(invalid, invalid, defaults);

        assertEquals(Integer.valueOf(3333), attributes.getUserId());
        assertEquals("defaultUser", attributes.getUserName());
        assertEquals(Integer.valueOf(4444), attributes.getGroupId());
        assertEquals("defaultGroup", attributes.getGroupName());
        assertEquals(0444, attributes.getOctalMode());

        // Test cases when invalid defaults should not override blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(blank, blank, invalid);

        assertNull(attributes.getUserId());
        assertNull(attributes.getUserName());
        assertNull(attributes.getGroupId());
        assertNull(attributes.getGroupName());
        assertEquals(0, attributes.getOctalMode());

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(invalid, blank, invalid);

        assertNull(attributes.getUserId());
        assertNull(attributes.getUserName());
        assertNull(attributes.getGroupId());
        assertNull(attributes.getGroupName());
        assertEquals(0, attributes.getOctalMode());
    }
}
