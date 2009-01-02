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

/**
 * Interface of a component, which may be used to map file names.
 */
public interface FileMapper
{
    /**
     * Role used to register component implementations with the container.
     */
    public static final String ROLE = FileMapper.class.getName();

    /**
     * The default role-hint: "default".
     */
    public static final String DEFAULT_ROLE_HINT = "default";

    /**
     * Maps the given source name to a target name.
     * 
     * @param pName
     *            The source name.
     * @return The target name.
     * @throws IllegalArgumentException
     *             The source name is null or empty.
     */
    public String getMappedFileName( String pName );
}