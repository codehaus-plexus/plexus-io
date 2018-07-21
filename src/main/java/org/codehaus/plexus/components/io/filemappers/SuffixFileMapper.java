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

import org.codehaus.plexus.util.StringUtils;

/**
 * A file mapper, which maps by adding a suffix.
 */
public class SuffixFileMapper extends AbstractFileMapper
{
    /**
    * The suffix mappers role-hint: "suffix".
    */
    public static final String ROLE_HINT = "suffix";

    private String suffix;

    @Nonnull public String getMappedFileName( @Nonnull String name )
    {
        final String s = super.getMappedFileName( name ); // Check for null, etc.
        return getMappedFileName( suffix, s );
    }

    /**
     * Returns the suffix to add.
     */
    public String getSuffix()
    {
        return suffix;
    }

    /**
     * Sets the suffix to add.
     */
    public void setSuffix( String suffix )
    {
        this.suffix = suffix;
    }

    /**
     * Performs the mapping of a file name by adding a suffix.
     */
    public static String getMappedFileName( String suffix, String name )
    {
        String nameWithSuffix = name;
        if ( StringUtils.isNotBlank( suffix ) )
        {
        	final int dirSep = Math.max( name.lastIndexOf( '/' ), name.lastIndexOf( '\\' ) );
        	String filename = dirSep > 0 ? name.substring( dirSep +1 ) : name;
        	String dirname = dirSep > 0 ? name.substring( 0, dirSep +1 ) : "";
            if ( filename.contains( "." ) )
            {
                String beforeExtension = filename.substring( 0, filename.indexOf( '.' ) );
                String afterExtension = filename.substring( filename.indexOf( '.' ) + 1 ) ;
                nameWithSuffix = dirname + beforeExtension + suffix + "." + afterExtension;
            }
            else
            {
                nameWithSuffix += suffix;
            }
        }
        return nameWithSuffix;
    }
}