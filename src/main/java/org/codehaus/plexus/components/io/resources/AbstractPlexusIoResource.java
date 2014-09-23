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


/**
 * Default implementation of {@link PlexusIoResource}.
 */
public abstract class AbstractPlexusIoResource implements PlexusIoResource
{
    private final String name;

    private final long lastModified, size;

    private final boolean isFile, isDirectory, isExisting;

    protected AbstractPlexusIoResource( String name, long lastModified, long size, boolean isFile, boolean isDirectory,
                                        boolean isExisting )
    {
        this.name = name;
        this.lastModified = lastModified;
        this.size = size;
        this.isFile = isFile;
        this.isDirectory = isDirectory;
        this.isExisting = isExisting;
    }

    public long getLastModified()
    {
        return lastModified;
    }

	public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    public boolean isExisting()
    {
        return isExisting;
    }

    public boolean isFile()
    {
        return isFile;
    }

    public boolean isSymbolicLink()
    {
        return false;
    }
}