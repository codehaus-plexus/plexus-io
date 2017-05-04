package org.codehaus.plexus.components.io.attributes;

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

import org.codehaus.plexus.util.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings( { "NullableProblems" } )
public final class PlexusIoResourceAttributeUtils
{

    private PlexusIoResourceAttributeUtils()
    {
    }

    public static PlexusIoResourceAttributes mergeAttributes( PlexusIoResourceAttributes override,
                                                              PlexusIoResourceAttributes base,
                                                              PlexusIoResourceAttributes def )
    {
        if ( override == null )
        {
            return base;
        }
        SimpleResourceAttributes result;
        if ( base == null )
        {
            result = new SimpleResourceAttributes();
        }
        else
        {
            result = new SimpleResourceAttributes( base.getUserId(), base.getUserName(), base.getGroupId(),
                                                   base.getGroupName(), base.getOctalMode() );
            result.setSymbolicLink( base.isSymbolicLink() );
        }

        if ( override.getGroupId() != null && override.getGroupId() != -1 )
        {
            result.setGroupId( override.getGroupId() );
        }

        if ( def != null && def.getGroupId() >= 0 && ( result.getGroupId() == null || result.getGroupId() < 0 ) )
        {
            result.setGroupId( def.getGroupId() );
        }

        if ( override.getGroupName() != null )
        {
            result.setGroupName( override.getGroupName() );
        }

        if ( def != null && result.getGroupName() == null )
        {
            result.setGroupName( def.getGroupName() );
        }

        if ( override.getUserId() != null && override.getUserId() != -1 )
        {
            result.setUserId( override.getUserId() );
        }

        if ( def != null && def.getUserId() >= 0 && ( result.getUserId() == null || result.getUserId() < 0 ) )
        {
            result.setUserId( def.getUserId() );
        }

        if ( override.getUserName() != null )
        {
            result.setUserName( override.getUserName() );
        }

        if ( def != null && result.getUserName() == null )
        {
            result.setUserName( def.getUserName() );
        }

        if ( override.getOctalMode() > 0 )
        {
            result.setOctalMode( override.getOctalMode() );
        }

        if ( def != null && result.getOctalMode() < 0 )
        {
            result.setOctalMode( def.getOctalMode() );
        }

        return result;
    }

    public static boolean isGroupExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_EXECUTE );
    }

    public static boolean isGroupReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_READ );
    }

    public static boolean isGroupWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_WRITE );
    }

    public static boolean isOwnerExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_EXECUTE );
    }

    public static boolean isOwnerReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_READ );
    }

    public static boolean isOwnerWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_WRITE );
    }

    public static boolean isWorldExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_EXECUTE );
    }

    public static boolean isWorldReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_READ );
    }

    public static boolean isWorldWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_WRITE );
    }

    public static boolean isOctalModeEnabled( int mode, int targetMode )
    {
        return ( mode & targetMode ) != 0;
    }

    @SuppressWarnings( { "UnusedDeclaration" } )
    public static PlexusIoResourceAttributes getFileAttributes( File file )
        throws IOException
    {
        Map<String, PlexusIoResourceAttributes> byPath = getFileAttributesByPath( file, false );
        final PlexusIoResourceAttributes o = byPath.get( file.getAbsolutePath() );
        if ( o == null )
        {
            // We're on a crappy old java version (5) or the OS from hell. Just "fail".
            return SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();

        }
        return o;
    }

    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir )
        throws IOException
    {
        return getFileAttributesByPath( dir, true );
    }

    public static @Nonnull
    Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( @Nonnull File dir,
                                                                     boolean recursive )
        throws IOException
    {
        Map<Integer, String> userCache = new HashMap<>();
        Map<Integer, String> groupCache = new HashMap<>();
        final List<String> fileAndDirectoryNames;
        if ( recursive && dir.isDirectory() )
        {
            fileAndDirectoryNames = FileUtils.getFileAndDirectoryNames( dir, null, null, true, true, true, true );
        }
        else
        {
            fileAndDirectoryNames = Collections.singletonList( dir.getAbsolutePath() );
        }

        final Map<String, PlexusIoResourceAttributes> attributesByPath = new LinkedHashMap<>();

        for ( String fileAndDirectoryName : fileAndDirectoryNames )
        {
            attributesByPath.put( fileAndDirectoryName,
                                  new FileAttributes( new File( fileAndDirectoryName ), userCache, groupCache ) );
        }
        return attributesByPath;
    }

}
