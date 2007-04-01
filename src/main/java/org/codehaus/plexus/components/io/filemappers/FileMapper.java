package org.codehaus.plexus.components.io.filemappers;

/**
 * Interface of a component, which may be used to map file names.
 */
public interface FileMapper
{
    /**
     * Role used to register component implementations with the container.
     */
    public static final String ROLE = FileMapper.class.getName();

    /**
     * The default role-hint: "default".
     */
    public static final String DEFAULT_ROLE_HINT = "default";

    /**
     * Maps the given source name to a target name.
     * 
     * @param pName
     *            The source name.
     * @return The target name.
     * @throws IllegalArgumentException
     *             The source name is null or empty.
     */
    public String getMappedFileName( String pName );
}
