package org.codehaus.plexus.components.io.resources;

import java.io.IOException;

import org.codehaus.plexus.components.io.fileselectors.FileSelector;



/**
 * Default implementation of a resource collection.
 */
public abstract class AbstractPlexusIoResourceCollection implements PlexusIoResourceCollection
{
    private String prefix;

    private String[] includes;

    private String[] excludes;

    private FileSelector[] fileSelectors;

    private boolean caseSensitive = true;

    private boolean usingDefaultExcludes = true;

    private boolean includingEmptyDirectories = true;

    /**
     * Sets a string of patterns, which excluded files
     * should match.
     */
    public void setExcludes( String[] excludes )
    {
        this.excludes = excludes;
    }

    /**
     * Returns a string of patterns, which excluded files
     * should match.
     */
    public String[] getExcludes()
    {
        return excludes;
    }

    /**
     * Sets a set of file selectors, which should be used
     * to select the included files.
     */
    public void setFileSelectors( FileSelector[] fileSelectors )
    {
        this.fileSelectors = fileSelectors;
    }

    /**
     * Returns a set of file selectors, which should be used
     * to select the included files.
     */
    public FileSelector[] getFileSelectors()
    {
        return fileSelectors;
    }

    /**
     * Sets a string of patterns, which included files
     * should match.
     */
    public void setIncludes( String[] includes )
    {
        this.includes = includes;
    }

    /**
     * Returns a string of patterns, which included files
     * should match.
     */
    public String[] getIncludes()
    {
        return includes;
    }

    /**
     * Sets the prefix, which the file sets contents shall
     * have.
     */
    public void setPrefix( String prefix )
    {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix, which the file sets contents shall
     * have.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Sets, whether the include/exclude patterns are
     * case sensitive. Defaults to true.
     */
    public void setCaseSensitive( boolean caseSensitive )
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Returns, whether the include/exclude patterns are
     * case sensitive. Defaults to true.
     */
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /**
     * Sets, whether the default excludes are being
     * applied. Defaults to true.
     */
    public void setUsingDefaultExcludes( boolean usingDefaultExcludes )
    {
        this.usingDefaultExcludes = usingDefaultExcludes;
    }

    /**
     * Returns, whether the default excludes are being
     * applied. Defaults to true.
     */
    public boolean isUsingDefaultExcludes()
    {
        return usingDefaultExcludes;
    }

    /**
     * Sets, whether empty directories are being included. Defaults
     * to true.
     */
    public void setIncludingEmptyDirectories( boolean includingEmptyDirectories )
    {
        this.includingEmptyDirectories = includingEmptyDirectories;
    }

    /**
     * Returns, whether empty directories are being included. Defaults
     * to true.
     */
    public boolean isIncludingEmptyDirectories()
    {
        return includingEmptyDirectories;
    }

    protected boolean isSelected( PlexusIoResource plexusIoResource ) throws IOException
    {
        FileSelector[] fileSelectors = getFileSelectors();
        if ( fileSelectors != null )
        {
            for ( int i = 0;  i < fileSelectors.length;  i++ )
            {
                if ( !fileSelectors[i].isSelected( plexusIoResource ) )
                {
                    return false;
                }
            }
        }
        return true;
    }
}
