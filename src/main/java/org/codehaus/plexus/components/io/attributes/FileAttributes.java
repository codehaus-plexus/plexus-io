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
import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/*
 * File attributes
 * Immutable
 */
public class FileAttributes implements PlexusIoResourceAttributes {
    public static final LinkOption[] FOLLOW_LINK_OPTIONS = new LinkOption[] {};

    public static final LinkOption[] NOFOLLOW_LINK_OPTIONS = new LinkOption[] {LinkOption.NOFOLLOW_LINKS};

    @Nullable
    private final Integer groupId;

    @Nullable
    private final String groupName;

    @Nullable
    private final Integer userId;

    private final String userName;

    private final boolean symbolicLink;

    private final boolean regularFile;

    private final boolean directory;

    private final boolean other;

    private final int octalMode;

    private final Set<PosixFilePermission> permissions;

    private final long size;

    private final FileTime lastModifiedTime;

    private static final Map<FileSystem, Map<Integer, String>> UIDS_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private static final Map<FileSystem, Map<Integer, String>> GIDS_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * @deprecated use {@link #FileAttributes(File)} and remove the unused userCache and groupCache parameters
     */
    @Deprecated
    public FileAttributes(
            @Nonnull File file, @Nonnull Map<Integer, String> userCache, @Nonnull Map<Integer, String> groupCache)
            throws IOException {
        this(file);
    }

    public FileAttributes(@Nonnull File file) throws IOException {
        this(file.toPath(), false);
    }

    public FileAttributes(@Nonnull File file, boolean followLinks) throws IOException {
        this(file.toPath(), followLinks);
    }

    private static Map<Integer, String> getUserCache(FileSystem fs) {
        return UIDS_CACHE.computeIfAbsent(fs, f -> new ConcurrentHashMap<>());
    }

    private static Map<Integer, String> getGroupCache(FileSystem fs) {
        return GIDS_CACHE.computeIfAbsent(fs, f -> new ConcurrentHashMap<>());
    }

    public FileAttributes(@Nonnull Path path, boolean followLinks) throws IOException {
        LinkOption[] options = followLinks ? FOLLOW_LINK_OPTIONS : NOFOLLOW_LINK_OPTIONS;
        Set<String> views = path.getFileSystem().supportedFileAttributeViews();
        String names;
        if (views.contains("unix")) {
            names =
                    "unix:gid,uid,isSymbolicLink,isRegularFile,isDirectory,isOther,mode,permissions,size,lastModifiedTime";
        } else if (views.contains("posix")) {
            names = "posix:*";
        } else {
            names = "basic:*";
        }
        Map<String, Object> attrs = Files.readAttributes(path, names, options);
        this.groupId = (Integer) attrs.get("gid");
        if (attrs.containsKey("group")) {
            this.groupName = ((Principal) attrs.get("group")).getName();
        } else if (this.groupId != null) {
            Map<Integer, String> cache = getGroupCache(path.getFileSystem());
            String name = cache.get(this.groupId);
            if (name == null) {
                name = getPrincipalName(path, "unix:group");
                cache.put(this.groupId, name);
            }
            this.groupName = name;
        } else {
            this.groupName = null;
        }
        this.userId = (Integer) attrs.get("uid");
        if (attrs.containsKey("owner")) {
            this.userName = ((Principal) attrs.get("owner")).getName();
        } else if (this.userId != null) {
            Map<Integer, String> cache = getUserCache(path.getFileSystem());
            String name = cache.get(this.userId);
            if (name == null) {
                name = getPrincipalName(path, "unix:owner");
                cache.put(this.userId, name);
            }
            this.userName = name;
        } else if (views.contains("owner")) {
            this.userName = getPrincipalName(path, "owner:owner");
        } else {
            this.userName = null;
        }
        this.symbolicLink = (Boolean) attrs.get("isSymbolicLink");
        this.regularFile = (Boolean) attrs.get("isRegularFile");
        this.directory = (Boolean) attrs.get("isDirectory");
        this.other = (Boolean) attrs.get("isOther");
        this.octalMode = attrs.containsKey("mode")
                ? (Integer) attrs.get("mode") & 0xfff
                : PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE;
        //noinspection unchecked
        this.permissions = attrs.containsKey("permissions")
                ? (Set<PosixFilePermission>) attrs.get("permissions")
                : Collections.emptySet();
        this.size = (Long) attrs.get("size");
        this.lastModifiedTime = (FileTime) attrs.get("lastModifiedTime");
    }

    private static String getPrincipalName(Path path, String attribute) throws IOException {
        Object owner = Files.getAttribute(path, attribute, LinkOption.NOFOLLOW_LINKS);
        return ((Principal) owner).getName();
    }

    public FileAttributes(
            @Nullable Integer userId,
            String userName,
            @Nullable Integer groupId,
            @Nullable String groupName,
            int octalMode,
            boolean symbolicLink,
            boolean regularFile,
            boolean directory,
            boolean other,
            Set<PosixFilePermission> permissions,
            long size,
            FileTime lastModifiedTime) {
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.octalMode = octalMode;
        this.symbolicLink = symbolicLink;
        this.regularFile = regularFile;
        this.directory = directory;
        this.other = other;
        this.permissions = permissions;
        this.size = size;
        this.lastModifiedTime = lastModifiedTime;
    }

    public static @Nonnull PlexusIoResourceAttributes uncached(@Nonnull File file) throws IOException {
        return new FileAttributes(file);
    }

    @Nullable
    public Integer getGroupId() {

        return groupId;
    }

    public boolean hasGroupId() {
        return false;
    }

    public boolean hasUserId() {
        return false;
    }

    @Nullable
    public String getGroupName() {
        return groupName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isGroupExecutable() {
        return containsPermission(PosixFilePermission.GROUP_EXECUTE);
    }

    private boolean containsPermission(PosixFilePermission groupExecute) {
        return permissions.contains(groupExecute);
    }

    public boolean isGroupReadable() {
        return containsPermission(PosixFilePermission.GROUP_READ);
    }

    public boolean isGroupWritable() {
        return containsPermission(PosixFilePermission.GROUP_WRITE);
    }

    public boolean isOwnerExecutable() {
        return containsPermission(PosixFilePermission.OWNER_EXECUTE);
    }

    public boolean isOwnerReadable() {
        return containsPermission(PosixFilePermission.OWNER_READ);
    }

    public boolean isOwnerWritable() {
        return containsPermission(PosixFilePermission.OWNER_WRITE);
    }

    public boolean isWorldExecutable() {
        return containsPermission(PosixFilePermission.OTHERS_EXECUTE);
    }

    public boolean isWorldReadable() {
        return containsPermission(PosixFilePermission.OTHERS_READ);
    }

    public boolean isWorldWritable() {
        return containsPermission(PosixFilePermission.OTHERS_WRITE);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append("File Attributes:");
        sb.append(System.lineSeparator());
        sb.append("------------------------------");
        sb.append(System.lineSeparator());
        sb.append("user: ");
        sb.append(userName == null ? "" : userName);
        sb.append(System.lineSeparator());
        sb.append("group: ");
        sb.append(groupName == null ? "" : groupName);
        sb.append(System.lineSeparator());
        sb.append("uid: ");
        sb.append(hasUserId() ? Integer.toString(userId) : "");
        sb.append(System.lineSeparator());
        sb.append("gid: ");
        sb.append(hasGroupId() ? Integer.toString(groupId) : "");

        return sb.toString();
    }

    public int getOctalMode() {
        return octalMode;
    }

    public int calculatePosixOctalMode() {
        int result = 0;

        if (isOwnerReadable()) {
            result |= AttributeConstants.OCTAL_OWNER_READ;
        }

        if (isOwnerWritable()) {
            result |= AttributeConstants.OCTAL_OWNER_WRITE;
        }

        if (isOwnerExecutable()) {
            result |= AttributeConstants.OCTAL_OWNER_EXECUTE;
        }

        if (isGroupReadable()) {
            result |= AttributeConstants.OCTAL_GROUP_READ;
        }

        if (isGroupWritable()) {
            result |= AttributeConstants.OCTAL_GROUP_WRITE;
        }

        if (isGroupExecutable()) {
            result |= AttributeConstants.OCTAL_GROUP_EXECUTE;
        }

        if (isWorldReadable()) {
            result |= AttributeConstants.OCTAL_WORLD_READ;
        }

        if (isWorldWritable()) {
            result |= AttributeConstants.OCTAL_WORLD_WRITE;
        }

        if (isWorldExecutable()) {
            result |= AttributeConstants.OCTAL_WORLD_EXECUTE;
        }

        return result;
    }

    public String getOctalModeString() {
        return Integer.toString(getOctalMode(), 8);
    }

    public boolean isSymbolicLink() {
        return symbolicLink;
    }

    public boolean isRegularFile() {
        return regularFile;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isOther() {
        return other;
    }

    public long getSize() {
        return size;
    }

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    protected Set<PosixFilePermission> getPermissions() {
        return permissions;
    }
}
