package org.codehaus.plexus.components.io.resources.proxy;

import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.NameSupplier;
import org.codehaus.plexus.components.io.functions.ResourceAttributeSupplier;
import org.codehaus.plexus.components.io.functions.SizeSupplier;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class ResourceInvocationHandler
    implements InvocationHandler
{
    private PlexusIoResource testImpl;

    private final ContentSupplier contentSupplier;
    private final NameSupplier nameSupplier;
    private final SizeSupplier sizeSupplier;
    private final SymlinkDestinationSupplier symlinkDestinationSupplier;
    private final ResourceAttributeSupplier resourceAttributeSupplier;

    public ResourceInvocationHandler( @Nonnull PlexusIoResource target, Object alternativeHandler )
    {
        this.testImpl = target;
        this.contentSupplier = asOrNull( alternativeHandler, ContentSupplier.class );
        this.nameSupplier  = asOrNull( alternativeHandler, NameSupplier.class );
        this.sizeSupplier = asOrNull(  alternativeHandler, SizeSupplier.class);
        this.symlinkDestinationSupplier = asOrNull(  alternativeHandler, SymlinkDestinationSupplier.class);
        this.resourceAttributeSupplier = asOrNull(  alternativeHandler, ResourceAttributeSupplier.class);

    }

    @SuppressWarnings( "unchecked" )
    private static <T> T asOrNull(Object instance, Class<T> clazz){
        if (instance != null && clazz.isAssignableFrom( instance.getClass())) return (T) instance;
        else return null;
    }

    public Object invoke( Object proxy, Method method, Object[] args )
        throws Throwable
    {
        String name = method.getName();
        if (contentSupplier != null && "getContents".equals( name )){
            return contentSupplier.getContents();
        }
        if (nameSupplier != null && "getName".equals( name )){
            return nameSupplier.getName();
        }
        if (sizeSupplier != null && "getSize".equals( name )){
            return sizeSupplier.getSize();
        }
        if (symlinkDestinationSupplier != null && "getSymlinkDestination".equals( name )){
            return symlinkDestinationSupplier.getSymlinkDestination();
        }

        if (resourceAttributeSupplier != null && "getAttributes".equals( name )){
            return resourceAttributeSupplier.getAttributes();
        }

        return method.invoke( testImpl, args );
    }
}

