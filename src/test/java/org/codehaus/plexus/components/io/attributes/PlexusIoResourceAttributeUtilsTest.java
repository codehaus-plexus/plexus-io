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

import junit.framework.TestCase;

import org.codehaus.plexus.components.io.attributes.AttributeParser.NumericUserIDAttributeParser;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.*;
import java.net.URL;
import java.util.*;

public class PlexusIoResourceAttributeUtilsTest
    extends TestCase
{

    private Locale origSystemLocale;

    @Override
    protected void setUp()
        throws Exception
    {
       this.origSystemLocale = Locale.getDefault();
       // sample ls output files have US date format and we use SimpleDateFormt with system locale for ls date format parsing 
       // otherwise test could fail on systems with non-US locales
       Locale.setDefault( Locale.US );
    }
    
    @Override
    protected void tearDown()
        throws Exception
    {
        Locale.setDefault( origSystemLocale );
    }
    
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

        PlexusIoResourceAttributes fileAttrs = (PlexusIoResourceAttributes) attrs.get( f.getAbsolutePath() );

        System.out.println( "Got attributes for: " + f.getAbsolutePath() + fileAttrs );

        assertNotNull( fileAttrs );
        assertTrue( fileAttrs.isOwnerReadable() );
        assertEquals( System.getProperty( "user.name" ), fileAttrs.getUserName() );
    }

    public void testFolderJava7()
            throws IOException, CommandLineException {

        if (Os.isFamily( Os.FAMILY_WINDOWS ) || Os.isFamily( Os.FAMILY_WIN9X )){
            return; // Nothing to do here.
        }

        URL resource = Thread.currentThread().getContextClassLoader().getResource(
            getClass().getName().replace( '.', '/' ) + ".class" );

        if ( resource == null )
        {
            throw new IllegalStateException(
                "SOMETHING IS VERY WRONG. CANNOT FIND THIS TEST CLASS IN THE CLASSLOADER." );
        }

        File f = new File( resource.getPath().replaceAll( "%20", " " ) );
        final File aDir = f.getParentFile().getParentFile().getParentFile();

        Commandline commandLine = new Commandline("chmod");
        commandLine.addArguments(new String[]{"763", f.getAbsolutePath()});

        CommandLineUtils.executeCommandLine(commandLine, null , null);
        Map attrs =
            PlexusIoResourceAttributeUtils.getFileAttributesByPath( aDir, new ConsoleLogger( Logger.LEVEL_INFO, "test" ),
                                                                    Logger.LEVEL_DEBUG );

        PlexusIoResourceAttributes fileAttrs = (PlexusIoResourceAttributes) attrs.get( f.getAbsolutePath() );

        assertTrue( fileAttrs.isGroupReadable());
        assertTrue( fileAttrs.isGroupWritable());
        assertFalse( fileAttrs.isGroupExecutable());

        assertTrue( fileAttrs.isOwnerExecutable());
        assertTrue( fileAttrs.isOwnerReadable());
        assertTrue( fileAttrs.isOwnerWritable());

        assertTrue( fileAttrs.isWorldExecutable());
        assertFalse( fileAttrs.isWorldReadable());
        assertTrue( fileAttrs.isWorldWritable());

        assertNotNull(fileAttrs);
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
        assertEquals( 1020, o.getUserId().intValue() );
        assertEquals( 1030, o.getGroupId().intValue() );
        // Should probably test pass 2 too...
    }

    public void testSingleLine()
        throws Exception
    {
        String output =
            "-rw-r--r-- 1 1003 1002 1533 2010-04-23 14:34 /home/bamboo/agent1/xml-data/build-dir/PARALLEL-CH1W/checkout/spi/pom.xml";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        AttributeParser parser = getNumericParser();
        parse( byteArrayInputStream, parser );
}
    
    public void testReversedMonthDayOrder()
        throws Exception
    {
        String output = //
            "-rw-r--r--   1 501  80  7683 31 May 10:06 pom_newer.xml\n" + //
            "-rwxr--r--   1 502  81  7683  1 Jun 2010  pom_older.xml";
        InputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        NumericUserIDAttributeParser parser = getNumericParser();
        parse( byteArrayInputStream, parser );
        Map<String, PlexusIoResourceAttributes> map = parser.getAttributesByPath();
        
        // 6 months or newer ls date format
        FileAttributes newerFileAttr = (FileAttributes) map.get( "pom_newer.xml" );
        assertNotNull( newerFileAttr );
        assertEquals( "-rw-r--r--", new String( newerFileAttr.getLsModeParts() ) );
        assertEquals( 501, newerFileAttr.getUserId().intValue() );
        assertEquals( 80, newerFileAttr.getGroupId().intValue() );
        
        // older than 6 months ls date format
        FileAttributes olderFileAttr = (FileAttributes) map.get( "pom_older.xml" );
        assertNotNull( olderFileAttr );
        assertEquals( "-rwxr--r--", new String( olderFileAttr.getLsModeParts() ) );
        assertEquals( 502, olderFileAttr.getUserId().intValue() );
        assertEquals( 81, olderFileAttr.getGroupId().intValue() );
    }

    public void testOddLinuxFormatWithExtermelyLargeNumericsSingleLine()
        throws Exception
    {
        String output =
            "-rw-rw-r-- 1 4294967294 4294967294 7901 2011-06-07 18:39 /mnt/work/src/maven-plugins-trunk/maven-compiler-plugin/pom.xml";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        AttributeParser parser = getNumericParser();
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
        assertEquals( 203222, o.getUserId().intValue() );
        assertEquals( 10513, o.getGroupId().intValue() );
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

    public void testMergeAttributesWithNullBase() {

        char[] mode = new char[10];
        Arrays.fill(mode, (char) 0);

        FileAttributes override = new FileAttributes(1001, "myUser", 1001, "test", mode);
        FileAttributes defaults = new FileAttributes(1000, "defaultUser", 1000, "defaultTest", mode);

        PlexusIoResourceAttributes attributes
                = PlexusIoResourceAttributeUtils.mergeAttributes(override, null, defaults);

        assertEquals(attributes.getGroupId(), new Integer(1001));
        assertEquals(attributes.getUserId(), new Integer(1001));
    }

    public void testMergeAttributesWithNullOverrideGroup() {
        char[] mode = new char[10];
        Arrays.fill(mode, (char) 0);

        FileAttributes override = new FileAttributes(1001, "myUser", -1, null, mode);
        FileAttributes defaults = new FileAttributes(1000, "defaultUser", 1000, "defaultGroup", mode);

        PlexusIoResourceAttributes attributes
                = PlexusIoResourceAttributeUtils.mergeAttributes(override, null, defaults);

        assertEquals(new Integer(1000), attributes.getGroupId());
        assertEquals(new Integer(1001), attributes.getUserId());
    }

    private InputStream getStream( String s )
    {
        return this.getClass().getClassLoader().getResourceAsStream( s );
    }

    private Map checkStream( String baseName )
        throws Exception
    {


        AttributeParser.NumericUserIDAttributeParser numericParser = getNumericParser();
        InputStream phase1 = getStream( baseName + "-p1.txt" );
        parse( phase1, numericParser );


        final AttributeParser.SymbolicUserIDAttributeParser nameBasedParser = getNameBasedParser();
        InputStream phase2 = getStream( baseName + "-p2.txt" );
        parse( phase2, nameBasedParser );

        return nameBasedParser.merge( numericParser );
    }

    private AttributeParser.NumericUserIDAttributeParser getNumericParser()
    {
        BufferingStreamConsumer target = new BufferingStreamConsumer();
        Logger logger = new ConsoleLogger( 1, "UnitTest" );
        return new AttributeParser.NumericUserIDAttributeParser( target, logger );
    }

    private AttributeParser.SymbolicUserIDAttributeParser getNameBasedParser()
    {
        BufferingStreamConsumer target = new BufferingStreamConsumer();
        Logger logger = new ConsoleLogger( 1, "UnitTest" );
        return new AttributeParser.SymbolicUserIDAttributeParser( target, logger );
    }

    private void parse( InputStream stream, AttributeParser parser )
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

        final List<String> lines = new Vector<String>(); // Thread safe

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
            StringBuilder resp = new StringBuilder();
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
