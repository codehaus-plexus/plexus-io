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
 * Abstract base class for deriving file mappers. It is recommended
 * to use this class, if your implement your own file mappers, as
 * this might allow to extend the FileMapper interface later on
 * without loosing upwards compatibility.
 */
public abstract class AbstractFileMapper implements FileMapper
{
    /**
     * Checks the input and returns it without modifications.
     */
    @Override
    public @Nonnull String getMappedFileName( @Nonnull String pName )
    {
        if ( pName == null || pName.length() == 0 )
        {
            throw new IllegalArgumentException( "The source name must not be null." );
        }
        return pName;
    }
}
