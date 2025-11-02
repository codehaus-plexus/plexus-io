package org.codehaus.plexus.components.io.resources.proxy;

import javax.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.plexus.components.io.attributes.FileAttributes;
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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyFactoryTest {

    @Test
    void createProxy() throws Exception {
        final PlexusIoResource proxy = ProxyFactory.createProxy(getPomResource(), null);
        assertInstanceOf(ResourceAttributeSupplier.class, proxy);
        assertInstanceOf(FileSupplier.class, proxy);
        assertFalse(proxy instanceof SymlinkDestinationSupplier);
    }

    @Test
    void createProxyWithNameOverride() throws Exception {
        NameSupplier ns = () -> "fred";
        final PlexusIoResource proxy = ProxyFactory.createProxy(getPomResource(), ns);
        assertEquals("fred", proxy.getName());
    }

    @Test
    void createProxyWithResourceAttributeOverride() throws Exception {
        final PlexusIoResourceAttributes s = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
        ResourceAttributeSupplier ns = () -> s;
        final PlexusIoResource proxy = ProxyFactory.createProxy(getPomResource(), ns);
        assertSame(s, ((ResourceAttributeSupplier) proxy).getAttributes());
    }

    @Test
    void createProxyWithSizeSupplierOverride() throws Exception {
        final PlexusIoResourceAttributes s = SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();
        SizeSupplier ns = () -> 42;
        final PlexusIoResource proxy = ProxyFactory.createProxy(getPomResource(), ns);
        assertEquals(42, proxy.getSize());
    }

    @Test
    void createProxyWithContentSupplierOverride() throws Exception {
        final InputStream s = new ByteArrayInputStream(new byte[10]);
        ContentSupplier ns = () -> s;
        final PlexusIoResource proxy = ProxyFactory.createProxy(getPomResource(), ns);
        assertEquals(s, proxy.getContents());
    }

    @Test
    void createProxyWithSymlinkDestinationSupplierOverride() throws Exception {
        SymlinkDestinationSupplier ns = () -> "mordor";
        final PlexusIoResource proxy = ProxyFactory.createProxy(getDummySymlinkResource(), ns);
        assertEquals("mordor", ((SymlinkDestinationSupplier) proxy).getSymlinkDestination());
    }

    private PlexusIoFileResource getPomResource() throws IOException {
        final File file = new File("pom.xml");
        PlexusIoResourceAttributes attrs = FileAttributes.uncached(file);

        return new PlexusIoFileResource(file, "pom.xml", attrs) {};
    }

    static class Dummy extends PlexusIoFileResource implements SymlinkDestinationSupplier {
        public Dummy(@Nonnull File file, @Nonnull PlexusIoResourceAttributes attrs) throws IOException {
            super(file, file.getName(), attrs);
        }

        public String getSymlinkDestination() {
            throw new IllegalStateException("Unsupported");
        }
    }

    private Dummy getDummySymlinkResource() throws IOException {
        final File file = new File("pom.xml");
        PlexusIoResourceAttributes attrs = FileAttributes.uncached(file);

        return new Dummy(file, attrs);
    }
}
