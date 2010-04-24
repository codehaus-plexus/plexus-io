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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

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
            result = new SimpleResourceAttributes( base.getUserId(), base.getUserName(), base.getGroupId(), base.getGroupName(),
                                          base.getOctalMode() );
        }
        
        if ( override.getGroupId() != -1 )
        {
            result.setGroupId( override.getGroupId() );
        }
        
        if ( def != null && result.getGroupId() < 0 )
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
        
        if ( override.getUserId() != -1 )
        {
            result.setUserId( override.getUserId() );
        }
        
        if ( def != null && result.getUserId() < 0 )
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

    public static PlexusIoResourceAttributes getFileAttributes( File file )
        throws IOException
    {
        Map byPath = getFileAttributesByPath( file, null, Logger.LEVEL_DEBUG, false );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    public static PlexusIoResourceAttributes getFileAttributes( File file, Logger logger )
        throws IOException
    {
        Map byPath = getFileAttributesByPath( file, logger, Logger.LEVEL_DEBUG, false );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    public static PlexusIoResourceAttributes getFileAttributes( File file, Logger logger, int logLevel )
        throws IOException
    {
        Map byPath = getFileAttributesByPath( file, logger, logLevel, false );
        return (PlexusIoResourceAttributes) byPath.get( file.getAbsolutePath() );
    }

    public static Map getFileAttributesByPath( File dir )
        throws IOException
    {
        return getFileAttributesByPath( dir, null, Logger.LEVEL_DEBUG, true );
    }

    public static Map getFileAttributesByPath( File dir, Logger logger )
        throws IOException
    {
        return getFileAttributesByPath( dir, logger, Logger.LEVEL_DEBUG, true );
    }

    public static Map getFileAttributesByPath( File dir, Logger logger, int logLevel )
        throws IOException
    {
        return getFileAttributesByPath( dir, logger, Logger.LEVEL_DEBUG, true );
    }
    
    public static Map getFileAttributesByPath( File dir, Logger logger, int logLevel, boolean recursive )
        throws IOException
    {
        if ( !enabledOnCurrentOperatingSystem() )
        {
            return Collections.EMPTY_MAP;
        }

        if ( logger == null )
        {
            logger = new ConsoleLogger( Logger.LEVEL_INFO, "Internal" );
        }
        LoggerStreamConsumer loggerConsumer = new LoggerStreamConsumer( logger, logLevel );

        AttributeParser parser = new AttributeParser( loggerConsumer, logger );
        
        String lsOptions = "-1nla" + ( recursive ? "R" : "d" );
        try
        {
            executeLs( dir, lsOptions, loggerConsumer, parser, logger );
        }
        catch ( CommandLineException e )
        {
            IOException error = new IOException( "Failed to quote directory: '" + dir + "'" );
            error.initCause( e );

            throw error;
        }

        parser.initSecondPass();
        lsOptions = "-1la" + ( recursive ? "R" : "d" );
        try
        {
            executeLs( dir, lsOptions, loggerConsumer, parser, logger );
        }
        catch ( CommandLineException e )
        {
            IOException error = new IOException( "Failed to quote directory: '" + dir + "'" );
            error.initCause( e );

            throw error;
        }

        return parser.attributesByPath;
    }

    private static boolean enabledOnCurrentOperatingSystem()
    {
        return !Os.isFamily( Os.FAMILY_WINDOWS ) && !Os.isFamily( Os.FAMILY_WIN9X );
    }

    private static void executeLs( File dir, String options, LoggerStreamConsumer loggerConsumer,
                                   StreamConsumer parser, Logger logger )
        throws IOException, CommandLineException
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

        try
        {
            int result = CommandLineUtils.executeCommandLine( numericCli, parser, loggerConsumer );

            if ( result != 0 )
            {
                throw new IOException( "Failed to retrieve numeric file attributes using: '" + numericCli.toString()
                    + "'" );
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

    private static final class LoggerStreamConsumer
        implements StreamConsumer
    {
        private Logger logger;

        private int level;

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


    private static final int[] LS_LAST_DATE_PART_INDICES = { 7, 7, 6 };


    static final class AttributeParser
        implements StreamConsumer
    {
        private final StreamConsumer delegate;

        private final Map attributesByPath = new LinkedHashMap();

        private final Logger logger;

        private boolean nextIsPathPrefix = false;

        private String pathPrefix = "";

        private boolean extractNames = false;

        private boolean secondPass = false;

        private final SimpleDateFormat[] LS_DATE_FORMATS;

        public AttributeParser( StreamConsumer delegate, Logger logger )
        {
            this.delegate = delegate;
            this.logger = logger;
            LS_DATE_FORMATS =
                new SimpleDateFormat[]{ new SimpleDateFormat( "MMM dd yyyy" ), new SimpleDateFormat( "MMM dd HH:mm" ),
                    new SimpleDateFormat( "yyyy-MM-dd HH:mm" ), };

        }

        public void consumeLine( String line )
        {
            if ( totalLinePattern.matcher( line ).matches() )
            {
                // skip it.
            }
            else if ( line.trim().length() == 0 )
            {
                nextIsPathPrefix = true;

                if ( logger.isDebugEnabled() )
                {
                    logger.debug( "Anticipating path prefix in next line" );
                }
            }
            else if ( nextIsPathPrefix )
            {
                if ( !line.endsWith( ":" ) )
                {
                    if ( logger.isDebugEnabled() )
                    {
                        logger.debug( "Path prefix not found. Checking next line." );
                    }
                }
                else
                {
                    nextIsPathPrefix = false;
                    pathPrefix = line.substring( 0, line.length() - 1 );

                    if ( !pathPrefix.endsWith( "/" ) )
                    {
                        pathPrefix += "/";
                    }

                    if ( logger.isDebugEnabled() )
                    {
                        logger.debug( "Set path prefix to: " + pathPrefix );
                    }
                }
            }
            else
            {
                String[] parts = line.split( "\\s+" );
                int lastDatePart = verifyParsability( line, parts, logger );

                if ( lastDatePart > 0 )
                {
                    int idx = line.indexOf( parts[lastDatePart] ) + parts[lastDatePart].length() + 1;

                    String path = pathPrefix + line.substring( idx );
                    while ( path.length() > 0 && Character.isWhitespace( path.charAt( 0 ) ) )
                    {
                        path = path.substring( 1 );
                    }

                    if ( logger.isDebugEnabled() )
                    {
                        logger.debug( "path: '" + path + "'" );
                        logger.debug( "mode: '" + parts[0] + "'" );
                        logger.debug( "uid: '" + parts[2] );
                        logger.debug( "gid: '" + parts[3] );
                    }

                    FileAttributes attributes;
                    synchronized ( attributesByPath )
                    {
                        if ( secondPass )
                        {
                            attributes = (FileAttributes) attributesByPath.get( path );
                        }
                        else
                        {
                            attributes = new FileAttributes();
                            attributes.setLsModeline( parts[0] );
                            attributesByPath.put( path, attributes );
                        }

                        if ( attributes != null )
                        {
                            if ( extractNames )
                            {
                                attributes.setUserName( parts[2] );
                                attributes.setGroupName( parts[3] );
                            }
                            else
                            {
                                attributes.setUserId( Integer.parseInt( parts[2] ) );
                                attributes.setGroupId( Integer.parseInt( parts[3] ) );
                            }
                        }
                    }
                }
            }

            delegate.consumeLine( line );
        }

        public void initSecondPass()
        {
            secondPass = true;
            extractNames = true;
            nextIsPathPrefix = false;
            pathPrefix = "";
        }

        public Map getAttributesByPath()
        {
            return attributesByPath;
        }

        private int verifyParsability( String line, String[] parts, Logger logger )
        {
            if ( parts.length > 7 )
            {
                String dateCandidate = parts[5] + " " + parts[6] + " " + parts[7];
                for ( int i = 0; i < LS_DATE_FORMATS.length; i++ )
                {
                    try
                    {
                        LS_DATE_FORMATS[i].parse( dateCandidate );
                        return LS_LAST_DATE_PART_INDICES[i];
                    }
                    catch ( ParseException e )
                    {
                        if ( logger.isDebugEnabled() )
                        {
                            logger.debug( "Failed to parse date: '" + dateCandidate + "' using format: " +
                                LS_DATE_FORMATS[i].toPattern(), e );
                        }
                    }
                }
            }

            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Unparseable line: '" + line +
                    "'\nReason: unrecognized date format; ambiguous start-index for path in listing." );
            }

            return -1;
        }

    }
}
