package org.codehaus.plexus.components.io.fileselectors;

/*
 * Copyright 2007 The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    /**
     * Returns, whether the {@link FileInfo} refers to a symlink.
     * This method will return "false" for java versions prior to java7.
     */
    boolean isSymbolicLink();
}