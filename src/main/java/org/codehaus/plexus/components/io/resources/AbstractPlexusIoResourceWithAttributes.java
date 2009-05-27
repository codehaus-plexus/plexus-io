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

    /**
     * Creates a new instance. The settings are copied
     * from the given resource.
     */
    public AbstractPlexusIoResourceWithAttributes( PlexusIoResourceWithAttributes plexusIoResource )
    {
        super( plexusIoResource );
        this.attributes = plexusIoResource.getAttributes();
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