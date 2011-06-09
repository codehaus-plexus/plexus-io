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

import java.util.Arrays;

public class FileAttributes
    implements PlexusIoResourceAttributes
{

    protected static final char VALUE_DISABLED_MODE = '-';

    protected static final char VALUE_WRITABLE_MODE = 'w';

    protected static final char VALUE_READABLE_MODE = 'r';

    protected static final char VALUE_EXECUTABLE_MODE = 'x';

    protected static final int INDEX_WORLD_EXECUTE = 9;

    protected static final int INDEX_WORLD_WRITE = 8;

    protected static final int INDEX_WORLD_READ = 7;

    protected static final int INDEX_GROUP_EXECUTE = 6;

    protected static final int INDEX_GROUP_WRITE = 5;

    protected static final int INDEX_GROUP_READ = 4;

    protected static final int INDEX_OWNER_EXECUTE = 3;

    protected static final int INDEX_OWNER_WRITE = 2;

    protected static final int INDEX_OWNER_READ = 1;

    private Integer groupId;

    private String groupName;

    private Integer userId;

    private String userName;

    private char[] mode;

    public FileAttributes( int userId, String userName, int groupId, String groupName, char[] mode )
    {
        this.groupId = new Integer( groupId );
        this.groupName = groupName;
        this.userId = new Integer( userId );
        this.userName = userName;
        setLsModeParts( mode );
    }

    public FileAttributes()
    {
        mode = new char[10];
        Arrays.fill( mode, VALUE_DISABLED_MODE );
    }

    protected char[] getLsModeParts()
    {
        return mode;
    }

    protected void setLsModeParts( char[] mode )
    {
        if ( mode.length < 10 )
        {
            this.mode = new char[10];
            System.arraycopy( mode, 0, this.mode, 0, mode.length );
            for ( int i = mode.length; i < 10; i++ )
            {
                this.mode[i] = VALUE_DISABLED_MODE;
            }
        }
        else
        {
            this.mode = mode;
        }
    }

    public int getGroupId()
    {
        if ( !hasGroupId() )
        {
            throw new IllegalStateException( "Cannot get the group id because it has not been set" );
        }
        return groupId.intValue();
    }

    public String getGroupName()
    {
        return groupName;
    }

    public int getUserId()
    {
        if ( !hasUserId() )
        {
            throw new IllegalStateException( "Cannot get the user id because it has not been set" );
        }

        return userId.intValue();
    }

    public boolean hasGroupId()
    {
        return groupId != null;
    }

    public boolean hasUserId()
    {
        return userId != null;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isGroupExecutable()
    {
        return checkFlag( '-', INDEX_GROUP_EXECUTE );
    }

    private boolean checkFlag( char disabledValue, int idx )
    {
        return mode == null ? false : mode[idx] != disabledValue;
    }

    public boolean isGroupReadable()
    {
        return checkFlag( '-', INDEX_GROUP_READ );
    }

    public boolean isGroupWritable()
    {
        return checkFlag( '-', INDEX_GROUP_WRITE );
    }

    public boolean isOwnerExecutable()
    {
        return checkFlag( '-', INDEX_OWNER_EXECUTE );
    }

    public boolean isOwnerReadable()
    {
        return checkFlag( '-', INDEX_OWNER_READ );
    }

    public boolean isOwnerWritable()
    {
        return checkFlag( '-', INDEX_OWNER_WRITE );
    }

    public boolean isWorldExecutable()
    {
        return checkFlag( '-', INDEX_WORLD_EXECUTE );
    }

    public boolean isWorldReadable()
    {
        return checkFlag( '-', INDEX_WORLD_READ );
    }

    public boolean isWorldWritable()
    {
        return checkFlag( '-', INDEX_WORLD_WRITE );
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "\nFile Attributes:\n------------------------------\nuser: " );
        sb.append( userName == null ? "" : userName );
        sb.append( "\ngroup: " );
        sb.append( groupName == null ? "" : groupName );
        sb.append( "\nuid: " );
        sb.append( hasUserId() ?  userId.toString() : "");
        sb.append( "\ngid: " );
        sb.append( hasGroupId() ? groupId.toString() : "");
        sb.append( "\nmode: " );
        sb.append( mode == null ? "" : String.valueOf( mode ) );

        return sb.toString();
    }

    public int getOctalMode()
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

    public PlexusIoResourceAttributes setGroupExecutable( boolean flag )
    {
        setMode( flag ? VALUE_EXECUTABLE_MODE : VALUE_DISABLED_MODE, INDEX_GROUP_EXECUTE );
        return this;
    }

    public PlexusIoResourceAttributes setGroupId( int gid )
    {
        this.groupId = new Integer( gid );
        return this;
    }

    public PlexusIoResourceAttributes setGroupName( String name )
    {
        this.groupName = name;
        return this;
    }

    public PlexusIoResourceAttributes setGroupReadable( boolean flag )
    {
        setMode( flag ? VALUE_READABLE_MODE : VALUE_DISABLED_MODE, INDEX_GROUP_READ );
        return this;
    }

    public PlexusIoResourceAttributes setGroupWritable( boolean flag )
    {
        setMode( flag ? VALUE_WRITABLE_MODE : VALUE_DISABLED_MODE, INDEX_GROUP_WRITE );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerExecutable( boolean flag )
    {
        setMode( flag ? VALUE_EXECUTABLE_MODE : VALUE_DISABLED_MODE, INDEX_OWNER_EXECUTE );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerReadable( boolean flag )
    {
        setMode( flag ? VALUE_READABLE_MODE : VALUE_DISABLED_MODE, INDEX_OWNER_READ );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerWritable( boolean flag )
    {
        setMode( flag ? VALUE_WRITABLE_MODE : VALUE_DISABLED_MODE, INDEX_OWNER_WRITE );
        return this;
    }

    public PlexusIoResourceAttributes setUserId( int uid )
    {
        this.userId = new Integer( uid );
        return this;
    }

    public PlexusIoResourceAttributes setUserName( String name )
    {
        this.userName = name;
        return this;
    }

    public PlexusIoResourceAttributes setWorldExecutable( boolean flag )
    {
        setMode( flag ? VALUE_EXECUTABLE_MODE : VALUE_DISABLED_MODE, INDEX_WORLD_EXECUTE );
        return this;
    }

    public PlexusIoResourceAttributes setWorldReadable( boolean flag )
    {
        setMode( flag ? VALUE_READABLE_MODE : VALUE_DISABLED_MODE, INDEX_WORLD_READ );
        return this;
    }

    public PlexusIoResourceAttributes setWorldWritable( boolean flag )
    {
        setMode( flag ? VALUE_WRITABLE_MODE : VALUE_DISABLED_MODE, INDEX_WORLD_WRITE );
        return this;
    }

    public PlexusIoResourceAttributes setLsModeline( String modeLine )
    {
        setLsModeParts( modeLine.toCharArray() );
        return this;
    }

    private void setMode( char value, int modeIdx )
    {
        char[] mode = getLsModeParts();
        mode[modeIdx] = value;

        setLsModeParts( mode );
    }

    public PlexusIoResourceAttributes setOctalMode( int mode )
    {
        setGroupExecutable( PlexusIoResourceAttributeUtils.isGroupExecutableInOctal( mode ) );
        setGroupReadable( PlexusIoResourceAttributeUtils.isGroupReadableInOctal( mode ) );
        setGroupWritable( PlexusIoResourceAttributeUtils.isGroupWritableInOctal( mode ) );
        setOwnerExecutable( PlexusIoResourceAttributeUtils.isOwnerExecutableInOctal( mode ) );
        setOwnerReadable( PlexusIoResourceAttributeUtils.isOwnerReadableInOctal( mode ) );
        setOwnerWritable( PlexusIoResourceAttributeUtils.isOwnerWritableInOctal( mode ) );
        setWorldExecutable( PlexusIoResourceAttributeUtils.isWorldExecutableInOctal( mode ) );
        setWorldReadable( PlexusIoResourceAttributeUtils.isWorldReadableInOctal( mode ) );
        setWorldWritable( PlexusIoResourceAttributeUtils.isWorldWritableInOctal( mode ) );
        return this;
    }

    public PlexusIoResourceAttributes setOctalModeString( String mode )
    {
        setOctalMode( Integer.parseInt( mode, 8 ) );
        return this;
    }
}