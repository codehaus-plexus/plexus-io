package org.codehaus.plexus.components.io.fileselectors;

import java.io.IOException;


/**
 * The default file selector: Selects all files.
 */
public class AllFilesFileSelector implements FileSelector
{
    /**
     * The all files selectors role-hint: "all".
     */
    public static final String ROLE_HINT = "all";

    public boolean isSelected( FileInfo fileInfo ) throws IOException
    {
        return true;
    }
}
