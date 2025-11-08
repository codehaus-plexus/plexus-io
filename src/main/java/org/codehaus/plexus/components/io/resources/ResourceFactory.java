package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;

import static org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils.getFileAttributes;
import static org.codehaus.plexus.components.io.resources.PlexusIoFileResource.getName;

/**
 * @author Kristian Rosenvold
 */
public class ResourceFactory {
    public static PlexusIoResource createResource(Path path) throws IOException {
        return createResource(path, getName(path.toFile()), null, null, getFileAttributes(path));
    }

    public static PlexusIoResource createResource(Path path, String name) throws IOException {
        return createResource(path, name, null, null, getFileAttributes(path));
    }

    public static PlexusIoResource createResource(
            Path path, String name, final ContentSupplier contentSupplier, PlexusIoResourceAttributes attributes)
            throws IOException {
        return createResource(path, name, contentSupplier, null, attributes);
    }

    public static PlexusIoResource createResource(Path path, InputStreamTransformer inputStreamTransformer)
            throws IOException {
        return createResource(path, getName(path.toFile()), null, inputStreamTransformer, getFileAttributes(path));
    }

    public static PlexusIoResource createResource(
            Path path,
            String name,
            final ContentSupplier contentSupplier,
            InputStreamTransformer inputStreamTransformer)
            throws IOException {
        return createResource(path, name, contentSupplier, inputStreamTransformer, getFileAttributes(path));
    }

    public static PlexusIoResource createResource(
            Path path,
            String name,
            final ContentSupplier contentSupplier,
            InputStreamTransformer inputStreamTransformer,
            PlexusIoResourceAttributes attributes)
            throws IOException {
        File f = path.toFile();
        boolean symbolicLink = attributes.isSymbolicLink();
        return symbolicLink
                ? new PlexusIoSymlinkResource(f, name, attributes)
                : new PlexusIoFileResource(
                        f, name, attributes, new FileAttributes(path, true), contentSupplier, inputStreamTransformer);
    }

    /**
     * @deprecated Use {@link #createResource(Path)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(File f) throws IOException {
        return createResource(f.toPath());
    }

    /**
     * @deprecated Use {@link #createResource(Path, String)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(File f, String name) throws IOException {
        return createResource(f.toPath(), name);
    }

    /**
     * @deprecated Use {@link #createResource(Path, String, ContentSupplier, PlexusIoResourceAttributes)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(
            File f, String name, final ContentSupplier contentSupplier, PlexusIoResourceAttributes attributes)
            throws IOException {
        return createResource(f.toPath(), name, contentSupplier, attributes);
    }

    /**
     * @deprecated Use {@link #createResource(Path, InputStreamTransformer)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(File f, InputStreamTransformer inputStreamTransformer)
            throws IOException {
        return createResource(f.toPath(), inputStreamTransformer);
    }

    /**
     * @deprecated Use {@link #createResource(Path, String, ContentSupplier, InputStreamTransformer)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(
            File f, String name, final ContentSupplier contentSupplier, InputStreamTransformer inputStreamTransformer)
            throws IOException {
        return createResource(f.toPath(), name, contentSupplier, inputStreamTransformer);
    }

    /**
     * @deprecated Use {@link #createResource(Path, String, ContentSupplier, InputStreamTransformer, PlexusIoResourceAttributes)} instead
     */
    @Deprecated
    public static PlexusIoResource createResource(
            File f,
            String name,
            final ContentSupplier contentSupplier,
            InputStreamTransformer inputStreamTransformer,
            PlexusIoResourceAttributes attributes)
            throws IOException {
        return createResource(f.toPath(), name, contentSupplier, inputStreamTransformer, attributes);
    }
}
