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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

/**
 * Implementation of {@link PlexusIoResource} for files.
 */
public class PlexusIoFileResource
    extends AbstractPlexusIoResourceWithAttributes
    implements PlexusIoResourceWithAttributes
{
    private File file;

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource()
    {
        // Does nothing
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file )
    {
        this( file, file.getPath().replace( '\\', '/' ) );
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file, PlexusIoResourceAttributes attrs )
    {
        this( file, file.getPath().replace( '\\', '/' ), attrs );
    }

    /**
     * Creates a new instance.
     */
    public PlexusIoFileResource( File file, String name )
    {
        setFile( file );
        setName( name );
    }

    public PlexusIoFileResource( File file, String name, PlexusIoResourceAttributes attrs )
    {
        setName( name );
        setAttributes( attrs );
        setFile( file );
    }

    /**
     * Sets the resources file.
     */
    public void setFile( File file )
    {
        this.file = file;
        setLastModified( file.lastModified() );
        setSize( file.length() );
        setFile( file.isFile() );
        setDirectory( file.isDirectory() );
        setExisting( file.exists() );
    }

    /**
     * Returns the resources file.
     */
    public File getFile()
    {
        return file;
    }

    public InputStream getContents()
        throws IOException
    {
        return new FileInputStream( getFile() );
    }

    public URL getURL()
        throws IOException
    {
        return getFile().toURI().toURL();
    }

}