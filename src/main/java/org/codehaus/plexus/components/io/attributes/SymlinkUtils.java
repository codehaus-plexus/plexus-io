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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kristian Rosenvold
 */
public class SymlinkUtils {
    /**
     * Reads the target of the symbolic link
     *
     * @param symlink A file that is a symlink
     * @return A file that is the target of the symlink
     * @throws java.io.IOException
     */
    public static @NotNull File readSymbolicLink(@NotNull File symlink) throws IOException {
        return Files.readSymbolicLink(symlink.toPath()).toFile();
    }

    public static @NotNull File createSymbolicLink(@NotNull File symlink, File target) throws IOException {
        Path link = symlink.toPath();
        if (!Files.exists(link, LinkOption.NOFOLLOW_LINKS)) {
            link = Files.createSymbolicLink(link, target.toPath());
        }
        return link.toFile();
    }
}
