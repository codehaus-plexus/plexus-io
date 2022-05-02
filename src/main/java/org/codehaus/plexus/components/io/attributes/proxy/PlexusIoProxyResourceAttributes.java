package org.codehaus.plexus.components.io.attributes.proxy;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

import javax.annotation.Nullable;

public class PlexusIoProxyResourceAttributes
    implements PlexusIoResourceAttributes


{
    PlexusIoResourceAttributes target;

    public PlexusIoProxyResourceAttributes( PlexusIoResourceAttributes thisAttr )
    {
        this.target = thisAttr;
    }

    @Override
    public boolean isOwnerReadable()
    {
        return target.isOwnerReadable();
    }

    @Override
    public int getOctalMode()
    {
        return target.getOctalMode();
    }

    @Override
    public String getUserName()
    {
        return target.getUserName();
    }

    @Override
    public boolean isGroupReadable()
    {
        return target.isGroupReadable();
    }

    @Override
    public boolean isWorldExecutable()
    {
        return target.isWorldExecutable();
    }

    @Override
    @Nullable public Integer getGroupId()
    {
        return target.getGroupId();
    }

    @Override
    public boolean isGroupWritable()
    {
        return target.isGroupWritable();
    }

    @Override
    public Integer getUserId()
    {
        return target.getUserId();
    }

/*    public String getOctalModeString()
    {
        return target.getOctalModeString();
    }
*/
    @Override
    public boolean isOwnerWritable()
    {
        return target.isOwnerWritable();
    }

    @Override
    public boolean isOwnerExecutable()
    {
        return target.isOwnerExecutable();
    }

    @Override
    public boolean isSymbolicLink()
    {
        return target.isSymbolicLink();
    }

    @Override
    public boolean isGroupExecutable()
    {
        return target.isGroupExecutable();
    }

    @Override
    public boolean isWorldWritable()
    {
        return target.isWorldWritable();
    }

    @Override
    @Nullable public String getGroupName()
    {
        return target.getGroupName();
    }

    @Override
    public boolean isWorldReadable()
    {
        return target.isWorldReadable();
    }
}
