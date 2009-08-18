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
 * An implementation of {@link FileMapper}, which changes the files extension.
 */
public class FileExtensionMapper extends AbstractFileMapper
{
    /**
     * The file extension mappers role-hint: "fileExtension".
     */
    public static final String ROLE_HINT = "fileExtension";

    private String targetExtension;

    /**
     * Sets the target files extension.
     * 
     * @throws IllegalArgumentException
     *             The target extension is null or empty.
     */
    public void setTargetExtension( String pTargetExtension )
    {
        if ( pTargetExtension == null )
        {
            throw new IllegalArgumentException( "The target extension is null." );
        }
        if ( pTargetExtension.length() == 0 )
        {
            throw new IllegalArgumentException( "The target extension is empty." );
        }
        if ( pTargetExtension.charAt( 0 ) == '.' )
        {
            targetExtension = pTargetExtension;
        }
        else
        {
            targetExtension = '.' + pTargetExtension;
        }
    }

    /**
     * Returns the target files extension.
     */
    public String getTargetExtension()
    {
        return targetExtension;
    }

    public String getMappedFileName( String pName )
    {
        final String ext = getTargetExtension();
        if ( ext == null )
        {
            throw new IllegalStateException( "The target extension has not been set." );
        }
        final String name = super.getMappedFileName( pName ); // Check arguments
        final int dirSep = Math.max( pName.lastIndexOf( '/' ), pName.lastIndexOf( '\\' ) );
        final int offset = pName.lastIndexOf( '.' );
        if ( offset <= dirSep )
        {
            return name + ext;
        }
        else
        {
            return name.substring( 0, offset ) + ext;
        }
    }
}