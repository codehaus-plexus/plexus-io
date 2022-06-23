package org.codehaus.plexus.components.io.filemappers;

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

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test case for the various file mappers.
 */
public class FileMapperTest extends TestSupport
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
                fail( "Mapper " + pMapper.getClass().getName() + " failed for input nr. " + i
                		+ ", " + input + ": Expected " + output
                        + ", got " + result );
            }
        }
    }

    protected static final String[] SAMPLES =
        new String[] { null, "", "a", "xyz.gif", "b/a", "b/xyz.gif", "b\\a", "b\\xyz.gif", "c.c/a", "c.c/xyz.gif",
            "c.c\\a", "c.c\\xyz.gif" };

    @Test
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

    @Test
    public void testDefaultMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        testFileMapper( (FileMapper) lookup( FileMapper.class ), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.class, IdentityMapper.ROLE_HINT ), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.class ), SAMPLES, results );
    }

    @Test
    public void testFileExtensionMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        for ( int i = 2; i <= 10; i += 2 )
        {
            results[i] += ".png";
        }
        for ( int i = 3; i <= 11; i += 2 )
        {
            results[i] = results[i].substring( 0, results[i].length() - ".gif".length() ) + ".png";
        }
        testFileExtensionMapper( results, new FileExtensionMapper() );
        testFileExtensionMapper( results, (FileExtensionMapper) lookup( FileMapper.class, FileExtensionMapper.ROLE_HINT ) );
    }

    private void testFileExtensionMapper( final String[] results, final FileExtensionMapper mapper )
    {
        mapper.setTargetExtension( "png" );
        testFileMapper( mapper, SAMPLES, results );
        mapper.setTargetExtension( ".png" );
        testFileMapper( mapper, SAMPLES, results );
    }

    @Test
    public void testFlattenMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        results[4] = results[6] = results[8] = results[10] = results[2];
        results[5] = results[7] = results[9] = results[11] = results[3];
        testFileMapper( new FlattenFileMapper(), SAMPLES, results );
        testFileMapper( (FileMapper) lookup( FileMapper.class, FlattenFileMapper.ROLE_HINT ), SAMPLES, results );
    }

    private void testMergeMapper( String pTargetName, String[] pResults, MergeFileMapper pMapper )
    {
        pMapper.setTargetName( pTargetName );
        testFileMapper( pMapper, SAMPLES, pResults );
    }

    @Test
    public void testMergeMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        final String targetName = "zgh";
        for ( int i = 2; i < results.length; i++ )
        {
            results[i] = targetName;
        }
        testMergeMapper( targetName, results, new MergeFileMapper() );
        testMergeMapper( targetName, results, (MergeFileMapper) lookup( FileMapper.class, MergeFileMapper.ROLE_HINT ) );
    }

    @Test
    public void testPrefixMapper() throws Exception
    {
        final String prefix = "x7Rtf";
        final String[] results = getIdentityResults();
        testFileMapper( new PrefixFileMapper(), SAMPLES, results );
        testFileMapper( (PrefixFileMapper) lookup( FileMapper.class, PrefixFileMapper.ROLE_HINT ), SAMPLES, results );
        for ( int i = 0; i < results.length; i++ )
        {
            if ( results[i] != null )
            {
                results[i] = prefix + results[i];
            }
        }
        PrefixFileMapper mapper = new PrefixFileMapper();
        mapper.setPrefix( prefix );
        testFileMapper( mapper, SAMPLES, results );
        mapper = (PrefixFileMapper) lookup( FileMapper.class, PrefixFileMapper.ROLE_HINT );
        mapper.setPrefix( prefix );
        testFileMapper( mapper, SAMPLES, results );
    }

    @Test
    public void testSuffixMapper() throws Exception
    {
        final String suffix = "suffix";
        String[] samples = Arrays.copyOf( SAMPLES, SAMPLES.length + 2 );
        samples[samples.length - 2] = "archive.tar.gz";
        samples[samples.length - 1] = "directory/archive.tar.gz";
        String[] results = new String[] { null, null, "asuffix", "xyzsuffix.gif", "b/asuffix", "b/xyzsuffix.gif",
            "b\\asuffix", "b\\xyzsuffix.gif", "c.c/asuffix", "c.c/xyzsuffix.gif", "c.c\\asuffix", "c.c\\xyzsuffix.gif",
            "archivesuffix.tar.gz", "directory/archivesuffix.tar.gz" };
        SuffixFileMapper mapper = new SuffixFileMapper();
        mapper.setSuffix( suffix );
        testFileMapper( mapper, samples, results );
        mapper = (SuffixFileMapper) lookup( FileMapper.class, SuffixFileMapper.ROLE_HINT );
        mapper.setSuffix( suffix );
        testFileMapper( mapper, samples, results );
    }

    private RegExpFileMapper configure( RegExpFileMapper pMapper, String pPattern, String pReplacement )
    {
        pMapper.setPattern( pPattern );
        pMapper.setReplacement( pReplacement );
        return pMapper;
    }

    @Test
    public void testRegExpFileMapper() throws Exception
    {
        final String[] results = getIdentityResults();
        results[3] = "xyz.jpg";
        results[5] = "b/xyz.jpg";
        results[7] = "b\\xyz.jpg";
        results[9] = "c.c/xyz.jpg";
        results[11] = "c.c\\xyz.jpg";
        testFileMapper( configure(new RegExpFileMapper(), "\\.gif$", ".jpg"), SAMPLES, results );
        testFileMapper( configure(new RegExpFileMapper(), "^(.*)\\.gif$", "$1.jpg"), SAMPLES, results );
        testFileMapper( configure((RegExpFileMapper) lookup( FileMapper.class, RegExpFileMapper.ROLE_HINT ), "\\.gif$", ".jpg"), SAMPLES, results );
        final RegExpFileMapper mapper = configure( new RegExpFileMapper(), "c", "f" );
        mapper.setReplaceAll( true );
        final String[] fResults = getIdentityResults();
        fResults[8] = "f.f/a";
        fResults[9] = "f.f/xyz.gif";
        fResults[10] = "f.f\\a";
        fResults[11] = "f.f\\xyz.gif";
        testFileMapper( mapper, SAMPLES, fResults );
    }
}