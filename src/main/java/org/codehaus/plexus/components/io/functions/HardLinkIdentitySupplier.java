package org.codehaus.plexus.components.io.functions;

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

import java.io.IOException;

/**
 * Optional identity for resources whose contents can share one hard-link payload.
 * Implementations must return null when transformation or a replacement content
 * supplier invalidates that relationship. Equal identities must denote the same
 * content within a stable source, not merely equal bytes from unrelated files.
 * Consumers must also compare the metadata they intend to write.
 *
 * @since 3.7.1
 */
public interface HardLinkIdentitySupplier {
    /**
     * Returns a source-scoped identity suitable for equality comparisons.
     * Identities must only be cached for the duration of one archive operation.
     *
     * @return the content identity, or null when preservation cannot be guaranteed
     * @throws IOException if source attributes cannot be read
     */
    Object getHardLinkIdentity() throws IOException;
}
