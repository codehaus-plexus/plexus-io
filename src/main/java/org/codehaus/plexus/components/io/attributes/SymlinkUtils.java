/*
 * Copyright 2014 The Codehaus Foundation.
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
package org.codehaus.plexus.components.io.attributes;

import javax.annotation.Nonnull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * @author Kristian Rosenvold
 */
public class SymlinkUtils {
    /**
     * Reads the target of the symbolic link
     *
     * @param symlink A path that is a symlink
     * @return A path that is the target of the symlink
     * @throws java.io.IOException
     * @deprecated Use {@link Files#readSymbolicLink(Path)} directly
     */
    @Deprecated
    public static @Nonnull Path readSymbolicLink(@Nonnull Path symlink) throws IOException {
        return Files.readSymbolicLink(symlink);
    }

    /**
     * Reads the target of the symbolic link
     *
     * @param symlink A file that is a symlink
     * @return A file that is the target of the symlink
     * @throws java.io.IOException
     * @deprecated Use {@link Files#readSymbolicLink(Path)} directly
     */
    @Deprecated
    public static @Nonnull File readSymbolicLink(@Nonnull File symlink) throws IOException {
        return readSymbolicLink(symlink.toPath()).toFile();
    }

    /**
     * @deprecated Use {@link Files#createSymbolicLink(Path, Path, java.nio.file.attribute.FileAttribute[])} directly
     */
    @Deprecated
    public static @Nonnull Path createSymbolicLink(@Nonnull Path symlink, Path target) throws IOException {
        if (!Files.exists(symlink, LinkOption.NOFOLLOW_LINKS)) {
            return Files.createSymbolicLink(symlink, target);
        }
        return symlink;
    }

    /**
     * @deprecated Use {@link Files#createSymbolicLink(Path, Path, java.nio.file.attribute.FileAttribute[])} directly
     */
    @Deprecated
    public static @Nonnull File createSymbolicLink(@Nonnull File symlink, File target) throws IOException {
        return createSymbolicLink(symlink.toPath(), target.toPath()).toFile();
    }
}
