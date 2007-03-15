package org.codehaus.plexus.components.io.filemappers;

/**
 * Default implementation of {@link FileMapper}, which performs the identity mapping: All names are left unchanged.
 */
public class IdentityMapper implements FileMapper
{
    /**
     * The identity mappers role-hint: "identity".
     */
    public static final String ROLE_HINT = "identity";

    public String getMappedFileName( String pName )
    {
        if ( pName == null || pName.length() == 0 )
        {
            throw new IllegalArgumentException( "The source name must not be null." );
        }
        return pName;
    }
}
