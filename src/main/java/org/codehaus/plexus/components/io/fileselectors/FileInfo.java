package org.codehaus.plexus.components.io.fileselectors;

import java.io.IOException;
import java.io.InputStream;

/**
 * An object implementing this interface is passed to the
 * file selector when the method
 * {@link FileSelector#isSelected(FileInfo)}
 * is invoked. This object provides information about
 * the file to select or deselect.
 */
public interface FileInfo
{
    /**
     * Returns the resources name, which may include path components,
     * like directory names, or something like that. The resources name
     * is expected to be a relative name and the path components must
     * be separated by {@link java.io.File#pathSeparator}
     */
    String getName();

    /**
     * Creates an {@link InputStream}, which may be used to read
     * the files contents. This is useful, if the file selector
     * comes to a decision based on the files contents.
     */
    InputStream getContents() throws IOException;

    /**
     * Returns, whether the {@link FileInfo} refers to a file.
     */
    boolean isFile();

    /**
     * Returns, whether the {@link FileInfo} refers to a directory.
     */
    boolean isDirectory();
}
