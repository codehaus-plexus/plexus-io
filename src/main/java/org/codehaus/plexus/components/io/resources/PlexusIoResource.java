package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.components.io.fileselectors.FileInfo;


/**
 * A resource is a file-like entity. It may be an actual file,
 * an URL, a zip entry, or something like that.
 */
public interface PlexusIoResource extends FileInfo
{
    /**
     * Unknown resource size.
     */
    public static final long UNKNOWN_RESOURCE_SIZE = -1;

    /**
     * Unknown modification date
     */
    public static final long UNKNOWN_MODIFICATION_DATE = 0;

    /**
     * Returns the date, when the resource was last modified, if known.
     * Otherwise, returns {@link #UNKNOWN_MODIFICATION_DATE}.
     * @see java.io.File#lastModified()
     */
    long getLastModified();

    /**
     * Returns, whether the resource exists.
     */
    boolean isExisting();

    /**
     * Returns the resources size, if known. Otherwise returns
     * {@link #UNKNOWN_RESOURCE_SIZE}.
     */
    long getSize();

    /**
     * Returns an {@link URL}, which may be used to reference the
     * resource, if possible.
     * @return An URL referencing the resource, if possible, or null.
     *   In the latter case, you are forced to use {@link #getInputStream()}.
     */
    URL getURL() throws IOException;
}
