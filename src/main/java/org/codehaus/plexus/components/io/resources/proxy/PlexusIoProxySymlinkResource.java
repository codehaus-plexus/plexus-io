/*
 * Copyright 2014 The Codehaus Foundation.
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

package org.codehaus.plexus.components.io.resources.proxy;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.components.io.resources.PlexusIoSymlink;

import javax.annotation.Nonnull;
import java.io.IOException;

public class PlexusIoProxySymlinkResource
    extends PlexusIoProxyResource
    implements PlexusIoSymlink
{

    public PlexusIoProxySymlinkResource( @Nonnull PlexusIoResource plexusIoResource )
    {
        super( plexusIoResource );
        if ( !( plexusIoResource instanceof PlexusIoSymlink ) )
        {
            throw new IllegalArgumentException( "Resource must be symlink" );
        }
    }

    public String getSymlinkDestination()
        throws IOException
    {
        return ( (PlexusIoSymlink) src ).getSymlinkDestination();
    }
}
