package org.codehaus.plexus.components.io.filemappers;

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

import javax.annotation.Nonnull;
import javax.inject.Named;

/**
 * Default implementation of {@link FileMapper}, which performs the identity mapping: All names are left unchanged.
 */
@Named(IdentityMapper.ROLE_HINT)
public class IdentityMapper extends AbstractFileMapper {
    /**
     * The identity mappers role-hint: "identity".
     */
    public static final String ROLE_HINT = "identity";

    @Nonnull
    public String getMappedFileName(@Nonnull String pName) {
        if (pName == null || pName.isEmpty()) {
            throw new IllegalArgumentException("The source name must not be null.");
        }
        return pName;
    }
}
