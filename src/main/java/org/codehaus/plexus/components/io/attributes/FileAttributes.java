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
import java.util.Arrays;

import static org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributeUtils.*;

/**
 * Attributes that are read by legacy-style attribute parsing.
 */
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

    public FileAttributes(int mode){
        this.mode = new char[10];
        Arrays.fill( this.mode, VALUE_DISABLED_MODE );
        setOctalMode( mode );
    }

    public FileAttributes(@Nonnull String lsMOdeLine)
    {
        mode = new char[10];
        Arrays.fill( mode, VALUE_DISABLED_MODE );
        setLsModeline( lsMOdeLine );
    }

    protected char[] getLsModeParts()
    {
        return mode;
    }

    private void setLsModeParts( @Nonnull char[] mode )
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

    @Nullable public Integer getGroupId()
    {
        return groupId;
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
        return checkFlag( '-', INDEX_GROUP_EXECUTE );
    }

    private boolean checkFlag( char disabledValue, int idx )
    {
        return mode != null && mode[idx] != disabledValue;
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
        StringBuilder sb = new StringBuilder();
        sb.append( "\nFile Attributes:\n------------------------------\nuser: " );
        sb.append( userName == null ? "" : userName );
        sb.append( "\ngroup: " );
        sb.append( groupName == null ? "" : groupName );
        sb.append( "\nuid: " );
        sb.append( userId != null ?  userId.toString() : "");
        sb.append( "\ngid: " );
        sb.append( groupId != null ? groupId.toString() : "");
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

    public PlexusIoResourceAttributes setGroupId( Integer gid )
    {
        this.groupId = gid;
        return this;
    }

    public PlexusIoResourceAttributes setGroupName( String name )
    {
        this.groupName = name;
        return this;
    }

    public PlexusIoResourceAttributes setUserId( Integer uid )
    {
        this.userId = uid;
        return this;
    }

    public PlexusIoResourceAttributes setUserName( String name )
    {
        this.userName = name;
        return this;
    }

    public PlexusIoResourceAttributes setLsModeline( @Nonnull String modeLine )
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

    private void setOctalMode( int mode )
    {
        setExecutable( INDEX_OWNER_EXECUTE, isOwnerExecutableInOctal( mode ));
        setExecutable( INDEX_GROUP_EXECUTE, isGroupExecutableInOctal( mode ) );
        setExecutable( INDEX_WORLD_EXECUTE, isWorldExecutableInOctal( mode ));

        setReadable( INDEX_OWNER_READ, isOwnerReadableInOctal( mode ));
        setReadable( INDEX_GROUP_READ, isGroupReadableInOctal( mode ) );
        setReadable( INDEX_WORLD_READ, isWorldReadableInOctal( mode ));

        setWriteable( INDEX_OWNER_WRITE, isOwnerWritableInOctal( mode ));
        setWriteable( INDEX_GROUP_WRITE, isGroupWritableInOctal( mode ) );
        setWriteable( INDEX_WORLD_WRITE, isWorldWritableInOctal( mode ));
    }

    private void setExecutable( int index, boolean executable )
    {
        setMode( executable ? VALUE_EXECUTABLE_MODE : VALUE_DISABLED_MODE, index );
    }

    private void setReadable( int index, boolean readable )
    {
        setMode( readable ? VALUE_READABLE_MODE : VALUE_DISABLED_MODE, index );
    }
    private void setWriteable( int index, boolean writable )
    {
        setMode( writable ? VALUE_WRITABLE_MODE : VALUE_DISABLED_MODE, index );
    }
    // Legacy mode does not support symlinks
    public boolean isSymbolicLink()
    {
        return false;
    }
}