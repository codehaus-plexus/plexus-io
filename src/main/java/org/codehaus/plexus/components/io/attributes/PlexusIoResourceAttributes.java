package org.codehaus.plexus.components.io.attributes;

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
