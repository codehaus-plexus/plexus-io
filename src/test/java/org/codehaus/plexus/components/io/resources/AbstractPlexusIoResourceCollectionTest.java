package org.codehaus.plexus.components.io.resources;

import javax.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import org.codehaus.plexus.components.io.functions.InputStreamTransformer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kristian Rosenvold
 */
class AbstractPlexusIoResourceCollectionTest {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void getIncludes() throws Exception {
        AbstractPlexusIoResourceCollection sut = new AbstractPlexusIoResourceCollection() {
            @Override
            public Iterator<PlexusIoResource> getResources() {
                return Arrays.asList(getResource("r1"), getResource("r2")).iterator();
            }

            @Override
            public Stream stream() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isConcurrentAccessSupported() {
                return true;
            }
        };

        sut.setStreamTransformer(new InputStreamTransformer() {
            @Nonnull
            @Override
            public InputStream transform(@Nonnull PlexusIoResource resource, @Nonnull final InputStream inputStream)
                    throws IOException {
                final byte[] buf = new byte[2];
                buf[0] = (byte) inputStream.read();
                buf[1] = (byte) inputStream.read();
                return new ByteArrayInputStream(buf);
            }
        });

        final PlexusIoResource next = sut.getResources().next();
        final InputStream inputStream = sut.getInputStream(next);
        inputStream.read();
        inputStream.read();
        assertEquals(-1, inputStream.read());
        inputStream.close();
    }

    private static PlexusIoResource getResource(final String r1) {
        return new AbstractPlexusIoResource(r1, 0, 0, true, false, true) {
            @Nonnull
            @Override
            public InputStream getContents() {
                return new ByteArrayInputStream((r1 + "Payload").getBytes());
            }

            @Override
            public URL getURL() {
                throw new IllegalStateException("Not implemented");
            }
        };
    }
}
