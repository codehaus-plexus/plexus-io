package org.codehaus.plexus.components.io.resources;

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

import javax.inject.Named;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.functions.PlexusIoResourceConsumer;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;

/**
 * Implementation of {@link PlexusIoResourceCollection} for the set
 * of files in a common directory.
 */
@Named(PlexusIoFileResourceCollection.ROLE_HINT)
public class PlexusIoFileResourceCollection extends AbstractPlexusIoResourceCollectionWithAttributes {
    /**
     * Role hint of this component
     */
    public static final String ROLE_HINT = "files";

    private File baseDir;

    private boolean isFollowingSymLinks = true;

    /**
     * @since 3.2.0
     */
    private Comparator<String> filenameComparator;

    public PlexusIoFileResourceCollection() {}

    public PlexusIoResource resolve(final PlexusIoResource resource) throws IOException {
        return resource;
    }

    @Override
    public InputStream getInputStream(PlexusIoResource resource) throws IOException {
        return resource.getContents();
    }

    @Override
    public String getName(PlexusIoResource resource) {
        return resource.getName();
    }

    /**
     * @param baseDir The base directory of the file collection
     */
    public void setBaseDir(java.nio.file.Path baseDir) {
        this.baseDir = baseDir.toFile();
    }

    /**
     * @param baseDir The base directory of the file collection
     * @deprecated Use {@link #setBaseDir(java.nio.file.Path)} instead
     */
    @Deprecated
    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * @return Returns the file collections base directory as a Path.
     */
    public java.nio.file.Path getBaseDirAsPath() {
        return baseDir != null ? baseDir.toPath() : null;
    }

    /**
     * @return Returns the file collections base directory.
     * @deprecated Use {@link #getBaseDirAsPath()} instead
     */
    @Deprecated
    public File getBaseDir() {
        return baseDir;
    }

    /**
     * @return Returns, whether symbolic links should be followed.
     * Defaults to true.
     */
    public boolean isFollowingSymLinks() {
        return isFollowingSymLinks;
    }

    /**
     * @param pIsFollowingSymLinks whether symbolic links should be followed
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setFollowingSymLinks(boolean pIsFollowingSymLinks) {
        isFollowingSymLinks = pIsFollowingSymLinks;
    }

    public void setDefaultAttributes(
            final int uid,
            final String userName,
            final int gid,
            final String groupName,
            final int fileMode,
            final int dirMode) {
        setDefaultFileAttributes(createDefaults(uid, userName, gid, groupName, fileMode));

        setDefaultDirAttributes(createDefaults(uid, userName, gid, groupName, dirMode));
    }

    public void setOverrideAttributes(
            final int uid,
            final String userName,
            final int gid,
            final String groupName,
            final int fileMode,
            final int dirMode) {
        setOverrideFileAttributes(createDefaults(uid, userName, gid, groupName, fileMode));

        setOverrideDirAttributes(createDefaults(uid, userName, gid, groupName, dirMode));
    }

    private static PlexusIoResourceAttributes createDefaults(
            final int uid, final String userName, final int gid, final String groupName, final int mode) {
        return new SimpleResourceAttributes(
                uid, userName, gid, groupName, mode >= 0 ? mode : PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE);
    }

    @Override
    public void setPrefix(String prefix) {
        char nonSeparator = File.separatorChar == '/' ? '\\' : '/';
        super.setPrefix(StringUtils.replace(prefix, nonSeparator, File.separatorChar));
    }

    private void addResources(List<PlexusIoResource> result, String[] resources) throws IOException {

        final File dir = getBaseDir();
        for (String name : resources) {
            String sourceDir = name.replace('\\', '/');
            File f = new File(dir, sourceDir);

            FileAttributes fattrs = new FileAttributes(f);
            PlexusIoResourceAttributes attrs = mergeAttributes(fattrs, fattrs.isDirectory());

            String remappedName = getName(name);

            PlexusIoResource resource =
                    ResourceFactory.createResource(f, remappedName, null, getStreamTransformer(), attrs);

            if (isSelected(resource)) {
                result.add(resource);
            }
        }
    }

    public Stream stream() {
        return new Stream() {
            public void forEach(PlexusIoResourceConsumer resourceConsumer) throws IOException {
                Iterator<PlexusIoResource> resources = getResources();
                while (resources.hasNext()) {
                    PlexusIoResource next = resources.next();
                    if (isSelected(next)) {
                        resourceConsumer.accept(next);
                    }
                }
                if (resources instanceof Closeable) {
                    ((Closeable) resources).close();
                }
            }

            public void forEach(ExecutorService es, final PlexusIoResourceConsumer resourceConsumer)
                    throws IOException {
                Iterator<PlexusIoResource> resources = getResources();
                while (resources.hasNext()) {
                    final PlexusIoResource next = resources.next();
                    Callable future = new Callable() {
                        public Object call() throws Exception {
                            resourceConsumer.accept(next);
                            return this;
                        }
                    };
                    es.submit(future);
                }
                if (resources instanceof Closeable) {
                    ((Closeable) resources).close();
                }
            }
        };
    }

    public Iterator<PlexusIoResource> getResources() throws IOException {
        final DirectoryScanner ds = new DirectoryScanner();
        final File dir = getBaseDir();
        ds.setBasedir(dir);
        final String[] inc = getIncludes();
        if (inc != null && inc.length > 0) {
            ds.setIncludes(inc);
        }
        final String[] exc = getExcludes();
        if (exc != null && exc.length > 0) {
            ds.setExcludes(exc);
        }
        if (isUsingDefaultExcludes()) {
            ds.addDefaultExcludes();
        }
        ds.setCaseSensitive(isCaseSensitive());
        ds.setFollowSymlinks(isFollowingSymLinks());
        ds.setFilenameComparator(filenameComparator);
        ds.scan();

        final List<PlexusIoResource> result = new ArrayList<>();
        if (isIncludingEmptyDirectories()) {
            String[] dirs = ds.getIncludedDirectories();
            addResources(result, dirs);
        }

        String[] files = ds.getIncludedFiles();
        addResources(result, files);
        return result.iterator();
    }

    public boolean isConcurrentAccessSupported() {
        return true;
    }

    /**
     * @since 3.2.0
     */
    public void setFilenameComparator(Comparator<String> filenameComparator) {
        this.filenameComparator = filenameComparator;
    }
}
