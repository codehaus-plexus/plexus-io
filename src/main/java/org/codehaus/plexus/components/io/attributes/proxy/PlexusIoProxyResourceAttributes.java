package org.codehaus.plexus.components.io.attributes.proxy;

import javax.annotation.Nullable;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

public class PlexusIoProxyResourceAttributes implements PlexusIoResourceAttributes {

    final PlexusIoResourceAttributes target;

    public PlexusIoProxyResourceAttributes(PlexusIoResourceAttributes thisAttr) {
        this.target = thisAttr;
    }

    public boolean isOwnerReadable() {
        return target.isOwnerReadable();
    }

    public int getOctalMode() {
        return target.getOctalMode();
    }

    public String getUserName() {
        return target.getUserName();
    }

    public boolean isGroupReadable() {
        return target.isGroupReadable();
    }

    public boolean isWorldExecutable() {
        return target.isWorldExecutable();
    }

    @Nullable
    public Integer getGroupId() {
        return target.getGroupId();
    }

    public boolean isGroupWritable() {
        return target.isGroupWritable();
    }

    public Integer getUserId() {
        return target.getUserId();
    }

    public boolean isOwnerWritable() {
        return target.isOwnerWritable();
    }

    public boolean isOwnerExecutable() {
        return target.isOwnerExecutable();
    }

    public boolean isSymbolicLink() {
        return target.isSymbolicLink();
    }

    public boolean isGroupExecutable() {
        return target.isGroupExecutable();
    }

    public boolean isWorldWritable() {
        return target.isWorldWritable();
    }

    @Nullable
    public String getGroupName() {
        return target.getGroupName();
    }

    public boolean isWorldReadable() {
        return target.isWorldReadable();
    }
}
