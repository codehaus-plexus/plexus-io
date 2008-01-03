package org.codehaus.plexus.components.io.filemappers;


/**
 * A file mapper, which maps by adding a prefix.
 */
public class PrefixFileMapper
    extends IdentityMapper
{
    /**
     * The merge mappers role-hint: "prefix".
     */
    public static final String ROLE_HINT = "prefix";

    private String prefix;

    public String getMappedFileName( String name )
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
        if ( prefix == null  ||  prefix.length() == 0 )
        {
            return name;
        }
        return prefix + name;
    }
}
