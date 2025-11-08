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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ClosingInputStream}
 */
class ClosingInputStreamTest {

    @Test
    void testRead() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        assertEquals('t', cis.read());
        assertEquals('e', cis.read());
        cis.close();
    }

    @Test
    void testReadByteArray() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        byte[] buffer = new byte[4];
        int read = cis.read(buffer);
        assertEquals(4, read);
        assertEquals("test", new String(buffer));
        cis.close();
    }

    @Test
    void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        byte[] buffer = new byte[10];
        int read = cis.read(buffer, 2, 4);
        assertEquals(4, read);
        assertEquals("test", new String(buffer, 2, 4));
        cis.close();
    }

    @Test
    void testSkip() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        long skipped = cis.skip(5);
        assertEquals(5, skipped);
        assertEquals('d', cis.read());
        cis.close();
    }

    @Test
    void testAvailable() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        assertEquals(9, cis.available());
        cis.read();
        assertEquals(8, cis.available());
        cis.close();
    }

    @Test
    void testClose() throws IOException {
        // Create test streams that track if they were closed
        final boolean[] targetClosed = {false};
        final boolean[] otherClosed = {false};

        InputStream target = new ByteArrayInputStream("test".getBytes()) {
            @Override
            public void close() throws IOException {
                targetClosed[0] = true;
                super.close();
            }
        };

        InputStream other = new ByteArrayInputStream(new byte[0]) {
            @Override
            public void close() throws IOException {
                otherClosed[0] = true;
                super.close();
            }
        };

        ClosingInputStream cis = new ClosingInputStream(target, other);
        cis.close();

        // Verify both streams were closed
        assertTrue(targetClosed[0], "Target stream should be closed");
        assertTrue(otherClosed[0], "Other stream should be closed");
    }

    @Test
    void testMarkAndReset() throws IOException {
        byte[] data = "test data".getBytes();
        InputStream target = new ByteArrayInputStream(data);
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        assertTrue(cis.markSupported());

        cis.mark(10);
        assertEquals('t', cis.read());
        assertEquals('e', cis.read());

        cis.reset();
        assertEquals('t', cis.read());
        cis.close();
    }

    @Test
    void testMarkSupported() {
        InputStream target = new ByteArrayInputStream("test".getBytes());
        InputStream other = new ByteArrayInputStream(new byte[0]);

        ClosingInputStream cis = new ClosingInputStream(target, other);
        assertTrue(cis.markSupported());
    }
}
