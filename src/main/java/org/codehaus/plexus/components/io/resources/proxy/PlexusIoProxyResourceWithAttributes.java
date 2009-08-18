package org.codehaus.plexus.components.io.resources.proxy;

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

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoResourceWithAttributes;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceWithAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlexusIoProxyResourceWithAttributes
    extends AbstractPlexusIoResourceWithAttributes
{

    private final PlexusIoResourceWithAttributes src;

    public PlexusIoProxyResourceWithAttributes( PlexusIoResourceWithAttributes plexusIoResource )
    {
        this.src = plexusIoResource;
        setName( src.getName() );
        setAttributes( src.getAttributes() );
    }

    public long getLastModified()
    {
        return src.getLastModified();
    }

    public long getSize()
    {
        return src.getSize();
    }

    public boolean isDirectory()
    {
        return src.isDirectory();
    }

    public boolean isExisting()
    {
        return src.isExisting();
    }

    public boolean isFile()
    {
        return src.isFile();
    }

    public URL getURL()
        throws IOException
    {
        return src.getURL();
    }

    public InputStream getContents()
        throws IOException
    {
        return src.getContents();
    }

}
