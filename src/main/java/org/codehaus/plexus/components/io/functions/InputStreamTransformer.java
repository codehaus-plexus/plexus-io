package org.codehaus.plexus.components.io.functions;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import javax.annotation.WillClose;
import java.io.IOException;
import java.io.InputStream;

/**
 * Transform a stream into some other kind of stream. May be used to apply filtering or other
 * kinds of transformations.
 */
public interface InputStreamTransformer {
    /**
     * Transform the supplied input stream into another input stream.
     *
     * NOTE: It is the responsibility of this method to delegate the call to "close" onto the
     * supplied input stream.
     * @param resource The p-io resource the stream is for
     * @param inputStream The stream to transform
     * @return A transformed stream or possibly the supplied stream
     * @throws IOException
     */
	InputStream transform( PlexusIoResource resource, @WillClose InputStream inputStream ) throws IOException;
}
