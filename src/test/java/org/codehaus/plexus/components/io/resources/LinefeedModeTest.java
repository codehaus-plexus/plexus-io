package org.codehaus.plexus.components.io.resources;

/*
 * Copyright 2025 The Codehaus Foundation.
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

/**
 * Tests for {@link LinefeedMode} enum
 */
class LinefeedModeTest {

    @Test
    void testEnumValues() {
        LinefeedMode[] modes = LinefeedMode.values();
        assertEquals(3, modes.length);
        assertEquals(LinefeedMode.dos, modes[0]);
        assertEquals(LinefeedMode.unix, modes[1]);
        assertEquals(LinefeedMode.preserve, modes[2]);
    }

    @Test
    void testValueOf() {
        assertEquals(LinefeedMode.dos, LinefeedMode.valueOf("dos"));
        assertEquals(LinefeedMode.unix, LinefeedMode.valueOf("unix"));
        assertEquals(LinefeedMode.preserve, LinefeedMode.valueOf("preserve"));
    }

    @Test
    void testEnumEquality() {
        assertSame(LinefeedMode.dos, LinefeedMode.valueOf("dos"));
        assertSame(LinefeedMode.unix, LinefeedMode.valueOf("unix"));
        assertSame(LinefeedMode.preserve, LinefeedMode.valueOf("preserve"));
    }
}
