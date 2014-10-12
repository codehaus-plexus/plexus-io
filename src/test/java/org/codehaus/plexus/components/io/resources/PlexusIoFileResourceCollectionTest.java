package org.codehaus.plexus.components.io.resources;

import junit.framework.TestCase;

import java.io.File;
import java.util.Iterator;

/**
 * @author Kristian Rosenvold
 */
public class PlexusIoFileResourceCollectionTest
    extends TestCase
{
    public void testGetName()
        throws Exception
    {
        PlexusIoFileResourceCollection
                coll = new PlexusIoFileResourceCollection();
        char nonSeparator = File.separatorChar == '/' ?'\\' : '/';
        coll.setPrefix("fud" + nonSeparator );

        coll.setBaseDir(new File("src/test/resources/symlinks") );
        final Iterator<PlexusIoResource> resources = coll.getResources();
        while (resources.hasNext()){
            final PlexusIoResource next = resources.next();
            final String name = coll.getName( next );
            assertTrue( name.indexOf(nonSeparator) < 0);
        }
    }
}
