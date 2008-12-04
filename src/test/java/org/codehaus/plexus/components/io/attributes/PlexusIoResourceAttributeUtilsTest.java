package org.codehaus.plexus.components.io.attributes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.Os;

import junit.framework.TestCase;

public class PlexusIoResourceAttributeUtilsTest
    extends TestCase
{

    public void testGetAttributesForThisTestClass()
        throws IOException
    {
        if ( Os.isFamily( Os.FAMILY_WINDOWS ) )
        {
            System.out.println( "WARNING: Unsupported OS, skipping test" );
            return;
        }

        URL resource =
            Thread.currentThread().getContextClassLoader().getResource(
                                                                        getClass().getName().replace( '.', '/' )
                                                                            + ".class" );
        
        if ( resource == null )
        {
            throw new IllegalStateException( "SOMETHING IS VERY WRONG. CANNOT FIND THIS TEST CLASS IN THE CLASSLOADER." );
        }
        
        File f = new File( resource.getPath().replaceAll( "%20", " " ) );
        
        Map attrs = PlexusIoResourceAttributeUtils.getFileAttributesByPath( f, new ConsoleLogger( Logger.LEVEL_INFO, "test" ), Logger.LEVEL_DEBUG );
        
        FileAttributes fileAttrs = (FileAttributes) attrs.get( f.getAbsolutePath() );
        
        System.out.println( "Got attributes for: " + f.getAbsolutePath() + fileAttrs );
        
        
        assertNotNull( fileAttrs );
        assertTrue( fileAttrs.isOwnerReadable() );
        assertEquals( System.getProperty( "user.name" ), fileAttrs.getUserName() );
    }

}
