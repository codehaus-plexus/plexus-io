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

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractResourceAttributesTCK {

    protected AbstractResourceAttributesTCK() {}

    protected abstract PlexusIoResourceAttributes newAttributes(int mode);

    protected abstract PlexusIoResourceAttributes newAttributes(String mode);

    @Test
    final void testSetOctalModeString_OwnerModes() {
        verifyStringOctalModeSet("700", new boolean[] {true, true, true, false, false, false, false, false, false});
        verifyStringOctalModeSet("600", new boolean[] {true, true, false, false, false, false, false, false, false});
        verifyStringOctalModeSet("400", new boolean[] {true, false, false, false, false, false, false, false, false});
        verifyStringOctalModeSet("200", new boolean[] {false, true, false, false, false, false, false, false, false});
    }

    @Test
    final void testSetOctalModeString_GroupModes() {
        verifyStringOctalModeSet("070", new boolean[] {false, false, false, true, true, true, false, false, false});
        verifyStringOctalModeSet("060", new boolean[] {false, false, false, true, true, false, false, false, false});
        verifyStringOctalModeSet("040", new boolean[] {false, false, false, true, false, false, false, false, false});
        verifyStringOctalModeSet("020", new boolean[] {false, false, false, false, true, false, false, false, false});
    }

    @Test
    final void testSetOctalModeString_WorldModes() {
        verifyStringOctalModeSet("007", new boolean[] {false, false, false, false, false, false, true, true, true});
        verifyStringOctalModeSet("006", new boolean[] {false, false, false, false, false, false, true, true, false});
        verifyStringOctalModeSet("004", new boolean[] {false, false, false, false, false, false, true, false, false});
        verifyStringOctalModeSet("002", new boolean[] {false, false, false, false, false, false, false, true, false});
    }

    @Test
    final void testSetOctalMode_OwnerModes() {
        verifyOctalModeSet("700", new boolean[] {true, true, true, false, false, false, false, false, false});
        verifyOctalModeSet("600", new boolean[] {true, true, false, false, false, false, false, false, false});
        verifyOctalModeSet("400", new boolean[] {true, false, false, false, false, false, false, false, false});
        verifyOctalModeSet("200", new boolean[] {false, true, false, false, false, false, false, false, false});
    }

    @Test
    final void testSetOctalMode_GroupModes() {
        verifyOctalModeSet("070", new boolean[] {false, false, false, true, true, true, false, false, false});
        verifyOctalModeSet("060", new boolean[] {false, false, false, true, true, false, false, false, false});
        verifyOctalModeSet("040", new boolean[] {false, false, false, true, false, false, false, false, false});
        verifyOctalModeSet("020", new boolean[] {false, false, false, false, true, false, false, false, false});
    }

    @Test
    final void testSetOctalMode_WorldModes() {
        verifyOctalModeSet("007", new boolean[] {false, false, false, false, false, false, true, true, true});
        verifyOctalModeSet("006", new boolean[] {false, false, false, false, false, false, true, true, false});
        verifyOctalModeSet("004", new boolean[] {false, false, false, false, false, false, true, false, false});
        verifyOctalModeSet("002", new boolean[] {false, false, false, false, false, false, false, true, false});
    }

    private void verifyStringOctalModeSet(String mode, boolean[] checkValues) {
        PlexusIoResourceAttributes attrs = newAttributes(Integer.parseInt(mode, 8));

        assertEquals(checkValues[0], attrs.isOwnerReadable());
        assertEquals(checkValues[1], attrs.isOwnerWritable());
        assertEquals(checkValues[2], attrs.isOwnerExecutable());

        assertEquals(checkValues[3], attrs.isGroupReadable());
        assertEquals(checkValues[4], attrs.isGroupWritable());
        assertEquals(checkValues[5], attrs.isGroupExecutable());

        assertEquals(checkValues[6], attrs.isWorldReadable());
        assertEquals(checkValues[7], attrs.isWorldWritable());
        assertEquals(checkValues[8], attrs.isWorldExecutable());
    }

    private void verifyOctalModeSet(String mode, boolean[] checkValues) {
        PlexusIoResourceAttributes attrs = newAttributes(Integer.parseInt(mode, 8));

        assertEquals(checkValues[0], attrs.isOwnerReadable());
        assertEquals(checkValues[1], attrs.isOwnerWritable());
        assertEquals(checkValues[2], attrs.isOwnerExecutable());

        assertEquals(checkValues[3], attrs.isGroupReadable());
        assertEquals(checkValues[4], attrs.isGroupWritable());
        assertEquals(checkValues[5], attrs.isGroupExecutable());

        assertEquals(checkValues[6], attrs.isWorldReadable());
        assertEquals(checkValues[7], attrs.isWorldWritable());
        assertEquals(checkValues[8], attrs.isWorldExecutable());
    }
}
