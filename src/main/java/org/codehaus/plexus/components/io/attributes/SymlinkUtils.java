package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * @author Kristian Rosenvold
 */
public class SymlinkUtils
{
    /**
     * Reads the target of the symbolic link
     *
     * @param symlink A file that is a symlink
     * @return A file that is the target of the symlink
     * @throws java.io.IOException
     */

    public static File readSymbolicLink( File symlink )
        throws IOException
    {
        final java.nio.file.Path path = java.nio.file.Files.readSymbolicLink( symlink.toPath() );
        return path.toFile();
    }

    public static File createSymbolicLink( File symlink, File target )
        throws IOException
    {
        Path link = symlink.toPath();
        if (!Files.exists(link, LinkOption.NOFOLLOW_LINKS)){
            link = java.nio.file.Files.createSymbolicLink( link, target.toPath() );
        }
        return link.toFile();

    }
}
