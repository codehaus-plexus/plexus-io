package org.codehaus.plexus.components.io.fileselectors;

import java.io.IOException;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;


/**
 * The default file selector: Selects all files.
 */
public class AllFilesFileSelector implements FileSelector
{
    /**
     * The all files selectors role-hint: "all".
     */
    public static final String ROLE_HINT = "all";

    public boolean isSelected( PlexusIoResource plexusIoResource ) throws IOException
    {
        return true;
    }
}
