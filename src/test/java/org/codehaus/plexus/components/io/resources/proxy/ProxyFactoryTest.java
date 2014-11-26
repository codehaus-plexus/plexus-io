package org.codehaus.plexus.components.io.resources.proxy;

import junit.framework.TestCase;
import org.codehaus.plexus.components.io.attributes.Java7FileAttributes;
import org.codehaus.plexus.components.io.attributes.PlexusIoResourceAttributes;
import org.codehaus.plexus.components.io.attributes.SimpleResourceAttributes;
import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.FileSupplier;
import org.codehaus.plexus.components.io.functions.NameSupplier;
import org.codehaus.plexus.components.io.functions.ResourceAttributeSupplier;
import org.codehaus.plexus.components.io.functions.SizeSupplier;
import org.codehaus.plexus.components.io.functions.SymlinkDestinationSupplier;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResource;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProxyFactoryTest
    extends TestCase
{

    public void testCreateProxy()
        throws Exception
    {
        final PlexusIoResource proxy = ProxyFactory.createProxy( getPomResource(), null );
        assertTrue(proxy instanceof ResourceAttributeSupplier );
        assertTrue(proxy instanceof FileSupplier );
        assertFalse( proxy instanceof SymlinkDestinationSupplier );
    }

    public void testCreateProxyWithNameOverride()
        throws Exception
    {
        NameSupplier ns = new NameSupplier()
        {
            public String getName()
            {
                return "fred";
            }
        };
        final PlexusIoResource proxy = ProxyFactory.createProxy( getPomResource(), ns );
        assertEquals( "fred", proxy.getName() );
    }

    public void testCreateProxyWithResourceAttributeOverride()
        throws Exception
    {
        final PlexusIoResourceAttributes s = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
        ResourceAttributeSupplier ns = new ResourceAttributeSupplier()
        {
            public PlexusIoResourceAttributes getAttributes()
            {
                return s;
            }
        };
        final PlexusIoResource proxy = ProxyFactory.createProxy( getPomResource(), ns );
        assertSame( s, ( ( ResourceAttributeSupplier)proxy).getAttributes() );
    }

    public void testCreateProxyWithSizeSupplierOverride()
        throws Exception
    {
        final PlexusIoResourceAttributes s = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
        SizeSupplier ns = new SizeSupplier()
        {
            public long getSize()
            {
                return 42;
            }
        };
        final PlexusIoResource proxy = ProxyFactory.createProxy( getPomResource(), ns );
        assertEquals( 42, proxy.getSize() );
    }


    public void testCreateProxyWithContentSupplierOverride()
        throws Exception
    {
        final InputStream s = new ByteArrayInputStream( new byte[10] );
        ContentSupplier ns = new ContentSupplier()
        {
            public InputStream getContents()
                throws IOException
            {
                return s;
            }

        };
        final PlexusIoResource proxy = ProxyFactory.createProxy( getPomResource(), ns );
        assertEquals( s, proxy.getContents() );
    }

    public void testCreateProxyWithSymlinkDestinationSupplierOverride()
        throws Exception
    {
        SymlinkDestinationSupplier ns = new SymlinkDestinationSupplier()
        {
            public String getSymlinkDestination()
                throws IOException
            {
                return "mordor";
            }
        };
        final PlexusIoResource proxy = ProxyFactory.createProxy( getDummySymlinkResource(), ns );
        assertEquals( "mordor", ((SymlinkDestinationSupplier)proxy).getSymlinkDestination() );
    }


    private PlexusIoFileResource getPomResource()
        throws IOException
    {
        final File file = new File( "pom.xml" );
        PlexusIoResourceAttributes attrs = Java7FileAttributes.uncached( file );

        return new PlexusIoFileResource( file, "pom.xml", attrs ){};
    }

    class Dummy extends PlexusIoFileResource implements SymlinkDestinationSupplier {
        public Dummy( @Nonnull File file, @Nonnull PlexusIoResourceAttributes attrs )
            throws IOException
        {
            super( file, file.getName(), attrs );
        }

        public String getSymlinkDestination()
            throws IOException
        {
            throw new IllegalStateException( "Unsupported" );
        }
    }
    private Dummy getDummySymlinkResource()
        throws IOException
    {
        final File file = new File( "pom.xml" );
        PlexusIoResourceAttributes attrs = Java7FileAttributes.uncached( file );

        return new Dummy( file, attrs );
    }

}