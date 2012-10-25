package org.codehaus.plexus.components.io.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.codehaus.plexus.PlexusTestCase;

public class PlexusIoZipFileResourceCollectionTest
    extends PlexusTestCase
{

    public void testNamelessRootFolder() throws Exception {
        PlexusIoZipFileResourceCollection resourceCollection = new PlexusIoZipFileResourceCollection();
        resourceCollection.setFile( getTestFile( "src/test/jars/namelessrootfolder.jar" ) );
        Iterator iterator = resourceCollection.getEntries();
        PlexusIoURLResource entry = (PlexusIoURLResource) iterator.next();
        assertEquals( "/dummy.txt", entry.getName() );
        BufferedReader d = new BufferedReader(new InputStreamReader(entry.getContents()));
        assertEquals( "dummy content", d.readLine() );
    }

    public void testDescriptionForError() throws Exception {
        PlexusIoZipFileResourceCollection resourceCollection = new PlexusIoZipFileResourceCollection();
        resourceCollection.setFile( getTestFile( "src/test/jars/namelessrootfolder.jar" ) );
        Iterator iterator = resourceCollection.getEntries();
        PlexusIoURLResource entry = (PlexusIoURLResource) iterator.next();
        String descriptionForError = entry.getDescriptionForError();
        assertTrue(descriptionForError.endsWith( "namelessrootfolder.jar!//dummy.txt" ));
    }

}
