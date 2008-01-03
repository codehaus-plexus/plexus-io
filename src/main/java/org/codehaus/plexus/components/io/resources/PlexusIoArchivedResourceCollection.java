package org.codehaus.plexus.components.io.resources;

import java.io.File;


/**
 * Extension of {@link PlexusIoResourceCollection} for archive
 * files: Zip, tar, gzip, bzip2, etc. files.
 */
public interface PlexusIoArchivedResourceCollection
    extends PlexusIoResourceCollection
{
    /**
     * Sets the arcihve file
     */
    void setFile( File file );

    /**
     * Returns the archive file
     */
    File getFile();
}
