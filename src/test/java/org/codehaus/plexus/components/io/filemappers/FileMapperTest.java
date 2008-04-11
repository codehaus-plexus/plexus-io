package org.codehaus.plexus.components.io.filemappers;

import java.lang.reflect.UndeclaredThrowableException;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.components.io.filemappers.FileExtensionMapper;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.components.io.filemappers.FlattenFileMapper;
import org.codehaus.plexus.components.io.filemappers.IdentityMapper;
import org.codehaus.plexus.components.io.filemappers.MergeFileMapper;

/**
 * Test case for the various file mappers.
 */
public class FileMapperTest extends PlexusTestCase
{
    protected void testFileMapper( FileMapper pMapper, String[] pInput, String[] pOutput )
    {
        for ( int i = 0; i < pInput.length; i++ )
        {
            final String input = pInput[i];
            final String output = pOutput[i];
            final String result;
            try
            {
                if ( output == null )
                {
                    try
                    {
                        pMapper.getMappedFileName( input );
                        fail( "Expected IllegalArgumentException for mapper " + pMapper.getClass().getName()
                                        + " and input: " + input );
                    }
                    catch ( IllegalArgumentException e )
                    {
                        // Ok
                    }
                    continue;
                }
                result = pMapper.getMappedFileName( input );
                if ( output.equals( result ) )
                {
                    continue;
                }
            }
            catch ( Throwable t )
            {
                throw new UndeclaredThrowableException( t, "Mapper " + pMapper.getClass().getName()
                                + " failed for input " + input + ": " + t.getMessage() );
            }
            if ( !output.equals( result ) )
            {
                fail( "Mapper " + pMapper.getClass().getName() + " failed for input " + input + ": Expected " + output
                                + ", got " + result );
            }
        }
    }

    protected static final String[] SAMPLES =
        new String[] { null, "", "a", "xyz.gif", "b/a", "b/xyz.gif", "b\\a", "b\\xyz.gif" };

    public void testIdentityMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        testFileMapper( new IdentityMapper(), SAMPLES, results );
    }

    private String[] getIdentityResults()
    {
        final String[] results = new String[SAMPLES.length];
        System.arraycopy( SAMPLES, 0, results, 0, SAMPLES.length );
        results[1] = null;
        return results;
    }

    public void testDefaultMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        testFileMapper( (FileMapper) lookup( FileMapper.ROLE ), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.ROLE, IdentityMapper.ROLE_HINT ), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.ROLE, FileMapper.DEFAULT_ROLE_HINT ), SAMPLES, results );
    }

    public void testFileExtensionMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        for ( int i = 2; i <= 6; i += 2 )
        {
            results[i] += ".png";
        }
        for ( int i = 3; i <= 7; i += 2 )
        {
            results[i] = results[i].substring( 0, results[i].length() - ".gif".length() ) + ".png";
        }
        testFileExtensionMapper( results, new FileExtensionMapper() );
        testFileExtensionMapper( results, (FileExtensionMapper) lookup( FileMapper.ROLE, FileExtensionMapper.ROLE_HINT ) );
    }

    private void testFileExtensionMapper( final String[] results, final FileExtensionMapper mapper )
    {
        mapper.setTargetExtension( "png" );
        testFileMapper( mapper, SAMPLES, results );
        mapper.setTargetExtension( ".png" );
        testFileMapper( mapper, SAMPLES, results );
    }

    public void testFlattenMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        results[4] = results[6] = results[2];
        results[5] = results[7] = results[3];
        testFileMapper( new FlattenFileMapper(), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.ROLE, FlattenFileMapper.ROLE_HINT ), SAMPLES, results );
    }

    private void testMergeMapper( String pTargetName, String[] pResults, MergeFileMapper pMapper )
    {
        pMapper.setTargetName( pTargetName );
        testFileMapper( pMapper, SAMPLES, pResults );
    }

    public void testMergeMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        final String targetName = "zgh";
        for ( int i = 2; i < results.length; i++ )
        {
            results[i] = targetName;
        }
        testMergeMapper( targetName, results, new MergeFileMapper() );
        testMergeMapper( targetName, results, (MergeFileMapper) lookup( FileMapper.ROLE, MergeFileMapper.ROLE_HINT ) );
    }

    public void testPrefixMapper() throws Exception
    {
        final String prefix = "x7Rtf";
        final String[] results = getIdentityResults();
        testFileMapper( new PrefixFileMapper(), SAMPLES, results );
        testFileMapper( (PrefixFileMapper) lookup( FileMapper.ROLE, PrefixFileMapper.ROLE_HINT ), SAMPLES, results );
        for ( int i = 0;  i < results.length;  i++ )
        {
            if  ( results[i] != null )
            {
                results[i] = prefix + results[i];
            }
        }
        PrefixFileMapper mapper = new PrefixFileMapper();
        mapper.setPrefix( prefix );
        testFileMapper( mapper, SAMPLES, results );
        mapper = (PrefixFileMapper) lookup( FileMapper.ROLE, PrefixFileMapper.ROLE_HINT );
        mapper.setPrefix( prefix );
        testFileMapper( mapper, SAMPLES, results );
    }
}
