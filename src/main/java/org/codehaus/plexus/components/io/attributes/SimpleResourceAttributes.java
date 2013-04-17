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

public class SimpleResourceAttributes
    implements PlexusIoResourceAttributes
{

    private Integer gid;

    private Integer uid;

    private String userName;

    private String groupName;

    private int mode;

    public SimpleResourceAttributes( Integer uid, String userName, Integer gid, String groupName, int mode )
    {
        this.uid = uid;
        this.userName = userName;
        this.gid = gid;
        this.groupName = groupName;
        this.mode = mode;
    }

    public SimpleResourceAttributes()
    {
    }

    public int getOctalMode()
    {
        return mode;
    }

    public Integer getGroupId()
    {
        return gid;
    }

    public String getGroupName()
    {
        return groupName;
    }


    public Integer getUserId()
    {
        return uid;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isGroupExecutable()
    {
        return PlexusIoResourceAttributeUtils.isGroupExecutableInOctal( mode );
    }

    public boolean isGroupReadable()
    {
        return PlexusIoResourceAttributeUtils.isGroupReadableInOctal( mode );
    }

    public boolean isGroupWritable()
    {
        return PlexusIoResourceAttributeUtils.isGroupWritableInOctal( mode );
    }

    public boolean isOwnerExecutable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerExecutableInOctal( mode );
    }

    public boolean isOwnerReadable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerReadableInOctal( mode );
    }

    public boolean isOwnerWritable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerWritableInOctal( mode );
    }

    public boolean isWorldExecutable()
    {
        return PlexusIoResourceAttributeUtils.isWorldExecutableInOctal( mode );
    }

    public boolean isWorldReadable()
    {
        return PlexusIoResourceAttributeUtils.isWorldReadableInOctal( mode );
    }

    public boolean isWorldWritable()
    {
        return PlexusIoResourceAttributeUtils.isWorldWritableInOctal( mode );
    }

    public String getOctalModeString()
    {
        return Integer.toString( mode, 8 );
    }

    public PlexusIoResourceAttributes setOctalMode( int mode )
    {
        this.mode = mode;
        return this;
    }

    public PlexusIoResourceAttributes setGroupExecutable( boolean flag )
    {
        set( AttributeConstants.OCTAL_GROUP_EXECUTE, flag );
        return this;
    }

    public PlexusIoResourceAttributes setGroupId( Integer gid )
    {
        this.gid = gid;
        return this;
    }

    public PlexusIoResourceAttributes setGroupName( String name )
    {
        this.groupName = name;
        return this;
    }

    public PlexusIoResourceAttributes setGroupReadable( boolean flag )
    {
        set( AttributeConstants.OCTAL_GROUP_READ, flag );
        return this;
    }

    public PlexusIoResourceAttributes setGroupWritable( boolean flag )
    {
        set( AttributeConstants.OCTAL_GROUP_WRITE, flag );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerExecutable( boolean flag )
    {
        set( AttributeConstants.OCTAL_OWNER_EXECUTE, flag );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerReadable( boolean flag )
    {
        set( AttributeConstants.OCTAL_OWNER_READ, flag );
        return this;
    }

    public PlexusIoResourceAttributes setOwnerWritable( boolean flag )
    {
        set( AttributeConstants.OCTAL_OWNER_WRITE, flag );
        return this;
    }

    public PlexusIoResourceAttributes setUserId( Integer uid )
    {
        this.uid = uid;
        return this;
    }

    public PlexusIoResourceAttributes setUserName( String name )
    {
        this.userName = name;
        return this;
    }

    public PlexusIoResourceAttributes setWorldExecutable( boolean flag )
    {
        set( AttributeConstants.OCTAL_WORLD_EXECUTE, flag );
        return this;
    }

    public PlexusIoResourceAttributes setWorldReadable( boolean flag )
    {
        set( AttributeConstants.OCTAL_WORLD_READ, flag );
        return this;
    }

    public PlexusIoResourceAttributes setWorldWritable( boolean flag )
    {
        set( AttributeConstants.OCTAL_WORLD_WRITE, flag );
        return this;
    }

    private void set( int bit, boolean enabled )
    {
        if ( enabled )
        {
            mode |= bit;
        }
        else
        {
            mode &= ~bit;
        }
    }

    public PlexusIoResourceAttributes setOctalModeString( String mode )
    {
        setOctalMode( Integer.parseInt( mode, 8 ) );
        return this;
    }

    public String toString()
    {
        return String.format(
            "\nResource Attributes:\n------------------------------\nuser: %s\ngroup: %s\nuid: %d\ngid: %d\nmode: %06o",
            userName == null ? "" : userName,
            groupName == null ? "" : groupName,
            uid != null ? uid : 0,
            gid != null ? gid : 0,
            mode );
    }
}
