package org.codehaus.plexus.components.io.attributes;

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

import javax.annotation.Nonnull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.FileUtils;

@SuppressWarnings({"NullableProblems"})
public final class PlexusIoResourceAttributeUtils {

    private PlexusIoResourceAttributeUtils() {}

    public static PlexusIoResourceAttributes mergeAttributes(
            PlexusIoResourceAttributes override, PlexusIoResourceAttributes base, PlexusIoResourceAttributes def) {
        if (override == null) {
            return base;
        }
        if (base == null) {
            return new SimpleResourceAttributes(
                    override.getUserId() != null && override.getUserId() != -1
                            ? override.getUserId()
                            : def != null && def.getUserId() != null && def.getUserId() != -1 ? def.getUserId() : null,
                    override.getUserName() != null ? override.getUserName() : def != null ? def.getUserName() : null,
                    override.getGroupId() != null && override.getGroupId() != -1
                            ? override.getGroupId()
                            : def != null && def.getGroupId() != null && def.getGroupId() != -1
                                    ? def.getGroupId()
                                    : null,
                    override.getGroupName() != null ? override.getGroupName() : def != null ? def.getGroupName() : null,
                    override.getOctalMode());
        } else {
            Integer uid = override.getUserId() != null && override.getUserId() != -1
                    ? override.getUserId()
                    : base.getUserId() != null && base.getUserId() != -1
                            ? base.getUserId()
                            : def.getUserId() != null && def.getUserId() != -1 ? def.getUserId() : null;
            String uname = override.getUserName() != null
                    ? override.getUserName()
                    : base.getUserName() != null ? base.getUserName() : def.getUserName();
            Integer gid = override.getGroupId() != null && override.getGroupId() != -1
                    ? override.getGroupId()
                    : base.getGroupId() != null && base.getGroupId() != -1
                            ? base.getGroupId()
                            : def.getGroupId() != null && def.getGroupId() != -1 ? def.getGroupId() : null;
            String gname = override.getGroupName() != null
                    ? override.getGroupName()
                    : base.getGroupName() != null ? base.getGroupName() : def.getGroupName();
            int mode = override.getOctalMode() > 0
                    ? override.getOctalMode()
                    : base.getOctalMode() >= 0 ? base.getOctalMode() : def.getOctalMode();
            if (base instanceof FileAttributes) {
                return new UserGroupModeFileAttributes(uid, uname, gid, gname, mode, (FileAttributes) base);
            } else {
                return new SimpleResourceAttributes(uid, uname, gid, gname, mode, base.isSymbolicLink());
            }
        }
    }

    public static boolean isGroupExecutableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_GROUP_EXECUTE);
    }

    public static boolean isGroupReadableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_GROUP_READ);
    }

    public static boolean isGroupWritableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_GROUP_WRITE);
    }

    public static boolean isOwnerExecutableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_OWNER_EXECUTE);
    }

    public static boolean isOwnerReadableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_OWNER_READ);
    }

    public static boolean isOwnerWritableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_OWNER_WRITE);
    }

    public static boolean isWorldExecutableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_WORLD_EXECUTE);
    }

    public static boolean isWorldReadableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_WORLD_READ);
    }

    public static boolean isWorldWritableInOctal(int mode) {
        return isOctalModeEnabled(mode, AttributeConstants.OCTAL_WORLD_WRITE);
    }

    public static boolean isOctalModeEnabled(int mode, int targetMode) {
        return (mode & targetMode) != 0;
    }

    public static PlexusIoResourceAttributes getFileAttributes(Path path) throws IOException {
        return getFileAttributes(path, false);
    }

    public static PlexusIoResourceAttributes getFileAttributes(Path path, boolean followLinks) throws IOException {
        return new FileAttributes(path, followLinks);
    }

    /**
     * @deprecated Use {@link #getFileAttributes(Path)} instead
     */
    @Deprecated
    public static PlexusIoResourceAttributes getFileAttributes(File file) throws IOException {
        return getFileAttributes(file.toPath(), false);
    }

    /**
     * @deprecated Use {@link #getFileAttributes(Path, boolean)} instead
     */
    @Deprecated
    public static PlexusIoResourceAttributes getFileAttributes(File file, boolean followLinks) throws IOException {
        return getFileAttributes(file.toPath(), followLinks);
    }

    public static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(Path dir)
            throws IOException {
        return getFileAttributesByPath(dir, true);
    }

    public static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(
            @Nonnull Path dir, boolean recursive) throws IOException {
        return getFileAttributesByPath(dir, recursive, false);
    }

    public static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(
            @Nonnull Path dir, boolean recursive, boolean followLinks) throws IOException {
        final List<String> fileAndDirectoryNames;
        File dirAsFile = dir.toFile();
        if (recursive && java.nio.file.Files.isDirectory(dir)) {
            fileAndDirectoryNames = FileUtils.getFileAndDirectoryNames(dirAsFile, null, null, true, true, true, true);
        } else {
            fileAndDirectoryNames = Collections.singletonList(dirAsFile.getAbsolutePath());
        }

        final Map<String, PlexusIoResourceAttributes> attributesByPath = new LinkedHashMap<>();

        for (String fileAndDirectoryName : fileAndDirectoryNames) {
            attributesByPath.put(fileAndDirectoryName, new FileAttributes(new File(fileAndDirectoryName), followLinks));
        }
        return attributesByPath;
    }

    /**
     * @deprecated Use {@link #getFileAttributesByPath(Path)} instead
     */
    @Deprecated
    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(File dir) throws IOException {
        return getFileAttributesByPath(dir.toPath(), true);
    }

    /**
     * @deprecated Use {@link #getFileAttributesByPath(Path, boolean)} instead
     */
    @Deprecated
    public static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(
            @Nonnull File dir, boolean recursive) throws IOException {
        return getFileAttributesByPath(dir.toPath(), recursive, false);
    }

    /**
     * @deprecated Use {@link #getFileAttributesByPath(Path, boolean, boolean)} instead
     */
    @Deprecated
    public static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPath(
            @Nonnull File dir, boolean recursive, boolean followLinks) throws IOException {
        return getFileAttributesByPath(dir.toPath(), recursive, followLinks);
    }
}
