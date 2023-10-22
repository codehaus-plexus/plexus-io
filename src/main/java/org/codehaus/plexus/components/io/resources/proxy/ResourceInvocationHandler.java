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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.NameSupplier;
import org.codehaus.plexus.components.io.functions.ResourceAttributeSupplier;
import org.codehaus.plexus.components.io.functions.SizeSupplier;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.jetbrains.annotations.NotNull;

class ResourceInvocationHandler implements InvocationHandler {
    private final PlexusIoResource testImpl;

    private final ContentSupplier contentSupplier;
    private final NameSupplier nameSupplier;
    private final SizeSupplier sizeSupplier;
    private final SymlinkDestinationSupplier symlinkDestinationSupplier;
    private final ResourceAttributeSupplier resourceAttributeSupplier;

    public ResourceInvocationHandler(@NotNull PlexusIoResource target, Object alternativeHandler) {
        this.testImpl = target;
        this.contentSupplier = asOrNull(alternativeHandler, ContentSupplier.class);
        this.nameSupplier = asOrNull(alternativeHandler, NameSupplier.class);
        this.sizeSupplier = asOrNull(alternativeHandler, SizeSupplier.class);
        this.symlinkDestinationSupplier = asOrNull(alternativeHandler, SymlinkDestinationSupplier.class);
        this.resourceAttributeSupplier = asOrNull(alternativeHandler, ResourceAttributeSupplier.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> T asOrNull(Object instance, Class<T> clazz) {
        if (instance != null && clazz.isAssignableFrom(instance.getClass())) return (T) instance;
        else return null;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if (contentSupplier != null && "getContents".equals(name)) {
            return contentSupplier.getContents();
        }
        if (nameSupplier != null && "getName".equals(name)) {
            return nameSupplier.getName();
        }
        if (sizeSupplier != null && "getSize".equals(name)) {
            return sizeSupplier.getSize();
        }
        if (symlinkDestinationSupplier != null && "getSymlinkDestination".equals(name)) {
            return symlinkDestinationSupplier.getSymlinkDestination();
        }

        if (resourceAttributeSupplier != null && "getAttributes".equals(name)) {
            return resourceAttributeSupplier.getAttributes();
        }

        return method.invoke(testImpl, args);
    }
}
