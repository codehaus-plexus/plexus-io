package org.codehaus.plexus.components.io.resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kristian Rosenvold
 */
public class LinefeedNormalizingInputStream
    extends InputStream
{

    private boolean slashRSeen = false;

    private boolean slashNSeen = false;

    private boolean eofSeen = false;

    private final LinefeedMode linefeedMode;

    private final InputStream target;

    private final boolean ensureLineFeedAtEndOfFile;

    public LinefeedNormalizingInputStream( InputStream in, LinefeedMode linefeedMode,
                                              boolean ensureLineFeedAtEndOfFile )
    {
        this.target = in;
        this.linefeedMode = linefeedMode;
        this.ensureLineFeedAtEndOfFile = ensureLineFeedAtEndOfFile;
    }

    private int readWithUpdate()
        throws IOException
    {
        final int target = this.target.read();
        eofSeen = target == -1;
        if ( eofSeen )
        {
            return target;
        }
        slashRSeen = target == '\r';
        slashNSeen = target == '\n';
        return target;
    }

    @Override public int read()
        throws IOException
    {
        if ( eofSeen )
        {
            return eofGame();
        }
        else
        {
            int target = readWithUpdate();
            if (eofSeen) return eofGame();
            if ( target == '\r' )
            {
                if ( linefeedMode == LinefeedMode.unix )
                {
                    target = readWithUpdate();
                }
            }
            return target;
        }
    }

    private int eofGame()
    {
        if (!ensureLineFeedAtEndOfFile ) return -1;
        switch ( linefeedMode )
        {
            case unix:
                if ( !slashNSeen )
                {
                    slashNSeen = true;
                    return '\n';
                }
                else
                {
                    return -1;
                }
            case dos:
                if ( !slashNSeen && !slashRSeen ) // We did not see trailing line feed
                {
                    slashRSeen = true;
                    return '\r';
                }
                if ( !slashNSeen )
                {
                    slashRSeen = false;
                    slashNSeen = true;
                    return '\n';
                }
                else
                {
                    return -1;
                }

            case preserve:
                return -1;
            default:
                throw new IllegalArgumentException( "Unsupported line feed mode" );
        }
    }

    @Override public synchronized void mark( int readlimit )
    {
        throw new UnsupportedOperationException( "Mark not implemented yet" );
    }
}
