package org.codehaus.plexus.components.io.filemappers;

/**
 * Implementation of a flattening file mapper: Removes all directory parts.
 */
public class FlattenFileMapper extends IdentityMapper
{
    /**
     * The flatten file mappers role-hint: "flatten".
     */
    public static final String ROLE_HINT = "flatten";

    public String getMappedFileName( String pName )
    {
        String name = super.getMappedFileName( pName ); // Check for null, etc.
        int offset = pName.lastIndexOf( '/' );
        if ( offset >= 0 )
        {
            name = name.substring( offset + 1 );
        }
        offset = pName.lastIndexOf( '\\' );
        if ( offset >= 0 )
        {
            name = name.substring( offset + 1 );
        }
        return name;
    }
}
