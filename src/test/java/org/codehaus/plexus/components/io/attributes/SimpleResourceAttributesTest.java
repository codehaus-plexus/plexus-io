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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleResourceAttributesTest extends AbstractResourceAttributesTCK {

    protected PlexusIoResourceAttributes newAttributes(int mode) {
        final SimpleResourceAttributes simpleResourceAttributes = new SimpleResourceAttributes();
        simpleResourceAttributes.setOctalMode(mode);
        return simpleResourceAttributes;
    }

    protected PlexusIoResourceAttributes newAttributes(String mode) {
        final SimpleResourceAttributes simpleResourceAttributes = new SimpleResourceAttributes();
        simpleResourceAttributes.setOctalModeString(mode);
        return simpleResourceAttributes;
    }

    @Test
    void testConstructorWithAllParameters() {
        SimpleResourceAttributes attrs = new SimpleResourceAttributes(1000, "testuser", 2000, "testgroup", 0644);
        assertEquals(1000, attrs.getUserId());
        assertEquals("testuser", attrs.getUserName());
        assertEquals(2000, attrs.getGroupId());
        assertEquals("testgroup", attrs.getGroupName());
        assertEquals(0644, attrs.getOctalMode());
    }

    @Test
    void testConstructorWithSymbolicLink() {
        SimpleResourceAttributes attrs = new SimpleResourceAttributes(1000, "testuser", 2000, "testgroup", 0644, true);
        assertTrue(attrs.isSymbolicLink());
        assertEquals(1000, attrs.getUserId());
    }

    @Test
    void testLastResortDummyAttributes() {
        PlexusIoResourceAttributes attrs = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
        assertNotNull(attrs);
        assertEquals(PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE, attrs.getOctalMode());
    }

    @Test
    void testSettersAndGetters() {
        SimpleResourceAttributes attrs = new SimpleResourceAttributes();
        attrs.setUserId(500);
        attrs.setUserName("user");
        attrs.setGroupId(600);
        attrs.setGroupName("group");

        assertEquals(500, attrs.getUserId());
        assertEquals("user", attrs.getUserName());
        assertEquals(600, attrs.getGroupId());
        assertEquals("group", attrs.getGroupName());
    }

    @Test
    void testSetOctalModeString() {
        SimpleResourceAttributes attrs = new SimpleResourceAttributes();
        attrs.setOctalModeString("0755");
        assertEquals(0755, attrs.getOctalMode());
        assertEquals("755", attrs.getOctalModeString()); // Returns octal string without leading 0
    }
}
