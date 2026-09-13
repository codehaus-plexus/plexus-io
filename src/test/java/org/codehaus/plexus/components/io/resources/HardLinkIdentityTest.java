/*
 * Copyright 2026 The plexus developers.
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

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.codehaus.plexus.components.io.functions.ContentSupplier;
import org.codehaus.plexus.components.io.functions.HardLinkIdentitySupplier;
import org.codehaus.plexus.components.io.functions.NameSupplier;
import org.codehaus.plexus.components.io.resources.proxy.ProxyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class HardLinkIdentityTest {
    @TempDir
    Path temp;

    /** Checks filesystem identity and invalidation when effective content changes. */
    @Test
    void identifiesOnlyOriginalContent() throws Exception {
        Path first = Files.write(temp.resolve("first"), new byte[] {1});
        Path second = temp.resolve("second");
        try {
            Files.createLink(second, first);
        } catch (java.io.IOException | UnsupportedOperationException e) {
            assumeTrue(false, "Hard links unavailable: " + e);
        }
        PlexusIoResource a = ResourceFactory.createResource(first.toFile());
        PlexusIoResource b = ResourceFactory.createResource(second.toFile());
        Object identity = ((HardLinkIdentitySupplier) a).getHardLinkIdentity();
        assumeTrue(identity != null, "Filesystem does not expose file keys");
        assertEquals(identity, ((HardLinkIdentitySupplier) b).getHardLinkIdentity());
        PlexusIoResource transformed = ResourceFactory.createResource(
                first.toFile(), (resource, input) -> new ByteArrayInputStream(new byte[] {2}));
        assertNull(((HardLinkIdentitySupplier) transformed).getHardLinkIdentity());
        PlexusIoResource supplied = ResourceFactory.createResource(
                first.toFile(),
                "custom",
                () -> new ByteArrayInputStream(new byte[] {3}),
                (org.codehaus.plexus.components.io.functions.InputStreamTransformer) null);
        assertNull(((HardLinkIdentitySupplier) supplied).getHardLinkIdentity());
    }

    /** Transparent renaming keeps identity while a replacement stream invalidates it. */
    @Test
    void proxiesRespectContentOverrides() throws Exception {
        Path file = Files.write(temp.resolve("file"), new byte[] {1});
        PlexusIoResource original = ResourceFactory.createResource(file.toFile());
        Object identity = ((HardLinkIdentitySupplier) original).getHardLinkIdentity();
        PlexusIoResource renamed = ProxyFactory.createProxy(original, (NameSupplier) () -> "renamed");
        assertEquals(identity, ((HardLinkIdentitySupplier) renamed).getHardLinkIdentity());
        PlexusIoResource replaced =
                ProxyFactory.createProxy(original, (ContentSupplier) () -> new ByteArrayInputStream(new byte[] {2}));
        assertNull(((HardLinkIdentitySupplier) replaced).getHardLinkIdentity());
    }

    /** Collection resolution must explicitly preserve or discard the source guarantee. */
    @Test
    void deferredResourcesRespectTransformers() throws Exception {
        Path file = Files.write(temp.resolve("file"), new byte[] {1});
        PlexusIoResource original = ResourceFactory.createResource(file.toFile());
        Object identity = ((HardLinkIdentitySupplier) original).getHardLinkIdentity();
        PlexusIoResourceCollection owner = new PlexusIoFileResourceCollection();
        assertEquals(
                identity,
                ((HardLinkIdentitySupplier) new Deferred(original, owner, false).asResource()).getHardLinkIdentity());
        assertNull(((HardLinkIdentitySupplier) new Deferred(original, owner, true).asResource()).getHardLinkIdentity());
    }
}
