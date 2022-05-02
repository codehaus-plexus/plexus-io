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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
 * File attributes
 * Immutable
 */
public class FileAttributes
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

    public FileAttributes( @Nonnull File file, @Nonnull Map<Integer, String> userCache,
                           @Nonnull Map<Integer, String> groupCache )
        throws IOException
    {

        Path path = file.toPath();
        if ( AttributeUtils.isUnix( path ) )
        {
            Map<String, Object> attrs = Files.readAttributes( path, "unix:permissions,gid,uid,isSymbolicLink,mode", LinkOption.NOFOLLOW_LINKS );
            this.permissions = (Set<PosixFilePermission>) attrs.get( "permissions" );

            groupId = (Integer) attrs.get( "gid" );

            String groupName = groupCache.get( groupId );
            if ( groupName != null )
            {
                this.groupName = groupName;
            }
            else
            {
                Object group = Files.getAttribute( path, "unix:group", LinkOption.NOFOLLOW_LINKS );
                this.groupName = ( (Principal) group ).getName();
                groupCache.put( groupId, this.groupName );
            }
            userId = (Integer) attrs.get( "uid" );
            String userName = userCache.get( userId );
            if ( userName != null )
            {
                this.userName = userName;
            }
            else
            {
                Object owner = Files.getAttribute( path, "unix:owner", LinkOption.NOFOLLOW_LINKS );
                this.userName = ( (Principal) owner ).getName();
                userCache.put( userId, this.userName );
            }
            octalMode = (Integer) attrs.get( "mode" ) & 0xfff; // Mask off top bits for compatibilty. Maybe check if we
                                                               // can skip this
            symbolicLink = (Boolean) attrs.get( "isSymbolicLink" );
        }
        else
        {
            FileOwnerAttributeView fa = AttributeUtils.getFileOwnershipInfo( file );
            this.userName = fa.getOwner().getName();
            userId = null;
            this.groupName = null;
            this.groupId = null;
            octalMode = PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE;
            permissions = Collections.emptySet();
            symbolicLink = Files.isSymbolicLink( path );
        }

    }

    public static @Nonnull
    PlexusIoResourceAttributes uncached( @Nonnull File file )
        throws IOException
    {
        return new FileAttributes( file, new HashMap<Integer, String>(), new HashMap<Integer, String>() );
    }

    @Override
    @Nullable
    public Integer getGroupId()
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

    @Override
    @Nullable
    public String getGroupName()
    {
        return groupName;
    }

    @Override
    public Integer getUserId()
    {
        return userId;
    }

    @Override
    public String getUserName()
    {
        return userName;
    }

    @Override
    public boolean isGroupExecutable()
    {
        return containsPermission( PosixFilePermission.GROUP_EXECUTE );
    }

    private boolean containsPermission( PosixFilePermission groupExecute )
    {
        return permissions.contains( groupExecute );
    }

    @Override
    public boolean isGroupReadable()
    {
        return containsPermission( PosixFilePermission.GROUP_READ );
    }

    @Override
    public boolean isGroupWritable()
    {
        return containsPermission( PosixFilePermission.GROUP_WRITE );
    }

    @Override
    public boolean isOwnerExecutable()
    {
        return containsPermission( PosixFilePermission.OWNER_EXECUTE );
    }

    @Override
    public boolean isOwnerReadable()
    {
        return containsPermission( PosixFilePermission.OWNER_READ );
    }

    @Override
    public boolean isOwnerWritable()
    {
        return containsPermission( PosixFilePermission.OWNER_WRITE );
    }

    @Override
    public boolean isWorldExecutable()
    {
        return containsPermission( PosixFilePermission.OTHERS_EXECUTE );

    }

    @Override
    public boolean isWorldReadable()
    {
        return containsPermission( PosixFilePermission.OTHERS_READ );
    }

    @Override
    public boolean isWorldWritable()
    {
        return containsPermission( PosixFilePermission.OTHERS_WRITE );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( 128 );
        sb.append( System.lineSeparator() );
        sb.append( "File Attributes:" );
        sb.append( System.lineSeparator() );
        sb.append( "------------------------------" );
        sb.append( System.lineSeparator() );
        sb.append( "user: " );
        sb.append( userName == null ? "" : userName );
        sb.append( System.lineSeparator() );
        sb.append( "group: " );
        sb.append( groupName == null ? "" : groupName );
        sb.append( System.lineSeparator() );
        sb.append( "uid: " );
        sb.append( hasUserId() ? Integer.toString( userId ) : "" );
        sb.append( System.lineSeparator() );
        sb.append( "gid: " );
        sb.append( hasGroupId() ? Integer.toString( groupId ) : "" );

        return sb.toString();
    }

    @Override
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

    @Override
    public boolean isSymbolicLink()
    {
        return symbolicLink;
    }
}