package org.codehaus.plexus.components.io.resources;

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;

public interface PlexusIoResourceWithAttributes
    extends PlexusIoResource
{
    
    PlexusIoResourceAttributes getAttributes();
    
    void setAttributes( PlexusIoResourceAttributes attributes );

}
