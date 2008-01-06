package org.codehaus.plexus.components.io.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Default implementation of {@link PlexusIoFileResourceCollection} for
 * zip files, tar files, etc.
 * @author jwi
 *
 */
public abstract class AbstractPlexusIoArchiveResourceCollection extends AbstractPlexusIoResourceCollection
    implements PlexusIoArchivedResourceCollection
{

    private File file;

    /**
     * Sets the zip file
     */
    public void setFile( File file )
    {
        this.file = file;
    }

    /**
     * Returns the zip file
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Returns an iterator over the archives entries.
     */
    protected abstract Iterator getEntries() throws IOException;

    public Iterator getResources() throws IOException
    {
        final List result = new ArrayList();
        for (Iterator it = getEntries();  it.hasNext();  )
        {
            final PlexusIoResource res = (PlexusIoResource) it.next();
            if ( isSelected( res ) )
            {
                result.add( res );
            }
        }
        return result.iterator();
    }

    public long getLastModified() throws IOException
    {
        File f = getFile();
        return f == null ? PlexusIoResource.UNKNOWN_MODIFICATION_DATE : f.lastModified();
    }
}
