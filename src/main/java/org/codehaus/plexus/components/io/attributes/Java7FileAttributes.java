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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * File attributes for a java7 file that are backed on disk by a file.
 * Immutable
 */
public class Java7FileAttributes
    implements PlexusIoResourceAttributes
{
    @Nullable
    private final Integer groupId;

    @Nullable
    private final String groupName;

    @Nullable
    private final Integer userId;

    private final String userName;

    private final boolean symbolicLink;

    private final int octalMode;

    private final Set<PosixFilePermission> permissions;

    public Java7FileAttributes( @Nonnull File file,  @Nonnull Map<Integer, String> userCache,
                                @Nonnull Map<Integer, String> groupCache )
        throws IOException
    {

        BasicFileAttributes basicFileAttributes = Java7AttributeUtils.getFileAttributes( file );

        if ( basicFileAttributes instanceof PosixFileAttributes )
        {
            this.permissions = ( (PosixFileAttributes) basicFileAttributes ).permissions();
            groupId = (Integer) Files.readAttributes( file.toPath(), "unix:gid" ).get( "gid" );

            String groupName = groupCache.get( groupId );
            if ( groupName != null )
            {
                this.groupName = groupName;
            }
            else
            {
                this.groupName = ( (PosixFileAttributes) basicFileAttributes ).group().getName();
                groupCache.put( groupId, this.groupName );
            }
            userId = (Integer) Files.readAttributes( file.toPath(), "unix:uid" ).get( "uid" );
            String userName = userCache.get( userId );
            if ( userName != null )
            {
                this.userName = userName;
            }
            else
            {
                this.userName = ( (PosixFileAttributes) basicFileAttributes ).owner().getName();
                userCache.put( userId, this.userName );
            }
            octalMode = calculatePosixOctalMode();
        } else {
            FileOwnerAttributeView fa = Java7AttributeUtils.getFileOwnershipInfo( file );
            this.userName = fa.getOwner().getName();
            userId = null;
            this.groupName = null;
            this.groupId = null;
            octalMode = -1;
            permissions = Collections.emptySet();
        }

        symbolicLink = basicFileAttributes.isSymbolicLink();
    }

    public static  @Nonnull PlexusIoResourceAttributes uncached(  @Nonnull File file )
        throws IOException
    {
        return new Java7FileAttributes( file, new HashMap<Integer, String>(), new HashMap<Integer, String>() );
    }


    @Nullable public Integer getGroupId()
    {

        return groupId;
    }

    public boolean hasGroupId()
    {
        return false;
    }

    public boolean hasUserId()
    {
        return false;
    }

    @Nullable public String getGroupName()
    {
        return groupName;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isGroupExecutable()
    {
        return containsPermission( PosixFilePermission.GROUP_EXECUTE );
    }

    private boolean containsPermission( PosixFilePermission groupExecute )
    {
        return permissions.contains( groupExecute );
    }

    public boolean isGroupReadable()
    {
        return containsPermission( PosixFilePermission.GROUP_READ );
    }

    public boolean isGroupWritable()
    {
        return containsPermission( PosixFilePermission.GROUP_WRITE );
    }

    public boolean isOwnerExecutable()
    {
        return containsPermission( PosixFilePermission.OWNER_EXECUTE );
    }

    public boolean isOwnerReadable()
    {
        return containsPermission( PosixFilePermission.OWNER_READ );
    }

    public boolean isOwnerWritable()
    {
        return containsPermission( PosixFilePermission.OWNER_WRITE );
    }

    public boolean isWorldExecutable()
    {
        return containsPermission( PosixFilePermission.OTHERS_EXECUTE );

    }

    public boolean isWorldReadable()
    {
        return containsPermission( PosixFilePermission.OTHERS_READ );
    }

    public boolean isWorldWritable()
    {
        return containsPermission( PosixFilePermission.OTHERS_WRITE );
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "\nFile Attributes:\n------------------------------\nuser: " );
        sb.append( userName == null ? "" : userName );
        sb.append( "\ngroup: " );
        sb.append( groupName == null ? "" : groupName );
        sb.append( "\nuid: " );
        sb.append( hasUserId() ? Integer.toString( userId ) : "" );
        sb.append( "\ngid: " );
        sb.append( hasGroupId() ? Integer.toString( groupId ) : "" );

        return sb.toString();
    }

    public int getOctalMode()
    {
        return octalMode;
    }

    public int calculatePosixOctalMode()
    {
        int result = 0;

        if ( isOwnerReadable() )
        {
            result |= AttributeConstants.OCTAL_OWNER_READ;
        }

        if ( isOwnerWritable() )
        {
            result |= AttributeConstants.OCTAL_OWNER_WRITE;
        }

        if ( isOwnerExecutable() )
        {
            result |= AttributeConstants.OCTAL_OWNER_EXECUTE;
        }

        if ( isGroupReadable() )
        {
            result |= AttributeConstants.OCTAL_GROUP_READ;
        }

        if ( isGroupWritable() )
        {
            result |= AttributeConstants.OCTAL_GROUP_WRITE;
        }

        if ( isGroupExecutable() )
        {
            result |= AttributeConstants.OCTAL_GROUP_EXECUTE;
        }

        if ( isWorldReadable() )
        {
            result |= AttributeConstants.OCTAL_WORLD_READ;
        }

        if ( isWorldWritable() )
        {
            result |= AttributeConstants.OCTAL_WORLD_WRITE;
        }

        if ( isWorldExecutable() )
        {
            result |= AttributeConstants.OCTAL_WORLD_EXECUTE;
        }

        return result;
    }

    public String getOctalModeString()
    {
        return Integer.toString( getOctalMode(), 8 );
    }

    public boolean isSymbolicLink()
    {
        return symbolicLink;
    }
}