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
 * A file mapper, which maps to a constant target name.
 */
@Named( MergeFileMapper.ROLE_HINT )
public class MergeFileMapper extends AbstractFileMapper
{
    /**
     * The merge mappers role-hint: "merge".
     */
    public static final String ROLE_HINT = "merge";

    private String targetName;

    /**
     * Sets the merge mappers target name.
     * 
     * @throws IllegalArgumentException
     *             The target name is null or empty.
     */
    public void setTargetName( String pName )
    {
        if ( pName == null )
        {
            throw new IllegalArgumentException( "The target name is null." );
        }
        if ( pName.length() == 0 )
        {
            throw new IllegalArgumentException( "The target name is empty." );
        }
        targetName = pName;
    }

    /**
     * Returns the merge mappers target name.
     * 
     * @throws IllegalArgumentException
     *             The target name is null or empty.
     */
    public String getTargetName()
    {
        return targetName;
    }

    @Nonnull public String getMappedFileName( @Nonnull String pName )
    {
        final String name = getTargetName();
        if ( name == null )
        {
            throw new IllegalStateException( "The target file name has not been set." );
        }
        super.getMappedFileName( pName ); // Check for null, etc.
        return name;
    }
}