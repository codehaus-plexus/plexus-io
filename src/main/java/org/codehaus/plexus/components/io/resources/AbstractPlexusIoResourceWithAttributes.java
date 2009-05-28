package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

public abstract class AbstractPlexusIoResourceWithAttributes
    extends AbstractPlexusIoResource
    implements PlexusIoResourceWithAttributes
{

    private PlexusIoResourceAttributes attributes;

    public AbstractPlexusIoResourceWithAttributes()
    {
        super();
    }

    public PlexusIoResourceAttributes getAttributes()
    {
        return attributes;
    }

    public void setAttributes( PlexusIoResourceAttributes attributes )
    {
        this.attributes = attributes;
    }

}