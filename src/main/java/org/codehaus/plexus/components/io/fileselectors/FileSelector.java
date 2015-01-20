package org.codehaus.plexus.components.io.fileselectors;

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
import java.io.IOException;


/**
 * Interface of a component, which selects/deselects files.
 */
public interface FileSelector
{
    /**
     * Role used to register component implementations with the container.
     */
    public static final String ROLE = FileSelector.class.getName();

    /**
     * The default role-hint: "default".
     */
    public static final String DEFAULT_ROLE_HINT = "default";

    /**
     * Returns, whether the given file is selected.
     * @param An instance of FileInfo with the files meta data.
     *   It is recommended, that the caller creates an instance
     *   of {@link PlexusIoResource}.
     */
    boolean isSelected( @Nonnull FileInfo fileInfo );
}