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

import javax.annotation.Nullable;

/*
 * A very simple pojo based PlexusIoResourceAttributes without any kind of backing
 */
public class SimpleResourceAttributes
    implements PlexusIoResourceAttributes
{

    private Integer gid;

    private Integer uid;

    private String userName;

    private String groupName;

    private int mode = PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE;

    private boolean isSymbolicLink;

    public SimpleResourceAttributes( Integer uid, String userName, Integer gid, String groupName, int mode )
    {
        this.uid = uid;
        this.userName = userName;
        this.gid = gid;
        this.groupName = groupName;
        this.mode = mode;
    }

    public static PlexusIoResourceAttributes lastResortDummyAttributesForBrokenOS()
    {
        return new SimpleResourceAttributes();
    }

    SimpleResourceAttributes()
    {
    }

    @Override
    public int getOctalMode()
    {
        return mode;
    }

    @Override
    @Nullable
    public Integer getGroupId()
    {
        return gid;
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
        return uid;
    }

    @Override
    public String getUserName()
    {
        return userName;
    }

    @Override
    public boolean isGroupExecutable()
    {
        return PlexusIoResourceAttributeUtils.isGroupExecutableInOctal( mode );
    }

    @Override
    public boolean isGroupReadable()
    {
        return PlexusIoResourceAttributeUtils.isGroupReadableInOctal( mode );
    }

    @Override
    public boolean isGroupWritable()
    {
        return PlexusIoResourceAttributeUtils.isGroupWritableInOctal( mode );
    }

    @Override
    public boolean isOwnerExecutable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerExecutableInOctal( mode );
    }

    @Override
    public boolean isOwnerReadable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerReadableInOctal( mode );
    }

    @Override
    public boolean isOwnerWritable()
    {
        return PlexusIoResourceAttributeUtils.isOwnerWritableInOctal( mode );
    }

    @Override
    public boolean isWorldExecutable()
    {
        return PlexusIoResourceAttributeUtils.isWorldExecutableInOctal( mode );
    }

    @Override
    public boolean isWorldReadable()
    {
        return PlexusIoResourceAttributeUtils.isWorldReadableInOctal( mode );
    }

    @Override
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

    public PlexusIoResourceAttributes setOctalModeString( String mode )
    {
        setOctalMode( Integer.parseInt( mode, 8 ) );
        return this;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( 128 );
        sb.append( System.lineSeparator() );
        sb.append( "Resource Attributes:" );
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
        sb.append( uid != null ? uid : 0 );
        sb.append( System.lineSeparator() );
        sb.append( "gid: " );
        sb.append( gid != null ? gid : 0 );
        sb.append( System.lineSeparator() );
        sb.append( "mode: " );
        sb.append( String.format( "%06o", mode) );

        return sb.toString();
    }

    public void setSymbolicLink( boolean isSymbolicLink )
    {
        this.isSymbolicLink = isSymbolicLink;
    }

    @Override
    public boolean isSymbolicLink()
    {
        return isSymbolicLink;
    }
}
