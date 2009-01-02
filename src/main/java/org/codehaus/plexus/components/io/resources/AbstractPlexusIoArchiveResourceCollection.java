package org.codehaus.plexus.components.io.resources;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.logging.Logger;



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

    protected AbstractPlexusIoArchiveResourceCollection()
    {
    }
    
    protected AbstractPlexusIoArchiveResourceCollection( Logger logger )
    {
        super( logger );
    }
    
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