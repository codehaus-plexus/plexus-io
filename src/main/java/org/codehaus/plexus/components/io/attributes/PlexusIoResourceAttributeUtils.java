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

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.*;

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

        if ( def != null && def.getGroupId() >= 0 && (result.getGroupId() == null || result.getGroupId() < 0) )
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
        Map byPath = getFileAttributesByPath( file, null, Logger.LEVEL_DEBUG, false, true );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    @SuppressWarnings( { "UnusedDeclaration" } )
    public static PlexusIoResourceAttributes getFileAttributes( File file, Logger logger )
        throws IOException
    {
        Map byPath = getFileAttributesByPath( file, logger, Logger.LEVEL_DEBUG, false, true );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    @SuppressWarnings( { "UnusedDeclaration" } )
    public static PlexusIoResourceAttributes getFileAttributes( File file, Logger logger, int logLevel )
        throws IOException
    {
        Map byPath = getFileAttributesByPath( file, logger, logLevel, false, true );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir )
        throws IOException
    {
        return getFileAttributesByPath( dir, null, Logger.LEVEL_DEBUG, true, true );
    }

    @SuppressWarnings( { "UnusedDeclaration" } )
    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir, Logger logger )
        throws IOException
    {
        return getFileAttributesByPath( dir, logger, Logger.LEVEL_DEBUG, true, true );
    }

    @SuppressWarnings( "UnusedParameters" )
    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir, Logger logger,
                                                                                   int logLevel )
        throws IOException
    {
        return getFileAttributesByPath( dir, logger, Logger.LEVEL_DEBUG, true, true );
    }

    public static Map<String, PlexusIoResourceAttributes> getFileAttributesByPath( File dir, Logger logger,
                                                                                   int logLevel, boolean recursive,
                                                                                   boolean includeNumericUserId )
        throws IOException
    {
        if ( !enabledOnCurrentOperatingSystem() )
        {
            //noinspection unchecked
            return Collections.emptyMap();
        }

        if ( Java7Reflector.isJava7() )
        {
            return getFileAttributesByPathJava7( dir );
        }

        if ( logger == null )
        {
            logger = new ConsoleLogger( Logger.LEVEL_INFO, "Internal" );
        }
        LoggerStreamConsumer loggerConsumer = new LoggerStreamConsumer( logger, logLevel );

        AttributeParser.NumericUserIDAttributeParser numericIdParser = null;
        FutureTask<Integer> integerFutureTask = null;
        Commandline numericCli = null;
        if ( includeNumericUserId )
        {
            numericIdParser = new AttributeParser.NumericUserIDAttributeParser( loggerConsumer, logger );

            String lsOptions1 = "-1nla" + ( recursive ? "R" : "d" );
            try
            {
                numericCli = setupCommandLine( dir, lsOptions1, logger );

                CommandLineCallable commandLineCallable =
                    CommandLineUtils.executeCommandLineAsCallable( numericCli, null, numericIdParser, loggerConsumer,
                                                                   0 );

                integerFutureTask = new FutureTask<Integer>( commandLineCallable );
                new Thread( integerFutureTask ).start();
            }
            catch ( CommandLineException e )
            {
                IOException error = new IOException( "Failed to quote directory: '" + dir + "'" );
                error.initCause( e );
                throw error;
            }
        }

        AttributeParser.SymbolicUserIDAttributeParser userId =
            getNameBasedParser( dir, logger, recursive, loggerConsumer );

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
                throw new IOException(
                    "Failed to retrieve numeric file attributes using: '" + numericCli.toString() + "'" );
            }
        }

        return userId.merge( numericIdParser );
    }

    private static AttributeParser.SymbolicUserIDAttributeParser getNameBasedParser( File dir, Logger logger,
                                                                                     boolean recursive,
                                                                                     LoggerStreamConsumer loggerConsumer )
        throws IOException
    {
        AttributeParser.SymbolicUserIDAttributeParser userId =
            new AttributeParser.SymbolicUserIDAttributeParser( loggerConsumer, logger );

        String lsOptions2 = "-1la" + ( recursive ? "R" : "d" );
        try
        {
            executeLs( dir, lsOptions2, loggerConsumer, userId, logger );
        }
        catch ( CommandLineException e )
        {
            IOException error = new IOException( "Failed to quote directory: '" + dir + "'" );
            error.initCause( e );

            throw error;
        }
        return userId;
    }

    private static Map<String, PlexusIoResourceAttributes> getFileAttributesByPathJava7( File dir )
        throws IOException
    {
        Map<Integer, String> userCache = new HashMap<Integer, String>();
        Map<Integer, String> groupCache = new HashMap<Integer, String>();
        final List fileAndDirectoryNames;
        if ( dir.isDirectory() )
        {
            // Seems like we're always recursive. Need to check that out wrt non-recusive use cases.
            fileAndDirectoryNames = FileUtils.getFileAndDirectoryNames( dir, null, null, true, true, true, true );
        }
        else
        {
            fileAndDirectoryNames = Collections.singletonList( dir.getAbsolutePath() );
        }

        final Map<String, PlexusIoResourceAttributes> attributesByPath =
            new LinkedHashMap<String, PlexusIoResourceAttributes>();

        for ( Object fileAndDirectoryName : fileAndDirectoryNames )
        {
            String fileName = (String) fileAndDirectoryName;
            attributesByPath.put( fileName, new Java7FileAttributes( new File( fileName ), userCache, groupCache ) );
        }
        return attributesByPath;
    }


    private static boolean enabledOnCurrentOperatingSystem()
    {
        return !Os.isFamily( Os.FAMILY_WINDOWS ) && !Os.isFamily( Os.FAMILY_WIN9X );
    }


    private static void executeLs( File dir, String options, LoggerStreamConsumer loggerConsumer, StreamConsumer parser,
                                   Logger logger )
        throws IOException, CommandLineException
    {
        Commandline numericCli = setupCommandLine( dir, options, logger );

        try
        {
            int result = CommandLineUtils.executeCommandLine( numericCli, parser, loggerConsumer );

            if ( result != 0 )
            {
                throw new IOException(
                    "Failed to retrieve numeric file attributes using: '" + numericCli.toString() + "'" );
            }
        }
        catch ( CommandLineException e )
        {
            IOException error =
                new IOException( "Failed to retrieve numeric file attributes using: '" + numericCli.toString() + "'" );
            error.initCause( e );

            throw error;
        }
    }

    private static Commandline setupCommandLine( File dir, String options, Logger logger )
    {
        Commandline numericCli = new Commandline();

        numericCli.getShell().setQuotedArgumentsEnabled( true );
        numericCli.getShell().setQuotedExecutableEnabled( false );

        numericCli.setExecutable( "ls" );

        numericCli.createArg().setLine( options );

        numericCli.createArg().setValue( dir.getAbsolutePath() );

        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Executing:\n\n" + numericCli.toString() + "\n" );
        }
        return numericCli;
    }


    private static final class LoggerStreamConsumer
        implements StreamConsumer
    {
        private final Logger logger;

        private final int level;

        public LoggerStreamConsumer( Logger logger, int level )
        {
            this.logger = logger;
            this.level = level;
        }

        public void consumeLine( String line )
        {
            switch ( level )
            {
                case Logger.LEVEL_DEBUG:
                    logger.debug( line );
                    break;
                case Logger.LEVEL_ERROR:
                    logger.error( line );
                    break;
                case Logger.LEVEL_FATAL:
                    logger.fatalError( line );
                    break;
                case Logger.LEVEL_WARN:
                    logger.warn( line );
                    break;
                default:
                    logger.info( line );
                    break;
            }
        }
    }

    static Pattern totalLinePattern = Pattern.compile( "\\w*\\s\\d*" );
}
