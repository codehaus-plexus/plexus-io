package org.codehaus.plexus.components.io.filemappers;


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
	public String getMappedFileName( String pName )
    {
        if ( pName == null || pName.length() == 0 )
        {
            throw new IllegalArgumentException( "The source name must not be null." );
        }
        return pName;
    }
}
