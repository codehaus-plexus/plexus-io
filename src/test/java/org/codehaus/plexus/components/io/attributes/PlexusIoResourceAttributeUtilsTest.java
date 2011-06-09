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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.StreamConsumer;

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

        URL resource = Thread.currentThread().getContextClassLoader().getResource(
            getClass().getName().replace( '.', '/' ) + ".class" );

        if ( resource == null )
        {
            throw new IllegalStateException(
                "SOMETHING IS VERY WRONG. CANNOT FIND THIS TEST CLASS IN THE CLASSLOADER." );
        }

        File f = new File( resource.getPath().replaceAll( "%20", " " ) );

        Map attrs =
            PlexusIoResourceAttributeUtils.getFileAttributesByPath( f, new ConsoleLogger( Logger.LEVEL_INFO, "test" ),
                                                                    Logger.LEVEL_DEBUG );

        FileAttributes fileAttrs = (FileAttributes) attrs.get( f.getAbsolutePath() );

        System.out.println( "Got attributes for: " + f.getAbsolutePath() + fileAttrs );

        assertNotNull( fileAttrs );
        assertTrue( fileAttrs.isOwnerReadable() );
        assertEquals( System.getProperty( "user.name" ), fileAttrs.getUserName() );
    }

    public void testAttributeParsers()
    {
        assertTrue( PlexusIoResourceAttributeUtils.totalLinePattern.matcher( "totalt 420" ).matches() );
        assertTrue( PlexusIoResourceAttributeUtils.totalLinePattern.matcher( "total 420" ).matches() );
        assertTrue( PlexusIoResourceAttributeUtils.totalLinePattern.matcher( "JSHS 420" ).matches() );
    }

    // to make a new testcase
    // Checkout plexus-io, and from the root of the module type the following:
    // ls -1nlaR src/main/java/org/codehaus/plexus/components >src/test/resources/`uname`-p1.txt
    // ls -1laR src/main/java/org/codehaus/plexus/components >src/test/resources/`uname`-p2.txt
    // Then a test-method that tests the output-

    public void testParserUbuntu10_04_en()
        throws Exception
    {
        final Map map = checkStream( "Linux" );

        final FileAttributes o = (FileAttributes) map.get(
            "src/main/java/org/codehaus/plexus/components/io/attributes/AttributeConstants.java" );

        // -rw-r--r--  1 1020 1030   11108 Mar 16 22:42 build.xml
        assertEquals( "-rw-rw-r--", new String( o.getLsModeParts() ) );
        assertEquals( 1020, o.getUserId() );
        assertEquals( 1030, o.getGroupId() );
        // Should probably test pass 2 too...
    }

    public void testSingleLine()
        throws Exception
    {
        String output =
            "-rw-r--r-- 1 1003 1002 1533 2010-04-23 14:34 /home/bamboo/agent1/xml-data/build-dir/PARALLEL-CH1W/checkout/spi/pom.xml";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        PlexusIoResourceAttributeUtils.AttributeParser parser = getParser();
        parse( byteArrayInputStream, parser );
    }

    public void testOddLinuxFormatWithExtermelyLargeNumericsSingleLine()
        throws Exception
    {
        String output =
            "-rw-rw-r-- 1 4294967294 4294967294 7901 2011-06-07 18:39 /mnt/work/src/maven-plugins-trunk/maven-compiler-plugin/pom.xml";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        PlexusIoResourceAttributeUtils.AttributeParser parser = getParser();
        parse( byteArrayInputStream, parser );
    }

    public void testParserCygwin()
        throws Exception
    {
        final Map map = checkStream( "CYGWIN_NT-5.1" );
        final FileAttributes o = (FileAttributes) map.get(
            "src/main/java/org/codehaus/plexus/components/io/attributes/AttributeConstants.java" );

        // -rw-r--r--  1 1020 1030   11108 Mar 16 22:42 build.xml
        assertEquals( "-rw-r--r--", new String( o.getLsModeParts() ) );
        assertEquals( 203222, o.getUserId() );
        assertEquals( 10513, o.getGroupId() );
    }

    public void testParserSolaris()
        throws Exception
    {
        checkStream( "SunOS" );
    }

    public void testMisc()
        throws Exception
    {
        checkStream( "Test" );
    }

    public void testParserFreeBsd()
        throws Exception
    {
        checkStream( "FreeBSD" );
    }

    private InputStream getStream( String s )
    {
        return this.getClass().getClassLoader().getResourceAsStream( s );
    }

    private Map checkStream( String baseName )
        throws Exception
    {
        PlexusIoResourceAttributeUtils.AttributeParser parser = getParser();

        InputStream phase1 = getStream( baseName + "-p1.txt" );
        parse( phase1, parser );
        InputStream phase2 = getStream( baseName + "-p2.txt" );
        parser.initSecondPass();
        parse( phase2, parser );
        return parser.getAttributesByPath();
    }

    private PlexusIoResourceAttributeUtils.AttributeParser getParser()
    {
        BufferingStreamConsumer target = new BufferingStreamConsumer();
        Logger logger = new ConsoleLogger( 1, "UnitTest" );
        return new PlexusIoResourceAttributeUtils.AttributeParser( target, logger );
    }

    private void parse( InputStream stream, PlexusIoResourceAttributeUtils.AttributeParser parser )
        throws Exception
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( stream ) );
        String line = bufferedReader.readLine();
        int lineNum = 0;
        try
        {
            while ( line != null )
            {
                parser.consumeLine( line );
                line = bufferedReader.readLine();
                lineNum++;
            }
        }
        catch ( Exception e )
        {
            Exception exception = new Exception( "At line " + lineNum + "in source:" + line );
            exception.initCause( e );
            throw e;
        }
    }

    static final class BufferingStreamConsumer
        implements StreamConsumer
    {

        final List lines = new Vector(); // Thread safe

        public void consumeLine( String line )
        {
            lines.add( line );
        }

        public Iterator iterator()
        {
            return lines.iterator();
        }

        public String toString()
        {
            StringBuffer resp = new StringBuffer();
            Iterator iterator = iterator();
            while ( iterator.hasNext() )
            {
                resp.append( iterator.next() );
                resp.append( "\n" );
            }
            return "BufferingStreamConsumer{" + resp.toString() + "}";
        }
    }

}
