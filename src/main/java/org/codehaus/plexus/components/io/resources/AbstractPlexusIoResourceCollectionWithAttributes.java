package org.codehaus.plexus.components.io.resources;

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

import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.logging.Logger;

/**
 * Default implementation of a resource collection with attributes.
 */
public abstract class AbstractPlexusIoResourceCollectionWithAttributes
    extends AbstractPlexusIoResourceCollection
    implements PlexusIOResourceCollectionWithAttributes
{

    private PlexusIoResourceAttributes defaultFileAttributes;

    private PlexusIoResourceAttributes defaultDirAttributes;

    private PlexusIoResourceAttributes overrideFileAttributes;

    private PlexusIoResourceAttributes overrideDirAttributes;

    protected AbstractPlexusIoResourceCollectionWithAttributes()
    {
    }

    protected AbstractPlexusIoResourceCollectionWithAttributes( final Logger logger )
    {
        super( logger );
    }

    protected PlexusIoResourceAttributes getDefaultFileAttributes()
    {
        return defaultFileAttributes;
    }

    protected PlexusIoResourceAttributes getDefaultDirAttributes()
    {
        return defaultDirAttributes;
    }

    protected PlexusIoResourceAttributes getOverrideFileAttributes()
    {
        return overrideFileAttributes;
    }

    protected PlexusIoResourceAttributes getOverrideDirAttributes()
    {
        return overrideDirAttributes;
    }

    public void setDefaultAttributes( final int uid, final String userName, final int gid, final String groupName,
                                      final int fileMode, final int dirMode )
    {
        defaultFileAttributes =
            new SimpleResourceAttributes( uid, userName, gid, groupName, fileMode > 0 ? fileMode : 0 );

        defaultDirAttributes =
            new SimpleResourceAttributes( uid, userName, gid, groupName, dirMode > 0 ? dirMode : 0 );
    }

    public void setOverrideAttributes( final int uid, final String userName, final int gid, final String groupName,
                                       final int fileMode, final int dirMode )
    {
        overrideFileAttributes =
            new SimpleResourceAttributes( uid, userName, gid, groupName, fileMode > 0 ? fileMode : 0 );

        overrideDirAttributes =
            new SimpleResourceAttributes( uid, userName, gid, groupName, dirMode > 0 ? fileMode : 0 );
    }
}
