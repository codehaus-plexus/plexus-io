package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;

/**
 * @author Kristian Rosenvold
 */
public interface PlexusIoSymlink
{
    public String getSymlinkDestination()
        throws IOException;
}
