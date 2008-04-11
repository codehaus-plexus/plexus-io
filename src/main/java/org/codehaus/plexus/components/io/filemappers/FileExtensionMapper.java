package org.codehaus.plexus.components.io.filemappers;

/**
 * An implementation of {@link FileMapper}, which changes the files extension.
 */
public class FileExtensionMapper extends IdentityMapper
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
        final int offset = pName.indexOf( '.' );
        if ( offset == -1 )
        {
            return name + ext;
        }
        else
        {
            return name.substring( 0, offset ) + ext;
        }
    }
}
