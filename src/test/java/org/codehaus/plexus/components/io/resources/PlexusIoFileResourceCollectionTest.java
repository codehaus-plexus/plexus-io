package org.codehaus.plexus.components.io.resources;

import java.io.File;

import org.codehaus.plexus.PlexusTestCase;

public class PlexusIoFileResourceCollectionTest
    extends PlexusTestCase
{

    public void testWithFileSuffix()
        throws Exception
    {
        PlexusIoFileResourceCollection resourceCollection = new PlexusIoFileResourceCollection();
        resourceCollection.setBaseDir(new File("src/test/resources") );
        resourceCollection.setFileSuffix("TEST");
        resourceCollection.setIncludes(new String[] {"Test-p1.txt"});
        final PlexusIoFileResource entry  = (PlexusIoFileResource) resourceCollection.getResources().next();
         assertEquals( "Test-p1TEST.txt", entry.getName() );
    }
    
    public void testWithNoFileSuffix()
        throws Exception
    {
        PlexusIoFileResourceCollection resourceCollection = new PlexusIoFileResourceCollection();
        resourceCollection.setBaseDir(new File("src/test/resources") );
        resourceCollection.setIncludes(new String[] {"Test-p1.txt"});
        final PlexusIoFileResource entry  = (PlexusIoFileResource) resourceCollection.getResources().next();
         assertEquals( "Test-p1.txt", entry.getName() );
    }
    
    public void testWithFileWith2Dot()
        throws Exception
    {
        PlexusIoFileResourceCollection resourceCollection = new PlexusIoFileResourceCollection();
        resourceCollection.setBaseDir(new File("src/test/resources") );
        resourceCollection.setIncludes(new String[] {"test.tar.gz"});
        resourceCollection.setFileSuffix("_2");
        final PlexusIoFileResource entry  = (PlexusIoFileResource) resourceCollection.getResources().next();
         assertEquals( "test_2.tar.gz", entry.getName() );
    }
    
    public void testWithFileWith3Dot()
        throws Exception
    {
        PlexusIoFileResourceCollection resourceCollection = new PlexusIoFileResourceCollection();
        resourceCollection.setBaseDir(new File("src/test/resources") );
        resourceCollection.setIncludes(new String[] {"test.any.of.extension"});
        resourceCollection.setFileSuffix("Suffix");
        final PlexusIoFileResource entry  = (PlexusIoFileResource) resourceCollection.getResources().next();
         assertEquals( "testSuffix.any.of.extension", entry.getName() );
    }
}
