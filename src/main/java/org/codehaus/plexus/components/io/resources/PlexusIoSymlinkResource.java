package org.codehaus.plexus.components.io.resources;

import javax.annotation.Nonnull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.output.DeferredFileOutputStream;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;

public class PlexusIoSymlinkResource extends PlexusIoFileResource implements SymlinkDestinationSupplier {
    private final String symLinkDestination;
    private final PlexusIoFileResource targetResource;

    PlexusIoSymlinkResource(@Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs)
            throws IOException {
        this(symlinkfile, name, attrs, symlinkfile.toPath());
    }

    PlexusIoSymlinkResource(
            @Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs, Path linkPath)
            throws IOException {
        this(symlinkfile, name, attrs, linkPath, java.nio.file.Files.readSymbolicLink(linkPath));
    }

    private PlexusIoSymlinkResource(
            @Nonnull File symlinkfile, String name, @Nonnull PlexusIoResourceAttributes attrs, Path path, Path linkPath)
            throws IOException {
        this(symlinkfile, name, attrs, linkPath.toString(), (PlexusIoFileResource)
                ResourceFactory.createResource(path.resolveSibling(linkPath).toFile()));
    }

    private PlexusIoSymlinkResource(
            @Nonnull File symlinkfile,
            String name,
            @Nonnull PlexusIoResourceAttributes attrs,
            String symLinkDestination,
            PlexusIoFileResource targetResource)
            throws IOException {
        super(symlinkfile, name, attrs, targetResource.getFileAttributes(), null, null);
        this.symLinkDestination = symLinkDestination;
        this.targetResource = targetResource;
    }

    public String getSymlinkDestination() throws IOException {
        return symLinkDestination;
    }

    public PlexusIoResource getTarget() {
        return targetResource;
    }

    public PlexusIoResource getLink() throws IOException {
        return new PlexusIoFileResource(getFile(), getName(), getAttributes());
    }

    @Override
    public long getSize() {
        DeferredFileOutputStream dfos = getDfos();
        if (dfos == null) {
            return targetResource.getSize();
        } else if (dfos.isInMemory()) {
            return dfos.getByteCount();
        } else {
            return dfos.getFile().length();
        }
    }

    @Override
    public boolean isDirectory() {
        return targetResource.isDirectory();
    }

    @Override
    public boolean isExisting() {
        return targetResource.isExisting();
    }

    @Override
    public boolean isFile() {
        return targetResource.isFile();
    }

    @Override
    public long getLastModified() {
        return targetResource.getLastModified();
    }

    @Nonnull
    @Override
    public PlexusIoResourceAttributes getAttributes() {
        return super.getAttributes();
    }
}
