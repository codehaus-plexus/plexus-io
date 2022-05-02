package org.codehaus.plexus.components.io.filemappers;

/*
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
 * A file mapper, which maps by adding a suffix to the filename.
 * If the filename contains dot, the suffix will be added before.
 * Example: {@code directory/archive.tar.gz => directory/archivesuffix.tar.gz}
 */
public class SuffixFileMapper extends AbstractFileMapper
{
    /**
    * The suffix mappers role-hint: "suffix".
    */
    public static final String ROLE_HINT = "suffix";

    private String suffix;

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
        if ( suffix == null )
        {
            throw new IllegalArgumentException( "The suffix is null." );
        }
        this.suffix = suffix;
    }

    @Override
    @Nonnull
    public String getMappedFileName( @Nonnull String pName)
    {
        final String name = super.getMappedFileName( pName );
        if ( suffix == null )
        {
            throw new IllegalStateException( "The suffix has not been set." );
        }
        final int dirSep = Math.max( name.lastIndexOf( '/' ), name.lastIndexOf( '\\' ) );
        String filename = dirSep > 0 ? name.substring( dirSep + 1 ) : name;
        String dirname = dirSep > 0 ? name.substring( 0, dirSep + 1 ) : "";
        if ( filename.contains( "." ) )
        {
            String beforeExtension = filename.substring( 0, filename.indexOf( '.' ) );
            String afterExtension = filename.substring( filename.indexOf( '.' ) + 1 ) ;
            return dirname + beforeExtension + suffix + "." + afterExtension;
        }
        return name + suffix;
    }
}