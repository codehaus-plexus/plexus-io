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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class PlexusIoURLResource
    extends AbstractPlexusIoResource
{
    protected PlexusIoURLResource( @Nonnull String name, long lastModified, long size, boolean isFile, boolean isDirectory,
                                   boolean isExisting )
    {
        super( name, lastModified, size, isFile, isDirectory, isExisting );
    }

    @Nonnull
    public InputStream getContents()
        throws IOException
    {
        final URL url = getURL();
        try
        {
            return url.openStream();
        }
        catch ( IOException e )
        {
            throw new IOException( getDescriptionForError( url ), e );
        }
    }

    String getDescriptionForError( URL url )
    {
        return url != null ? url.toExternalForm() : "url=null";
    }

    public abstract URL getURL() throws IOException;
}