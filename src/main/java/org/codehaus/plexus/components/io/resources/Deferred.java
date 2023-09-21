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
package org.codehaus.plexus.components.io.resources;

import javax.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.NameSupplier;
import org.codehaus.plexus.components.io.functions.SizeSupplier;
import org.codehaus.plexus.components.io.resources.proxy.ProxyFactory;

class Deferred implements ContentSupplier, NameSupplier, SizeSupplier {
    final DeferredFileOutputStream dfos;

    final PlexusIoResource resource;

    final PlexusIoResourceCollection owner;

    public Deferred(final PlexusIoResource resource, PlexusIoResourceCollection owner, boolean hasTransformer)
            throws IOException {
        this.resource = resource;
        this.owner = owner;
        dfos = hasTransformer
                ? DeferredFileOutputStream.builder()
                        .setThreshold(5000000)
                        .setPrefix("p-archiver")
                        .get()
                : null;
        if (dfos != null) {
            InputStream inputStream = owner.getInputStream(resource);
            IOUtils.copy(inputStream, dfos);
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Nonnull
    public InputStream getContents() throws IOException {
        if (dfos == null) {
            return resource.getContents();
        }
        if (dfos.isInMemory()) {
            return new ByteArrayInputStream(dfos.getData());
        } else {
            return new FileInputStream(dfos.getFile()) {
                @Override
                public void close() throws IOException {
                    super.close();
                    dfos.getFile().delete();
                }
            };
        }
    }

    public long getSize() {
        if (dfos == null) {
            return resource.getSize();
        }
        if (dfos.isInMemory()) {
            return dfos.getByteCount();
        } else {
            return dfos.getFile().length();
        }
    }

    public String getName() {
        return owner.getName(resource);
    }

    public PlexusIoResource asResource() {
        return ProxyFactory.createProxy(resource, Deferred.this);
    }
}
