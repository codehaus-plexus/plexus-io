package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.PlexusTestCase;


/**
 * Test case for {@link PlexusIoProxyResourceCollection}.
 */
public class PlexusIoProxyResourceCollectionTest extends PlexusTestCase
{
    private final String [] SAMPLE_INCLUDES = {"junk.*", "test/**", "dir*/file.xml"};
    
    private final String [] SAMPLE_EXCLUDES = {"*.junk", "somwhere/**"};
    
    public void testGetDefaultFileSelector() throws Exception
    {
        PlexusIoProxyResourceCollection resCol = new PlexusIoProxyResourceCollection();

        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();

        resCol.setIncludes( SAMPLE_INCLUDES );
        resCol.setExcludes( SAMPLE_EXCLUDES );
        
        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();
        
    }
}
