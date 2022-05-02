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

/**
 * A file mapper, which maps by adding a prefix.
 */
public class PrefixFileMapper extends AbstractFileMapper
{
    /**
     * The merge mappers role-hint: "prefix".
     */
    public static final String ROLE_HINT = "prefix";

    private String prefix;

    @Override
    @Nonnull public String getMappedFileName( @Nonnull String name )
    {
        final String s = super.getMappedFileName( name ); // Check for null, etc.
        return getMappedFileName( prefix, s );
    }

    /**
     * Returns the prefix to add.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Sets the prefix to add.
     */
    public void setPrefix( String prefix )
    {
        this.prefix = prefix;
    }

    /**
     * Performs the mapping of a file name by adding a prefix.
     */
    public static String getMappedFileName( String prefix, String name )
    {
        if ( prefix == null || prefix.length() == 0 )
        {
            return name;
        }
        return prefix + name;
    }
}