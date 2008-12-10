package org.codehaus.plexus.components.io.resources;

public interface PlexusIOResourceCollectionWithAttributes
{

    /**
     * Sets the file and directory attributes to use as overrides.
     * 
     * @param uid
     * @param userName
     * @param gid 
     * @param groupName
     * @param fileMode The octal mode to use for files
     * @param dirMode The octal mode to use for directories
     */
    void setOverrideAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode );
    
}
