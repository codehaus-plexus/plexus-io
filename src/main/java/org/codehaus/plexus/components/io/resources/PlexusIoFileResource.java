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

import org.codehaus.plexus.components.io.attributes.Java7AttributeUtils;
import org.codehaus.plexus.components.io.attributes.Java7Reflector;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

/**
 * Implementation of {@link PlexusIoResource} for files.
 */
public class PlexusIoFileResource
    extends AbstractPlexusIoResource
    implements PlexusIoResourceWithAttributes
{
    private final File file;

    private final PlexusIoResourceAttributes attributes;

    /**
     * Creates a new instance. This constructor is usually used with a directory
     */
    public PlexusIoFileResource( File file )
    {
        this( file, getName( file ) );
    }

    public PlexusIoFileResource( File file, String name )
    {
        this( file, name, null );
    }

    public PlexusIoFileResource( File file, PlexusIoResourceAttributes attrs )
    {
        this( file, getName( file ), attrs );
    }

    public PlexusIoFileResource( File file, String name, PlexusIoResourceAttributes attrs )
    {
        super( name, file.lastModified(), file.length(), file.isFile(), file.isDirectory(), file.exists() );
        this.file = file;
        this.attributes = attrs;
    }

    private static String getName( File file )
    {
        return file.getPath().replace( '\\', '/' );
    }

    public static PlexusIoFileResource readFromDisk( File file, String name, PlexusIoResourceAttributes attrs )
    {
        return new PlexusIoFileResource( file, name, attrs );
    }

    public static PlexusIoFileResource existingFile( File file, PlexusIoResourceAttributes attrs )
    {
        return new PlexusIoFileResource( file, getName( file ), attrs );
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

    public long getLastModified()
    {
        if ( Java7Reflector.isJava7() )
        {
            return Java7AttributeUtils.getLastModified( getFile() );
        }
        else
        {
            return getFile().lastModified();
        }
    }

    public long getSize()
    {
        return getFile().length();
    }

    public boolean isDirectory()
    {
        return getFile().isDirectory();
    }

    public boolean isExisting()
    {
        return getFile().exists();
    }

    public boolean isFile()
    {
        return getFile().isFile();
    }


    public PlexusIoResourceAttributes getAttributes()
    {
        return attributes;
    }
}