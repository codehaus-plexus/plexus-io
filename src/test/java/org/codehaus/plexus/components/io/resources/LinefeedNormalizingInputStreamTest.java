package org.codehaus.plexus.components.io.resources;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.codehaus.plexus.components.io.resources.LinefeedMode.dos;
import static org.codehaus.plexus.components.io.resources.LinefeedMode.preserve;
import static org.codehaus.plexus.components.io.resources.LinefeedMode.unix;

public class LinefeedNormalizingInputStreamTest
    extends TestCase
{

    public void testSimpleString()
        throws Exception
    {
        assertEquals("abc\n", roundtrip("abc", unix) );
        assertEquals("abc\r\n", roundtrip("abc", dos) );
        assertEquals("abc", roundtrip("abc", preserve) );
    }

    public void testInTheMiddleOfTheLine()
        throws Exception
    {
        assertEquals("a\nbc\n", roundtrip("a\r\nbc", unix) );
        assertEquals("a\r\nbc\r\n", roundtrip("a\r\nbc", dos) );
        assertEquals("a\r\nbc", roundtrip("a\r\nbc", preserve) );
    }

    public void testMultipleBlankLines()
        throws Exception
    {
        assertEquals("a\n\nbc\n", roundtrip("a\r\n\r\nbc", unix) );
        assertEquals("a\r\n\r\nbc\r\n", roundtrip("a\r\n\r\nbc", dos) );
        assertEquals("a\r\n\r\nbc", roundtrip("a\r\n\r\nbc", preserve) );
    }

    public void testTwoLinesAtEnd()
        throws Exception
    {
        assertEquals("a\n\n", roundtrip("a\r\n\r\n", unix) );
        assertEquals("a\r\n\r\n", roundtrip("a\r\n\r\n", dos) );
        assertEquals("a\r\n\r\n", roundtrip("a\r\n\r\n", preserve) );
    }

    public void testMalformed()
        throws Exception
    {
        assertEquals("abc", roundtrip("a\rbc", unix, false) );
        assertEquals("a\rbc", roundtrip("a\rbc", dos, false) );
        assertEquals("a\rbc", roundtrip("a\rbc", preserve, false) );
    }

    public void testRetainLineFeed()
        throws Exception
    {
        assertEquals("a\n\n", roundtrip("a\r\n\r\n", unix, false) );
        assertEquals("a\r\n\r\n", roundtrip("a\r\n\r\n", dos, false) );
        assertEquals("a\r\n\r\n", roundtrip("a\r\n\r\n", preserve, false) );
        assertEquals("a", roundtrip("a", unix, false) );
        assertEquals("a", roundtrip("a", dos, false) );
        assertEquals("a", roundtrip("a", preserve, false) );
    }

    private String roundtrip( String msg, LinefeedMode linefeedMode )
        throws IOException
    {
        return roundtrip( msg, linefeedMode, true );
    }

    private String roundtrip( String msg, LinefeedMode linefeedMode, boolean ensure )
        throws IOException
    {
        ByteArrayInputStream baos = new ByteArrayInputStream( msg.getBytes() );
        LinefeedNormalizingInputStream lf = new LinefeedNormalizingInputStream( baos, linefeedMode, ensure );
        byte[] buf = new byte[100];
        final int read = lf.read( buf );
        return new String( buf, 0, read);
    }

}