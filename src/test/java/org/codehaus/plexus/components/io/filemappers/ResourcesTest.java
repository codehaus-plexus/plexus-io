package org.codehaus.plexus.components.io.filemappers;

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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.codehaus.plexus.components.io.resources.AbstractPlexusIoArchiveResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResource;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Test case for resource collections.
 */
public class ResourcesTest extends TestSupport
{
    private static final String X_PATH = "x";
    private static final String A_PATH = X_PATH + "/a";
    private static final String B_PATH = X_PATH + "/b";
    private static final String Y_PATH = "y";

    private File getTestDir()
    {
        final String testDirPath = System.getProperty( "plexus.io.testDirPath" );
        return new File( testDirPath == null ? "target/plexus.io.testDir" : testDirPath );
    }

    private File getFilesDir()
    {
        return new File( getTestDir(), "files" );
    }

    private void createFiles()
        throws IOException
    {
        final File baseDir = getFilesDir();
        FileUtils.deleteDirectory( baseDir );
        FileUtils.mkdir( baseDir.getPath() );
        final File aFile = new File( baseDir, A_PATH );
        FileUtils.mkdir( aFile.getParentFile().getPath() );
        FileOutputStream fos = new FileOutputStream( aFile );
        fos.write( "0123456789".getBytes( "US-ASCII" ) );
        fos.close();
        final File bFile = new File( baseDir, B_PATH );
        fos = new FileOutputStream( bFile );
        fos.write( "abcdefghijklmnopqrstuvwxyz".getBytes( "US-ASCII" ) );
        fos.close();
        final File yDir = new File( baseDir, Y_PATH );
        FileUtils.mkdir( yDir.getPath() );
    }

    private void addDirToZipFile( ZipOutputStream zos, File dir, String path )
        throws IOException
    {
        final String prefix = path == null ? "" : ( path + "/" );
        File[] files = dir.listFiles();
        for ( File f : files )
        {
            final String entryName = prefix + f.getName();
            ZipEntry ze = new ZipEntry( entryName );
            if ( f.isFile() )
            {
                ze.setSize( f.length() );
                zos.putNextEntry( ze );
                FileInputStream fis = new FileInputStream( f );
                byte[] buffer = new byte[1024];
                for ( ;; )
                {
                    int res = fis.read( buffer );
                    if ( res == -1 )
                    {
                        break;
                    }
                    if ( res > 0 )
                    {
                        zos.write( buffer, 0, res );
                    }
                }
                fis.close();
                ze.setTime( f.lastModified() );
                zos.closeEntry();
            }
            else
            {
                addDirToZipFile( zos, f, entryName );
            }
        }
    }

    private void createZipFile( File dest, File dir ) throws IOException
    {
        FileOutputStream fos = new FileOutputStream( dest );
        ZipOutputStream zos = new ZipOutputStream( fos );
        addDirToZipFile( zos, dir, null );
        zos.close();
    }

    private void compare( InputStream in, File file )
        throws IOException
    {
        try( InputStream fIn = new FileInputStream( file ) )
        {
            for ( ;; )
            {
                int i1 = in.read();
                int i2 = fIn.read();
                assertEquals( i2, i1 );
                if ( i1 == -1 )
                {
                    break;
                }
            }
        }
    }

    private void compare( PlexusIoResource res, File file )
        throws IOException
    {
        assertTrue( res.getLastModified() != PlexusIoResource.UNKNOWN_MODIFICATION_DATE );
        if ( res instanceof PlexusIoFileResource )
        {
            assertEquals( res.getLastModified() / 1000, file.lastModified() / 1000 );
        }
        assertTrue( res.getSize() != PlexusIoResource.UNKNOWN_RESOURCE_SIZE );
        assertEquals( res.getSize(), file.length() );
        InputStream in = res.getContents();
        compare( in, file );
        in.close();
        URLConnection uc = res.getURL().openConnection();
        uc.setUseCaches( false );
        in = uc.getInputStream();
        compare( in, file );
        in.close();
    }

    private void testPlexusIoResourceCollection( PlexusIoResourceCollection plexusIoResourceCollection )
        throws IOException
    {
        boolean xPathSeen = false;
        boolean yPathSeen = false;
        boolean aFileSeen = false;
        boolean bFileSeen = false;
        Iterator iter = plexusIoResourceCollection.getResources();
        while ( iter.hasNext() )
        {
            PlexusIoResource res = (PlexusIoResource) iter.next();
            final String resName = res.getName().replace( File.separatorChar, '/' );
            if ( res.isDirectory() )
            {
                assertFalse( "The directory " + resName + " is a file.", res.isFile() );
                if ( X_PATH.equals( resName ) )
                {
                    xPathSeen = true;
                }
                else if ( Y_PATH.equals( resName ) )
                {
                    yPathSeen = true;
                }
                else if ( "".equals( resName ) || ".".equals( resName ) )
                {
                    // Ignore me
                }
                else
                {
                    fail( "Unexpected directory entry: " + resName );
                }
                final File dir = new File( getFilesDir(), resName );
                assertTrue( "The directory " + dir + " doesn't exist.", dir.isDirectory() );
            }
            else
            {
                assertTrue( "The file " + resName + " isn't reported to be a file.", res.isFile() );
                assertTrue( "The file " + resName + " doesn't exist.", res.isExisting() );
                final File f = new File( getFilesDir(), resName );
                assertTrue( "A file " + f + " doesn't exist.", f.isFile() && f.exists() );
                if ( A_PATH.equals( resName ) )
                {
                    aFileSeen = true;
                }
                else if ( B_PATH.equals( resName ) )
                {
                    bFileSeen = true;
                }
                else
                {
                    fail( "Unexpected file entry: " + resName );
                }
                compare( res, f );
            }
        }

        assertTrue( aFileSeen );
        assertTrue( bFileSeen );
        if ( iter instanceof Closeable )
        {
            ( (Closeable) iter ).close();
        }
    }

    private void testFileResourceCollection( PlexusIoFileResourceCollection resourceCollection )
        throws IOException
    {
        resourceCollection.setBaseDir( getFilesDir() );
        testPlexusIoResourceCollection( resourceCollection );
    }

    @Test
    public void testFileCollection() throws Exception
    {
        createFiles();
        testFileResourceCollection( (PlexusIoFileResourceCollection) lookup( PlexusIoResourceCollection.class ) );
        testFileResourceCollection( (PlexusIoFileResourceCollection) lookup( PlexusIoResourceCollection.class,
                                                                             PlexusIoFileResourceCollection.ROLE_HINT ) );
    }

    private void testZipFileCollection( AbstractPlexusIoArchiveResourceCollection resourceCollection, File zipFile )
        throws IOException
    {
        resourceCollection.setFile( zipFile );
        testPlexusIoResourceCollection( resourceCollection );
    }
}