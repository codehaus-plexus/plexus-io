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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class ForwardingIterator
    implements Iterator<PlexusIoResource>, Closeable
{
    private final Object possiblyCloseable;

    private PlexusIoResource next = null;

    ForwardingIterator( Object possiblyCloseable )
    {
        this.possiblyCloseable = possiblyCloseable;
    }

    public boolean hasNext()
    {
        if ( next == null )
        {
            try
            {
                next = getNextResource();
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        }
        return next != null;
    }

    public PlexusIoResource next()
    {
        if ( !hasNext() )
        {
            throw new NoSuchElementException();
        }
        PlexusIoResource ret = next;
        next = null;
        return ret;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    public void close()
        throws IOException
    {
        if ( possiblyCloseable instanceof Closeable )
        {
            ( (Closeable) possiblyCloseable ).close();
        }

    }

    /**
     * Returns the next resource or null if no next resource;
     */
    protected abstract PlexusIoResource getNextResource() throws IOException;
}
