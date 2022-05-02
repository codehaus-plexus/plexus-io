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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.attribute.FileTime;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.codehaus.plexus.components.io.attributes.AttributeUtils;
import org.codehaus.plexus.components.io.attributes.FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.FileSupplier;
import org.codehaus.plexus.components.io.functions.InputStreamTransformer;
import org.codehaus.plexus.components.io.functions.ResourceAttributeSupplier;

import javax.annotation.Nonnull;

/**
 * Implementation of {@link PlexusIoResource} for files.
 */
public class PlexusIoFileResource
    extends AbstractPlexusIoResource
    implements ResourceAttributeSupplier, FileSupplier

{
    @Nonnull
    private final File file;

    @Nonnull
    private final PlexusIoResourceAttributes attributes;

    private final ContentSupplier contentSupplier;

    private final DeferredFileOutputStream dfos;

    protected PlexusIoFileResource( @Nonnull File file, @Nonnull String name,
                                    @Nonnull PlexusIoResourceAttributes attrs )
        throws IOException
    {
        this( file, name, attrs, null, null );
    }

    @SuppressWarnings( "ConstantConditions" )
    PlexusIoFileResource( @Nonnull final File file, @Nonnull String name, @Nonnull PlexusIoResourceAttributes attrs,
                          final ContentSupplier contentSupplier, final InputStreamTransformer streamTransformer )
        throws IOException
    {
        super( name, file.lastModified(), file.length(), file.isFile(), file.isDirectory(), file.exists() );
        this.file = file;

        this.contentSupplier = contentSupplier != null ? contentSupplier : getRootContentSupplier( file );

        boolean hasTransformer = streamTransformer != null && streamTransformer != identityTransformer;
        InputStreamTransformer transToUse = streamTransformer != null ? streamTransformer : identityTransformer;

        dfos = hasTransformer && file.isFile() ? asDeferredStream( this.contentSupplier, transToUse, this ) : null;
        if ( attrs == null )
            throw new IllegalArgumentException( "attrs is null for file " + file.getName() );
        this.attributes = attrs;
    }

    private static DeferredFileOutputStream asDeferredStream( @Nonnull ContentSupplier supplier,
                                                              @Nonnull InputStreamTransformer transToUse,
                                                              PlexusIoResource resource )
        throws IOException
    {
        DeferredFileOutputStream dfos = new DeferredFileOutputStream( 5000000, "p-archiver", null, null );
        InputStream inputStream = supplier.getContents();
        InputStream transformed = transToUse.transform( resource, inputStream );
        IOUtils.copy( transformed, dfos );
        IOUtils.closeQuietly( inputStream );
        IOUtils.closeQuietly( transformed );
        return dfos;
    }

    private static ContentSupplier getRootContentSupplier( final File file )
    {
        return new ContentSupplier()
        {
            public InputStream getContents()
                throws IOException
            {
                return new FileInputStream( file );
            }
        };
    }

    public static String getName( File file )
    {
        return file.getPath().replace( '\\', '/' );
    }

    /**
     * Returns the resource file.
     */
    @Nonnull
    public File getFile()
    {
        return file;
    }

    @Nonnull
    public InputStream getContents()
        throws IOException
    {
        if ( dfos == null )
        {
            return contentSupplier.getContents();
        }
        if ( dfos.isInMemory() )
        {
            return new ByteArrayInputStream( dfos.getData() );
        }
        else
        {
            return new FileInputStream( dfos.getFile() )
            {
                @SuppressWarnings( "ResultOfMethodCallIgnored" )
                @Override
                public void close()
                    throws IOException
                {
                    super.close();
                    dfos.getFile().delete();
                }
            };
        }
    }

    @Nonnull
    public URL getURL()
        throws IOException
    {
        return getFile().toURI().toURL();
    }

    public long getSize()
    {
        if ( dfos == null )
        {
            if ( attributes instanceof FileAttributes)
            {
                return ( ( FileAttributes ) attributes ).getSize();
            }
            return getFile().length();
        }
        if ( dfos.isInMemory() )
        {
            return dfos.getByteCount();
        }
        else
        {
            return dfos.getFile().length();
        }
    }

    public boolean isDirectory()
    {
        if ( attributes instanceof FileAttributes )
        {
            return ( ( FileAttributes ) attributes ).isDirectory();
        }
        return getFile().isDirectory();
    }

    public boolean isExisting()
    {
        if ( attributes instanceof FileAttributes )
        {
            return true;
        }
        return getFile().exists();
    }

    public boolean isFile()
    {
        if ( attributes instanceof FileAttributes )
        {
            return ( ( FileAttributes ) attributes ).isRegularFile();
        }
        return getFile().isFile();
    }

    @Nonnull
    public PlexusIoResourceAttributes getAttributes()
    {
        return attributes;
    }

    public long getLastModified()
    {
        if ( attributes instanceof FileAttributes )
        {
            FileTime lastModified = ( ( FileAttributes ) attributes ).getLastModifiedTime();
            if ( lastModified != null )
            {
                return lastModified.toMillis();
            }
        }
        return AttributeUtils.getLastModified( getFile() );
    }

    @Override
    public boolean isSymbolicLink()
    {
        return getAttributes().isSymbolicLink();
    }

    private static final InputStreamTransformer identityTransformer = AbstractPlexusIoResourceCollection.identityTransformer;
}