package org.codehaus.plexus.components.io.attributes;

/*
 * Copyright 2007 The Codehaus Foundation.
 *
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

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineCallable;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

@SuppressWarnings( { "NullableProblems" } )
public final class PlexusIoResourceAttributeUtils
{

    private PlexusIoResourceAttributeUtils()
    {
    }


    public static PlexusIoResourceAttributes mergeAttributes( PlexusIoResourceAttributes override,
                                                              PlexusIoResourceAttributes base,
                                                              PlexusIoResourceAttributes def )
    {
        if ( override == null )
        {
            return base;
        }
        SimpleResourceAttributes result;
        if ( base == null )
        {
            result = new SimpleResourceAttributes();
        }
        else
        {
            result = new SimpleResourceAttributes( base.getUserId(), base.getUserName(), base.getGroupId(),
                                                   base.getGroupName(), base.getOctalMode() );
        }

        if ( override.getGroupId() != null && override.getGroupId() != -1 )
        {
            result.setGroupId( override.getGroupId() );
        }

        if ( def != null && def.getGroupId() >= 0 && ( result.getGroupId() == null || result.getGroupId() < 0 ) )
        {
            result.setGroupId( def.getGroupId() );
        }

        if ( override.getGroupName() != null )
        {
            result.setGroupName( override.getGroupName() );
        }

        if ( def != null && result.getGroupName() == null )
        {
            result.setGroupName( def.getGroupName() );
        }

        if ( override.getUserId() != null && override.getUserId() != -1 )
        {
            result.setUserId( override.getUserId() );
        }

        if ( def != null && def.getUserId() >= 0 && ( result.getUserId() == null || result.getUserId() < 0 ) )
        {
            result.setUserId( def.getUserId() );
        }

        if ( override.getUserName() != null )
        {
            result.setUserName( override.getUserName() );
        }

        if ( def != null && result.getUserName() == null )
        {
            result.setUserName( def.getUserName() );
        }

        if ( override.getOctalMode() > 0 )
        {
            result.setOctalMode( override.getOctalMode() );
        }

        if ( def != null && result.getOctalMode() < 0 )
        {
            result.setOctalMode( def.getOctalMode() );
        }

        return result;
    }

    public static boolean isGroupExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_EXECUTE );
    }

    public static boolean isGroupReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_READ );
    }

    public static boolean isGroupWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_GROUP_WRITE );
    }

    public static boolean isOwnerExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_EXECUTE );
    }

    public static boolean isOwnerReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_READ );
    }

    public static boolean isOwnerWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_OWNER_WRITE );
    }

    public static boolean isWorldExecutableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_EXECUTE );
    }

    public static boolean isWorldReadableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_READ );
    }

    public static boolean isWorldWritableInOctal( int mode )
    {
        return isOctalModeEnabled( mode, AttributeConstants.OCTAL_WORLD_WRITE );
    }

    public static boolean isOctalModeEnabled( int mode, int targetMode )
    {
        return ( mode & targetMode ) != 0;
    }

    @SuppressWarnings( { "UnusedDeclaration" } )
    public static PlexusIoResourceAttributes getFileAttributes( File file )
        throws IOException
    {
        Map<String, PlexusIoResourceAttributes> byPath = getFileAttributesByPath( file, false, true );
        final PlexusIoResourceAttributes o = byPath.get( file.getAbsolutePath() );
        if ( o == null )
        {
            // We're on a crappy old java version (5) or the OS from hell. Just "fail".
            return SimpleResourceAttributes.lastResortDummyAttributesForBrokenOS();

        }
        return o;
    }

    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir )
        throws IOException
    {
        return getFileAttributesByPath( dir, true, true );
    }

    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir, boolean recursive,
                                                                                   boolean includeNumericUserId )
        throws IOException
    {
        if ( Java7Reflector.isAtLeastJava7() )
        {
            return getFileAttributesByPathJava7( dir, recursive );
        }

        if ( !enabledOnCurrentOperatingSystem() )
        {
            //noinspection unchecked
            return Collections.emptyMap();
        }

        return getFileAttributesByPathScreenScrape( dir, recursive, includeNumericUserId );
    }

    public static void main( String[] args )
        throws IOException
    {
        if ( args.length < 0 )
        {
            System.out.println( "You must supply one directory to scan:" );
            return;
        }
        File dir = new File( args[0] );
        final Map<String, PlexusIoResourceAttributes> fileAttributesByPathScreenScrape =
            getFileAttributesByPathScreenScrape( dir, true, true );
        for ( String s : fileAttributesByPathScreenScrape.keySet() )
        {
            System.out.println( s + ":" + fileAttributesByPathScreenScrape.get( s ) );
        }


    }

    static Map<String, PlexusIoResourceAttributes> getFileAttributesByPathScreenScrape( File dir, boolean recursive,
                                                                                        boolean includeNumericUserId )
        throws IOException
    {
        StringBuilder loggerCache = new StringBuilder();
        StreamConsumer logger = createStringBuilderStreamConsumer( loggerCache );

        AttributeParser.NumericUserIDAttributeParser numericIdParser = null;
        FutureTask<Integer> integerFutureTask = null;
        Commandline numericCli;
        if ( includeNumericUserId )
        {
            numericIdParser = new AttributeParser.NumericUserIDAttributeParser( logger );

            String lsOptions1 = "-1nla" + ( recursive ? "R" : "d" );
            StreamConsumer stdErr = new ErrorMessageStreamConsumer();
            try
            {
                numericCli = setupCommandLine( dir, lsOptions1, logger );

                CommandLineCallable commandLineCallable =
                    CommandLineUtils.executeCommandLineAsCallable( numericCli, null, numericIdParser, stdErr, 0 );

                integerFutureTask = new FutureTask<Integer>( commandLineCallable );
                new Thread( integerFutureTask ).start();
            }
            catch ( CommandLineException e )
            {
                IOException error = new IOException(
                    "Failed to quote directory: '" + dir + "'\n" + stdErr.toString() + logger.toString() );
                error.initCause( e );
                throw error;
            }
        }

//        loggerCache.setLength( 0 );
        AttributeParser.SymbolicUserIDAttributeParser userId = getNameBasedParser( dir, logger, recursive );

        if ( includeNumericUserId )
        {
            final Integer result;
            try
            {
                result = integerFutureTask.get();
            }
            catch ( InterruptedException e )
            {
                throw new RuntimeException( e );
            }
            catch ( ExecutionException e )
            {
                throw new RuntimeException( e );
            }

            if ( result != 0 )
            {
                throw new IOException( "Failed (3) to retrieve numeric file attributes using:\n" + logger.toString() );
            }
        }

        return userId.merge( numericIdParser );
    }

    private static AttributeParser.SymbolicUserIDAttributeParser getNameBasedParser( File dir, StreamConsumer logger,
                                                                                     boolean recursive )
        throws IOException
    {
        AttributeParser.SymbolicUserIDAttributeParser userId =
            new AttributeParser.SymbolicUserIDAttributeParser( logger );

        StreamConsumer stdErr = new ErrorMessageStreamConsumer();
        String lsOptions2 = "-1la" + ( recursive ? "R" : "d" );
        try
        {
            executeLs( dir, lsOptions2, userId, logger );
        }
        catch ( CommandLineException e )
        {
            IOException error =
                new IOException( "Failed to quote directory: '" + dir + "'\n" + stdErr.toString() + logger.toString() );
            error.initCause( e );

            throw error;
        }
        return userId;
    }

    private static @Nonnull Map<String, PlexusIoResourceAttributes> getFileAttributesByPathJava7( @Nonnull File dir, boolean recursive )
        throws IOException
    {
        Map<Integer, String> userCache = new HashMap<Integer, String>();
        Map<Integer, String> groupCache = new HashMap<Integer, String>();
        final List<String> fileAndDirectoryNames;
        if ( recursive && dir.isDirectory() )
        {
            fileAndDirectoryNames = FileUtils.getFileAndDirectoryNames( dir, null, null, true, true, true, true );
        }
        else
        {
            fileAndDirectoryNames = Collections.singletonList( dir.getAbsolutePath() );
        }

        final Map<String, PlexusIoResourceAttributes> attributesByPath =
            new LinkedHashMap<String, PlexusIoResourceAttributes>();

        for ( String fileAndDirectoryName : fileAndDirectoryNames )
        {
            attributesByPath.put( fileAndDirectoryName,
                                  new Java7FileAttributes( new File( fileAndDirectoryName ), userCache, groupCache ) );
        }
        return attributesByPath;
    }


    private static boolean enabledOnCurrentOperatingSystem()
    {
        return !Os.isFamily( Os.FAMILY_WINDOWS ) && !Os.isFamily( Os.FAMILY_WIN9X );
    }


    private static void executeLs( File dir, String options, StreamConsumer parser, StreamConsumer logger )
        throws IOException, CommandLineException
    {
        Commandline numericCli = setupCommandLine( dir, options, logger );

        StreamConsumer stdErr = new ErrorMessageStreamConsumer();
        try
        {
            int result = CommandLineUtils.executeCommandLine( numericCli, parser, stdErr );

            if ( result != 0 )
            {
                throw new IOException( stdErr.toString() +
                                           "When scraping numeric file attributes:\n" + logger.toString() );
            }
        }
        catch ( CommandLineException e )
        {
            IOException error = new IOException(
                "Failed (2) to retrieve numeric file attributes using:\n" + stdErr.toString() + "\n"
                    + logger.toString() );
            error.initCause( e );

            throw error;
        }
    }

    private static Commandline setupCommandLine( @Nonnull File dir, String options, StreamConsumer logger )
    {
        Commandline numericCli = new Commandline();

        numericCli.getShell().setQuotedArgumentsEnabled( true );
        numericCli.getShell().setQuotedExecutableEnabled( false );

        numericCli.setExecutable( "ls" );

        numericCli.createArg().setLine( options );

        numericCli.createArg().setValue( dir.getAbsolutePath() );

        logger.consumeLine( "\nExecuting: " + numericCli.toString() + "\n" );
        return numericCli;
    }

    static class ErrorMessageStreamConsumer
        implements StreamConsumer
    {

        StringBuilder errorOutput = new StringBuilder();

        public synchronized void consumeLine( String line )
        {
            errorOutput.append( line ).append( "\n" );
        }

        public synchronized String toString()
        {
            return errorOutput.toString();
        }
    }

    private static @Nonnull StreamConsumer createStringBuilderStreamConsumer( @Nonnull final StringBuilder sb )
    {
        return new StreamConsumer()
        {
            public synchronized void consumeLine( String line )
            {
                sb.append( line ).append( "\n" );
            }

            public synchronized String toString()
            {
                return sb.toString();
            }
        };
    }

    static final Pattern totalLinePattern = Pattern.compile( "\\w*\\s\\d*" );
}
