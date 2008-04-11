package org.codehaus.plexus.components.io.fileselectors;

import java.io.IOException;


/**
 * Interface of a component, which selects/deselects files.
 */
public interface FileSelector
{
    /**
     * Role used to register component implementations with the container.
     */
    public static final String ROLE = FileSelector.class.getName();

    /**
     * The default role-hint: "default".
     */
    public static final String DEFAULT_ROLE_HINT = "default";

    /**
     * Returns, whether the given file is selected.
     * @param An instance of FileInfo with the files meta data.
     *   It is recommended, that the caller creates an instance
     *   of {@link PlexusIoResource}.
     */
    boolean isSelected(FileInfo fileInfo) throws IOException;
}
