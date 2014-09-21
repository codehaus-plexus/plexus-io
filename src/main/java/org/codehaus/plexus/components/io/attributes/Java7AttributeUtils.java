package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Kristian Rosenvold
 */
public class Java7AttributeUtils
{
    /*
    Reads last-modified with proper failure handling if something goes wrong.
     */
    public static long getLastModified( File file ){
        try
        {
            BasicFileAttributes basicFileAttributes =
                Files.readAttributes( file.toPath(), BasicFileAttributes.class );
            return basicFileAttributes.lastModifiedTime().toMillis();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }

    }
}
