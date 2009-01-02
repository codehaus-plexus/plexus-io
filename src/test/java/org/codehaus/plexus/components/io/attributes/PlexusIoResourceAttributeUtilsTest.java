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