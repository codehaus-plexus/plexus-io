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
import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kristian Rosenvold
 */
@SuppressWarnings("OctalInteger")
public class AttributeUtils {
    /*
    Reads last-modified with proper failure handling if something goes wrong.
     */
    public static long getLastModified(@Nonnull File file) {
        try {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return basicFileAttributes.lastModifiedTime().toMillis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void chmod(@Nonnull File file, int mode) throws IOException {
        final Path path = file.toPath();
        if (!Files.isSymbolicLink(path)) {
            Files.setPosixFilePermissions(path, getPermissions(mode));
        }
    }

    @Nonnull
    public static Set<PosixFilePermission> getPermissions(int mode) {
        Set<PosixFilePermission> perms = new HashSet<>();
        // add owners permission
        if ((mode & 0400) > 0) {
            perms.add(PosixFilePermission.OWNER_READ);
        }
        if ((mode & 0200) > 0) {
            perms.add(PosixFilePermission.OWNER_WRITE);
        }
        if ((mode & 0100) > 0) {
            perms.add(PosixFilePermission.OWNER_EXECUTE);
        }
        // add group permissions
        if ((mode & 0040) > 0) {
            perms.add(PosixFilePermission.GROUP_READ);
        }
        if ((mode & 0020) > 0) {
            perms.add(PosixFilePermission.GROUP_WRITE);
        }
        if ((mode & 0010) > 0) {
            perms.add(PosixFilePermission.GROUP_EXECUTE);
        }
        // add others permissions
        if ((mode & 0004) > 0) {
            perms.add(PosixFilePermission.OTHERS_READ);
        }
        if ((mode & 0002) > 0) {
            perms.add(PosixFilePermission.OTHERS_WRITE);
        }
        if ((mode & 0001) > 0) {
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
        }
        return perms;
    }

    @Nonnull
    public static PosixFileAttributes getPosixFileAttributes(@Nonnull File file) throws IOException {
        return Files.readAttributes(file.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }

    @Nonnull
    public static BasicFileAttributes getFileAttributes(@Nonnull File file) throws IOException {
        return getFileAttributes(file.toPath());
    }

    public static BasicFileAttributes getFileAttributes(Path path) throws IOException {
        if (isUnix(path)) {
            try {
                return Files.readAttributes(path, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            } catch (UnsupportedOperationException ignore) {
                // Maybe ignoring is dramatic. Maybe not. But we do get the basic attrs anyway
            }
        }
        return Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isUnix(Path path) {
        return path.getFileSystem().supportedFileAttributeViews().contains("unix");
    }

    @Nullable
    public static FileOwnerAttributeView getFileOwnershipInfo(@Nonnull File file) throws IOException {
        try {
            return Files.getFileAttributeView(file.toPath(), FileOwnerAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        } catch (UnsupportedOperationException e) {
            return null;
        }
    }
}
