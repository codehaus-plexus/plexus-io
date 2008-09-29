package org.codehaus.plexus.components.io.attributes;

public class SimpleResourceAttributes
    implements PlexusIoResourceAttributes
{
    
    private int gid = -1;
    
    private int uid = -1;
    
    private String userName;
    
    private String groupName;
    
    private int mode;
    
    public SimpleResourceAttributes( int uid, String userName, int gid, String groupName, int mode )
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

    public int getGroupId()
    {
        return gid;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public int getUserId()
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
    
    public PlexusIoResourceAttributes setGroupId( int gid )
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

    public PlexusIoResourceAttributes setUserId( int uid )
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

}
