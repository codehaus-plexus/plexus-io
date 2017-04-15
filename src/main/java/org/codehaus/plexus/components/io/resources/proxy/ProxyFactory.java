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

import org.codehaus.plexus.components.io.functions.FileSupplier;
import org.codehaus.plexus.components.io.functions.ResourceAttributeSupplier;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import javax.annotation.Nonnull;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory
{
    public static PlexusIoResource createProxy(@Nonnull PlexusIoResource target, Object alternateSupplier)
    {

        List<Class> interfaces = new ArrayList<>(  );
        interfaces.add( PlexusIoResource.class);
        if (target instanceof SymlinkDestinationSupplier ) interfaces.add( SymlinkDestinationSupplier.class);
        if (target instanceof FileSupplier ) interfaces.add( FileSupplier.class);
        if (target instanceof ResourceAttributeSupplier) interfaces.add( ResourceAttributeSupplier.class);

        return (PlexusIoResource) Proxy.newProxyInstance( PlexusIoResource.class.getClassLoader(),
                                                                        interfaces.toArray(new Class[interfaces.size()]),
                                                                        new ResourceInvocationHandler( target, alternateSupplier) );
    }
}
