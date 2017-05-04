package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kristian Rosenvold
 */
public class ClosingInputStream
    extends InputStream
{
    private final InputStream target;

    private final InputStream other;

    public ClosingInputStream( InputStream target, InputStream other )
    {
        this.target = target;
        this.other = other;
    }

    @Override
    public int read()
        throws IOException
    {
        return target.read();
    }

    @Override
    public int read( byte[] b )
        throws IOException
    {
        return target.read( b );
    }

    @Override
    public int read( byte[] b, int off, int len )
        throws IOException
    {
        return target.read( b, off, len );
    }

    @Override
    public long skip( long n )
        throws IOException
    {
        return target.skip( n );
    }

    @Override
    public int available()
        throws IOException
    {
        return target.available();
    }

    @Override
    public void close()
        throws IOException
    {
        other.close();
        target.close();
    }

    @Override
    public void mark( int readlimit )
    {
        target.mark( readlimit );
    }

    @Override
    public void reset()
        throws IOException
    {
        target.reset();
    }

    @Override
    public boolean markSupported()
    {
        return target.markSupported();
    }
}
