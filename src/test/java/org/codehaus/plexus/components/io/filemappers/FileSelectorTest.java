package org.codehaus.plexus.components.io.filemappers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.components.io.fileselectors.AllFilesFileSelector;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResource;


/**
 * Test case for implementations of {@link FileSelector}.
 */
public class FileSelectorTest extends PlexusTestCase
{
    protected void testFileSelector( FileSelector pSelector, String[] pInput, boolean[] pOutput)
        throws IOException
    {
        for ( int i = 0;  i < pInput.length;  i++ )
        {
            final String name = pInput[i];
            AbstractPlexusIoResource resource = new AbstractPlexusIoResource(){
                public InputStream getInputStream() throws IOException
                {
                    throw new IllegalStateException( "Not implemented" );
                }

                public URL getURL() throws IOException
                {
                    throw new IllegalStateException( "Not implemented" );
                }
            };
            resource.setName( name );
            resource.setDirectory( false );
            resource.setFile( true );
            boolean result = pSelector.isSelected( resource );
            if ( result != pOutput[i] )
            {
                fail( "Test fails for selector " + pSelector.getClass().getName()
                      + " and input " + name + ": Expected "
                      + pOutput[i] );
            }
        }
    }

    protected static final String[] SAMPLES =
        new String[]
        {
            "foo/x.gif",
            "foo/y.png",
            "bar/x.gif"
        };

    protected void testFileSelector( AllFilesFileSelector pSelector ) throws Exception
    {
        final boolean[] trues = getAllTrues();
        testFileSelector( pSelector, SAMPLES, trues );
    }

    private boolean[] getAllTrues()
    {
        final boolean[] trues = new boolean[SAMPLES.length];
        for ( int i = 0;  i < trues.length;  i++ )
        {
            trues[i] = true;
        }
        return trues;
    }

    public void testAllFilesFileSelector() throws Exception
    {
        testFileSelector( new AllFilesFileSelector() );
        testFileSelector( (AllFilesFileSelector) lookup( FileSelector.ROLE, FileSelector.DEFAULT_ROLE_HINT ) );
        testFileSelector( (AllFilesFileSelector) lookup( FileSelector.ROLE, AllFilesFileSelector.ROLE_HINT ) );
    }

    protected boolean[] getIncludeGifs( String[] pSamples )
    {
        boolean[] result = new boolean[pSamples.length];
        for ( int i = 0;  i < pSamples.length;  i++ )
        {
            result[i] = pSamples[i].endsWith( ".gif" );
        }
        return result;
    }

    protected boolean[] getExcludeBar( String[] pSamples, boolean[] pResult )
    {
        for ( int i = 0;  i < pSamples.length;  i++ )
        {
            if ( pSamples[i].startsWith( "bar/" ) )
            {
                pResult[i] = false;
            }
        }
        return pResult;
    }
    
    protected void testFileSelector( IncludeExcludeFileSelector pSelector ) throws Exception
    {
        testFileSelector( pSelector, SAMPLES, getAllTrues() );
        pSelector.setIncludes( new String[] { "**/*.gif" } );
        testFileSelector( pSelector, SAMPLES, getIncludeGifs( SAMPLES ) );
        pSelector.setExcludes( new String[] { "bar/*" } );
        testFileSelector( pSelector, SAMPLES, getExcludeBar( SAMPLES, getIncludeGifs( SAMPLES ) ) );
        pSelector.setIncludes( null );
        testFileSelector( pSelector, SAMPLES, getExcludeBar( SAMPLES, getAllTrues() ) );
    }

    public void testIncludeExcludeFileSelector() throws Exception
    {
        testFileSelector( new IncludeExcludeFileSelector() );
        testFileSelector( (IncludeExcludeFileSelector) lookup( FileSelector.ROLE, IncludeExcludeFileSelector.ROLE_HINT ) );
    }
}
