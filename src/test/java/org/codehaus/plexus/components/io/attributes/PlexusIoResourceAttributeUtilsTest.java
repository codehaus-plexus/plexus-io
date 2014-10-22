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

import junit.framework.Assert;
import org.codehaus.plexus.components.io.attributes.AttributeParser.NumericUserIDAttributeParser;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResource;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import static org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils.getFileAttributes;

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
            PlexusIoResourceAttributeUtils.getFileAttributesByPath( f, true, true );

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
        Map attrs = PlexusIoResourceAttributeUtils.getFileAttributesByPath( aDir, true, true );

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

    public void ignoredTestParserUbuntu10_04_en()
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

    public void testANotherOneWithTrouble()
        throws Exception
    {
        String output = "drwxr-xr-x+ 13 kristian  staff  442 Oct 11 14:14 ..";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        AttributeParser parser = getNameBasedParser1la();
        parse( byteArrayInputStream, parser );

    }
    public void testSingleLine()
        throws Exception
    {
        String output =
            "-rw-r--r-- 1 1003 1002 1533 2010-04-23 14:34 /home/bamboo/agent1/xml-data/build-dir/PARALLEL-CH1W/checkout/spi/pom.xml";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        AttributeParser parser = getNumericParser1nla();
        parse( byteArrayInputStream, parser );
}

    public void testSrcResource()
        throws IOException
    {
        File dir = new File("src/test/resources/symlinks");
        final Map<String, PlexusIoResourceAttributes> fileAttributesByPathScreenScrape =
            PlexusIoResourceAttributeUtils.getFileAttributesByPathScreenScrape( dir, true, true );
        assertNotNull( fileAttributesByPathScreenScrape);
        PlexusIoResourceAttributes pr = null;
        for ( String s : fileAttributesByPathScreenScrape.keySet() )
        {
            if (s.endsWith( "targetFile.txt" )) pr = fileAttributesByPathScreenScrape.get( s);
        }
        assertNotNull(pr);

        assertEquals(0644, pr.getOctalMode());
    }
    public void testPermissionDenied()
    {
        File dir = new File("src/test/lsPerms");
        try {
            PlexusIoResourceAttributeUtils.getFileAttributesByPathScreenScrape( dir, true,
                                                                                true );
            // We could search for permission denied here and throw some kind of specialized exception.
            fail("We were supposed to get an io exceptions");
        } catch (IOException ignore)
        {
            ignore.printStackTrace();
        }
    }

    public void testReversedMonthDayOrder()
        throws Exception
    {
        String output = //
            "-rw-r--r--   1 501  80  7683 31 May 10:06 pom_newer.xml\n" + //
            "-rwxr--r--   1 502  81  7683  1 Jun 2010  pom_older.xml";
        InputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        NumericUserIDAttributeParser parser = getNumericParser1nla();
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
        AttributeParser parser = getNumericParser1nla();
        parse( byteArrayInputStream, parser );
    }

    public void testMacOsWithSpace() throws Exception {
        String output =
                "-rwxrw--wx  1 kristian  DOMZZZ\\Domain Users  15387 Oct 20 15:30 /Users/kristian/lsrc/plexus/plexus-io/target/test-classes/org/codehaus/plexus/components/io/attributes/PlexusIoResourceAttributeUtilsTest.class";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( output.getBytes() );
        AttributeParser parser = getNameBasedParser1la();
        parse( byteArrayInputStream, parser );
        final Map<String, PlexusIoResourceAttributes> attributesByPath = parser.getAttributesByPath();
        String path = "/Users/kristian/lsrc/plexus/plexus-io/target/test-classes/org/codehaus/plexus/components/io/attributes/";
    }

    public void testMacOsLine()
        throws Exception
    {
        String line = "drwxr-xr-x+ 3 1000  20  102 Oct 11 14:24 ..";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( line.getBytes() );
        AttributeParser parser = getNumericParser1nla();
        parse( byteArrayInputStream, parser );

    }
    public void ignoredTestParserCygwin()
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

    public void testParserb26()
        throws Exception
    {
        final Map<String, PlexusIoResourceAttributes> map = checkStream2( "b26-linux" );
        PlexusIoResourceAttributes attrs = null;
        for ( String s : map.keySet() )
        {
            if (s.endsWith( "App.java" )) attrs = map.get(s);
        }
        Assert.assertEquals( 0664, attrs.getOctalMode() );

    }

    public void testMergeAttributesWithNullBase()
    {
        PlexusIoResourceAttributes override =
            new SimpleResourceAttributes( 1001, "myUser", 1001, "test", 0 );
        PlexusIoResourceAttributes defaults =
            new SimpleResourceAttributes( 1000, "defaultUser", 1000, "defaultTest", 0 );

        PlexusIoResourceAttributes attributes;
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, null, defaults );

        assertEquals( Integer.valueOf( 1001 ), attributes.getGroupId() );
        assertEquals( Integer.valueOf( 1001 ), attributes.getUserId() );
    }

    public void testMergeAttributesWithNullOverrideGroup()
    {
        final PlexusIoResourceAttributes override =
            new SimpleResourceAttributes( 1001, "myUser", -1, null, 0 );
        final PlexusIoResourceAttributes defaults =
            new SimpleResourceAttributes( 1000, "defaultUser", 1000, "defaultGroup", 0 );

        PlexusIoResourceAttributes attributes
            = PlexusIoResourceAttributeUtils.mergeAttributes( override, null, defaults );

        assertEquals( attributes.getGroupId(), Integer.valueOf( 1000 ) );
        assertEquals( attributes.getUserId(), Integer.valueOf( 1001 ) );
    }

    public void testMergeAttributesOverride()
    {
        final PlexusIoResourceAttributes blank = new SimpleResourceAttributes();
        final PlexusIoResourceAttributes invalid = new SimpleResourceAttributes( -1, null, -1, null, -1 );
        final PlexusIoResourceAttributes override =
            new SimpleResourceAttributes( 1111, "testUser", 2222, "testGroup", 0777 );
        final PlexusIoResourceAttributes defaults =
            new SimpleResourceAttributes( 3333, "defaultUser", 4444, "defaultGroup", 0444 );
        final PlexusIoResourceAttributes base =
            new SimpleResourceAttributes( 5555, "baseUser", 6666, "baseGroup", 0111 );

        PlexusIoResourceAttributes attributes;

        // When override is null, base is returned verbatim
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( null, null, null );
        assertNull( attributes );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( null, null, defaults );
        assertNull( attributes );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( null, base, null );
        assertSame( base, attributes );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( null, base, defaults );
        assertSame( base, attributes );

        // Test cases when override is non-null
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, null, null );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, base, null );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, null, defaults );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, base, defaults );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes(
            override, blank, null );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( override, invalid, null );

        assertEquals( Integer.valueOf( 1111 ), attributes.getUserId() );
        assertEquals( "testUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 2222 ), attributes.getGroupId() );
        assertEquals( "testGroup", attributes.getGroupName() );
        assertEquals( 0777, attributes.getOctalMode() );

        // Test cases when override has only blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( blank, base, null );

        assertEquals( Integer.valueOf( 5555 ), attributes.getUserId() );
        assertEquals( "baseUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 6666 ), attributes.getGroupId() );
        assertEquals( "baseGroup", attributes.getGroupName() );
        assertEquals( 0111, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( invalid, base, null );

        assertEquals( Integer.valueOf( 5555 ), attributes.getUserId() );
        assertEquals( "baseUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 6666 ), attributes.getGroupId() );
        assertEquals( "baseGroup", attributes.getGroupName() );
        assertEquals( 0111, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( blank, base, defaults );

        assertEquals( Integer.valueOf( 5555 ), attributes.getUserId() );
        assertEquals( "baseUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 6666 ), attributes.getGroupId() );
        assertEquals( "baseGroup", attributes.getGroupName() );
        assertEquals( 0111, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( invalid, base, defaults );

        assertEquals( Integer.valueOf( 5555 ), attributes.getUserId() );
        assertEquals( "baseUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 6666 ), attributes.getGroupId() );
        assertEquals( "baseGroup", attributes.getGroupName() );
        assertEquals( 0111, attributes.getOctalMode() );
    }

    public void testFileAttributes()
        throws IOException
    {
        PlexusIoResourceAttributes attrs = getFileAttributes( new File( "src/test/resources/symlinks/src/fileW.txt" ) );
        assertFalse( attrs.isSymbolicLink() );
        assertTrue( StringUtils.isNotEmpty( attrs.getUserName()));
        if (! Os.isFamily(Os.FAMILY_WINDOWS))
        {
            assertTrue( StringUtils.isNotEmpty( attrs.getGroupName()));
            assertNotNull(  attrs.getGroupId() );
            assertNotNull(  attrs.getUserId() );
        }
    }

    public void testMergeAttributesDefault()
    {
        final PlexusIoResourceAttributes blank = new SimpleResourceAttributes();
        final PlexusIoResourceAttributes invalid = new SimpleResourceAttributes( -1, null, -1, null, -1 );
        final PlexusIoResourceAttributes defaults =
            new SimpleResourceAttributes( 3333, "defaultUser", 4444, "defaultGroup", 0444 );

        PlexusIoResourceAttributes attributes;

        // Test cases when override and base have blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( blank, blank, defaults );

        assertEquals( Integer.valueOf( 3333 ), attributes.getUserId() );
        assertEquals( "defaultUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 4444 ), attributes.getGroupId() );
        assertEquals( "defaultGroup", attributes.getGroupName() );
        // 0 is a borderline case, for backwards compatibility it is not overridden by value from defaults
        assertEquals( 0, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( invalid, blank, defaults );

        assertEquals( Integer.valueOf( 3333 ), attributes.getUserId() );
        assertEquals( "defaultUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 4444 ), attributes.getGroupId() );
        assertEquals( "defaultGroup", attributes.getGroupName() );
        // 0 is a borderline case, for backwards compatibility it is not overridden by value from defaults
        assertEquals( 0, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( blank, invalid, defaults );

        assertEquals( Integer.valueOf( 3333 ), attributes.getUserId() );
        assertEquals( "defaultUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 4444 ), attributes.getGroupId() );
        assertEquals( "defaultGroup", attributes.getGroupName() );
        assertEquals( 0444, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( invalid, invalid, defaults );

        assertEquals( Integer.valueOf( 3333 ), attributes.getUserId() );
        assertEquals( "defaultUser", attributes.getUserName() );
        assertEquals( Integer.valueOf( 4444 ), attributes.getGroupId() );
        assertEquals( "defaultGroup", attributes.getGroupName() );
        assertEquals( 0444, attributes.getOctalMode() );

        // Test cases when invalid defaults should not override blank values
        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( blank, blank, invalid );

        assertNull( attributes.getUserId() );
        assertNull( attributes.getUserName() );
        assertNull( attributes.getGroupId() );
        assertNull( attributes.getGroupName() );
        assertEquals( 0, attributes.getOctalMode() );

        attributes = PlexusIoResourceAttributeUtils.mergeAttributes( invalid, blank, invalid );

        assertNull( attributes.getUserId() );
        assertNull( attributes.getUserName() );
        assertNull( attributes.getGroupId() );
        assertNull( attributes.getGroupName() );
        assertEquals( 0, attributes.getOctalMode() );
    }

    private InputStream getStream( String s )
    {
        return this.getClass().getClassLoader().getResourceAsStream( s );
    }

    private Map checkStream( String baseName )
        throws Exception
    {


        AttributeParser.NumericUserIDAttributeParser numericParser = getNumericParser1nla();
        InputStream phase1 = getStream( baseName + "-p1.txt" );
        parse( phase1, numericParser );


        final AttributeParser.SymbolicUserIDAttributeParser nameBasedParser = getNameBasedParser1la();
        InputStream phase2 = getStream( baseName + "-p2.txt" );
        parse( phase2, nameBasedParser );

        return nameBasedParser.merge( numericParser );
    }

    private Map<String, PlexusIoResourceAttributes> checkStream2( String baseName )
        throws Exception
    {


        AttributeParser.NumericUserIDAttributeParser numericParser = getNumericParser1nla();
        InputStream phase1 = getStream( baseName + "-1nlaR.txt" );
        parse( phase1, numericParser );


        final AttributeParser.SymbolicUserIDAttributeParser nameBasedParser = getNameBasedParser1la();
        InputStream phase2 = getStream( baseName + "-1laR.txt" );
        parse( phase2, nameBasedParser );

        return nameBasedParser.merge( numericParser );
    }

    private AttributeParser.NumericUserIDAttributeParser getNumericParser1nla()
    {
        return new AttributeParser.NumericUserIDAttributeParser( createConsoleLogger() );
    }

    private AttributeParser.SymbolicUserIDAttributeParser getNameBasedParser1la()
    {
        return new AttributeParser.SymbolicUserIDAttributeParser( createConsoleLogger() );
    }

    private StreamConsumer createConsoleLogger()
    {
        return new StreamConsumer()
        {
            public void consumeLine( String line )
            {
                System.out.println(line);
            }
        };
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
}
