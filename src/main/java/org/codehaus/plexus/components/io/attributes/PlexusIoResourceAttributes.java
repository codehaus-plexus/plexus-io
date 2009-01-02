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

public interface PlexusIoResourceAttributes
{
    boolean isOwnerReadable();
    
    boolean isOwnerWritable();
    
    boolean isOwnerExecutable();
    
    boolean isGroupReadable();

    boolean isGroupWritable();
    
    boolean isGroupExecutable();
    
    boolean isWorldReadable();

    boolean isWorldWritable();
    
    boolean isWorldExecutable();
    
    int getUserId();
    
    int getGroupId();
    
    String getUserName();
    
    String getGroupName();
    
    int getOctalMode();
    
    String getOctalModeString();
    
    PlexusIoResourceAttributes setOwnerReadable( boolean flag );

    PlexusIoResourceAttributes setOwnerWritable( boolean flag );

    PlexusIoResourceAttributes setOwnerExecutable( boolean flag );

    PlexusIoResourceAttributes setGroupReadable( boolean flag );

    PlexusIoResourceAttributes setGroupWritable( boolean flag );

    PlexusIoResourceAttributes setGroupExecutable( boolean flag );

    PlexusIoResourceAttributes setWorldReadable( boolean flag );

    PlexusIoResourceAttributes setWorldWritable( boolean flag );

    PlexusIoResourceAttributes setWorldExecutable( boolean flag );

    PlexusIoResourceAttributes setUserId( int uid );

    PlexusIoResourceAttributes setGroupId( int gid );

    PlexusIoResourceAttributes setUserName( String name );

    PlexusIoResourceAttributes setGroupName( String name );
    
    PlexusIoResourceAttributes setOctalMode( int mode );
    
    PlexusIoResourceAttributes setOctalModeString( String mode );
}