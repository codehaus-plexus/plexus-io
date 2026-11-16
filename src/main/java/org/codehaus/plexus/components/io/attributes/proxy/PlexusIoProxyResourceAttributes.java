package org.codehaus.plexus.components.io.attributes.proxy;

import javax.annotation.Nullable;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

public class PlexusIoProxyResourceAttributes implements PlexusIoResourceAttributes {

    final PlexusIoResourceAttributes target;

    public PlexusIoProxyResourceAttributes(PlexusIoResourceAttributes thisAttr) {
        this.target = thisAttr;
    }

    @Override
    public boolean isOwnerReadable() {
        return target.isOwnerReadable();
    }

    @Override
    public int getOctalMode() {
        return target.getOctalMode();
    }

    @Override
    public String getUserName() {
        return target.getUserName();
    }

    @Override
    public boolean isGroupReadable() {
        return target.isGroupReadable();
    }

    @Override
    public boolean isWorldExecutable() {
        return target.isWorldExecutable();
    }

    @Nullable
    @Override
    public Integer getGroupId() {
        return target.getGroupId();
    }

    @Override
    public boolean isGroupWritable() {
        return target.isGroupWritable();
    }

    @Override
    public Integer getUserId() {
        return target.getUserId();
    }

    @Override
    public boolean isOwnerWritable() {
        return target.isOwnerWritable();
    }

    @Override
    public boolean isOwnerExecutable() {
        return target.isOwnerExecutable();
    }

    @Override
    public boolean isSymbolicLink() {
        return target.isSymbolicLink();
    }

    @Override
    public boolean isGroupExecutable() {
        return target.isGroupExecutable();
    }

    @Override
    public boolean isWorldWritable() {
        return target.isWorldWritable();
    }

    @Nullable
    @Override
    public String getGroupName() {
        return target.getGroupName();
    }

    @Override
    public boolean isWorldReadable() {
        return target.isWorldReadable();
    }
}
