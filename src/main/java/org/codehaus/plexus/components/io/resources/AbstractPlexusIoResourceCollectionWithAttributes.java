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

/**
 * Default implementation of a resource collection with attributes.
 */
public abstract class AbstractPlexusIoResourceCollectionWithAttributes
    extends AbstractPlexusIoResourceCollection
{

    private PlexusIoResourceAttributes defaultFileAttributes;

    private PlexusIoResourceAttributes defaultDirAttributes;

    private PlexusIoResourceAttributes overrideFileAttributes;

    private PlexusIoResourceAttributes overrideDirAttributes;

    protected AbstractPlexusIoResourceCollectionWithAttributes()
    {
    }

    protected PlexusIoResourceAttributes getDefaultFileAttributes()
    {
        return defaultFileAttributes;
    }

    protected void setDefaultFileAttributes( final PlexusIoResourceAttributes defaultFileAttributes )
    {
        this.defaultFileAttributes = defaultFileAttributes;
    }

    protected PlexusIoResourceAttributes getDefaultDirAttributes()
    {
        return defaultDirAttributes;
    }

    protected void setDefaultDirAttributes( final PlexusIoResourceAttributes defaultDirAttributes )
    {
        this.defaultDirAttributes = defaultDirAttributes;
    }

    protected PlexusIoResourceAttributes getOverrideFileAttributes()
    {
        return overrideFileAttributes;
    }

    protected void setOverrideFileAttributes( final PlexusIoResourceAttributes overrideFileAttributes )
    {
        this.overrideFileAttributes = overrideFileAttributes;
    }

    protected PlexusIoResourceAttributes getOverrideDirAttributes()
    {
        return overrideDirAttributes;
    }

    protected void setOverrideDirAttributes( final PlexusIoResourceAttributes overrideDirAttributes )
    {
        this.overrideDirAttributes = overrideDirAttributes;
    }
}
