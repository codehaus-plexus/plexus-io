package org.codehaus.plexus.components.io.functions;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;

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
     * The close method will be delegated through the entire call chain
     *
     * @param resource The p-io resource the stream is for
     * @param inputStream The stream to transform
     * @return A transformed stream or possibly the supplied stream
     * @throws IOException
     */
	InputStream transform( PlexusIoResource resource, InputStream inputStream ) throws IOException;
}
