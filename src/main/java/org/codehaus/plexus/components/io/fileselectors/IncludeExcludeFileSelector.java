package org.codehaus.plexus.components.io.fileselectors;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.SelectorUtils;


/**
 * This file selector uses a set of patterns for including/excluding
 * files.
 */
public class IncludeExcludeFileSelector implements FileSelector
{
    /**
     * The include/exclude file selectors role-hint: "standard".
     */
    public static final String ROLE_HINT = "standard";

    private static final String[] ALL_INCLUDES = new String[]{ getCanonicalName( "**/*" ) };
    private static final String[] ZERO_EXCLUDES = new String[0];

    private boolean isCaseSensitive = true;

    private boolean useDefaultExcludes = true;

    private String[] includes;

    private String[] excludes;

    private String[] computedIncludes = ALL_INCLUDES;

    private String[] computedExcludes = ZERO_EXCLUDES;

    /**
     * Tests whether or not a name matches against at least one exclude
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         exclude pattern, or <code>false</code> otherwise.
     */
    protected boolean isExcluded( String name )
    {
        for ( int i = 0; i < computedExcludes.length; i++ )
        {
            if ( matchPath( computedExcludes[i], name, isCaseSensitive ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the list of include patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param includes A list of include patterns.
     *                 May be <code>null</code>, indicating that all files
     *                 should be included. If a non-<code>null</code>
     *                 list is given, all elements must be
     * non-<code>null</code>.
     */
    public void setIncludes( String[] includes )
    {
        this.includes = includes;
        if ( includes == null )
        {
            computedIncludes = ALL_INCLUDES;
        }
        else
        {
            computedIncludes = new String[includes.length];
            for ( int i = 0; i < includes.length; i++ )
            {
                String pattern = asPattern( includes[i] );
                computedIncludes[i] = pattern;
            }
        }
    }

    private static String getCanonicalName( String pName )
    {
        return pName.replace( '/', File.separatorChar ).replace('\\', File.separatorChar );
    }

    private String asPattern( String pPattern )
    {
        String pattern = getCanonicalName( pPattern.trim() );
        if ( pattern.endsWith( File.separator ) )
        {
            pattern += "**";
        }
        return pattern;
    }

    /**
     * Returns the list of include patterns to use.
     *
     * @return A list of include patterns.
     *         May be <code>null</code>, indicating that all files
     *         should be included. If a non-<code>null</code>
     *         list is given, all elements must be
     *         non-<code>null</code>.
     */
    public String[] getIncludes()
    {
        return includes;
    }

    /**
     * Sets the list of exclude patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param excludes A list of exclude patterns.
     *                 May be <code>null</code>, indicating that no files
     *                 should be excluded. If a non-<code>null</code> list is
     *                 given, all elements must be non-<code>null</code>.
     */
    public void setExcludes( String[] excludes )
    {
        this.excludes = null;
        final String[] defaultExcludes = useDefaultExcludes ? FileUtils.getDefaultExcludes() : ZERO_EXCLUDES;
        if ( excludes == null )
        {
            computedExcludes = defaultExcludes;
        }
        else
        {
            computedExcludes = new String[excludes.length + defaultExcludes.length];
            this.excludes = new String[excludes.length];
            for ( int i = 0; i < excludes.length; i++ )
            {
                computedExcludes[i] = asPattern( excludes[i] );
            }
            if ( defaultExcludes.length > 0 )
            {
                System.arraycopy( defaultExcludes, 0, computedExcludes, excludes.length, defaultExcludes.length );
            }
        }
    }

    /**
     * Returns the list of exclude patterns to use.
     *
     * @return A list of exclude patterns.
     *         May be <code>null</code>, indicating that no files
     *         should be excluded. If a non-<code>null</code> list is
     *         given, all elements must be non-<code>null</code>.
     */
    public String[] getExcludes()
    {
        return excludes;
    }

    /**
     * Tests, whether the given pattern is matching the given name.
     * @param pattern The pattern to match
     * @param name The name to test
     * @param isCaseSensitive Whether the pattern is case sensitive.
     * @return True, if the pattern matches, otherwise false
     */
    protected boolean matchPath( String pattern, String name,
                                 boolean isCaseSensitive )
    {
        return SelectorUtils.matchPath( pattern, name, isCaseSensitive );
    }

    /**
     * Tests whether or not a name matches against at least one include
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         include pattern, or <code>false</code> otherwise.
     */
    protected boolean isIncluded( String name )
    {
        for ( int i = 0; i < computedIncludes.length; i++ )
        {
            if ( matchPath( computedIncludes[i], name, isCaseSensitive ) )
            {
                return true;
            }
        }
        return false;
    }

    public boolean isSelected( PlexusIoResource plexusIoResource ) throws IOException
    {
        final String name = getCanonicalName( plexusIoResource.getName() );
        return isIncluded( name ) && !isExcluded( name );
    }

    /**
     * Returns, whether the include/exclude patterns are case sensitive.
     * @return True, if the patterns are case sensitive (default), or false.
     */
    public boolean isCaseSensitive()
    {
        return isCaseSensitive;
    }

    /**
     * Sets, whether the include/exclude patterns are case sensitive.
     * @param caseSensitive True, if the patterns are case sensitive (default), or false.
     */
    public void setCaseSensitive( boolean caseSensitive )
    {
        isCaseSensitive = caseSensitive;
    }

    /**
     * Returns, whether to use the default excludes, as specified by
     * {@link FileUtils#getDefaultExcludes()}.
     */
    public boolean isUseDefaultExcludes()
    {
        return useDefaultExcludes;
    }

    /**
     * Sets, whether to use the default excludes, as specified by
     * {@link FileUtils#getDefaultExcludes()}.
     */
    public void setUseDefaultExcludes( boolean pUseDefaultExcludes )
    {
        useDefaultExcludes = pUseDefaultExcludes;
        setExcludes( excludes );
    }
}
