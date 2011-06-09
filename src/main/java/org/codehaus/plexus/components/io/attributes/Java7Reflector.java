package org.codehaus.plexus.components.io.attributes;

/*
 * Copyright 2011 The Codehaus Foundation.
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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * This class is used *only* because we need to build plexus-io with
 * java 1.5/1.6.  Once we require 1.7 to build plexus-io the reflection
 * can be removed.
 *
 * @author Kristian Rosenvold
 */
public class Java7Reflector
{
    public static final Method pathMethod;

    public static final Method readAttributes;

    public static final Class posixFileAttributes;

    private static final Object[] arr;

    private static final Method ownerMethod;

    private static final Method groupMethod;

    private static final Method permissionsMethod;

    private static final Method userPrincipalGetNameMethod;

    private static Method posixFilePermissionsToStringMethod;

    private static final boolean isJava7;

    static
    {
        boolean isJava7x = true;
        Method posixFilePermisssionsMethod = null;
        Method userPrincipalGetNameMethodLocal = null;
        Method readAttributesLocal = null;
        Class<?> posixFileAttributesLocal = null;
        Method ownerMethodLocal = null;
        Method groupMethodLocal = null;
        Method permissionsMethodLocal = null;
        Object[] arrLocal = null;
        Method pathMethodLocal = null;
        try
        {
            Class files = Class.forName( "java.nio.file.Files" );
            Class path = Class.forName( "java.nio.file.Path" );
            Class linkOption = Class.forName( "java.nio.file.LinkOption" );
            Class userPrincipal = Class.forName( "java.nio.file.attribute.UserPrincipal" );
            Class posixFilePermisssions = Class.forName( "java.nio.file.attribute.PosixFilePermissions" );
            posixFileAttributesLocal = Class.forName( "java.nio.file.attribute.PosixFileAttributes" );
            pathMethodLocal  = File.class.getMethod( "toPath", new Class[]{ } );

            posixFilePermisssionsMethod = posixFilePermisssions.getMethod( "toString", Set.class );
            userPrincipalGetNameMethodLocal = userPrincipal.getMethod( "getName" );
            final Object[] enumConstants = linkOption.getEnumConstants();
            Enum NOFOLLOW_LINKS = (Enum) enumConstants[0];

            arrLocal = (Object[]) Array.newInstance( linkOption, 1 );
            arrLocal[0] = NOFOLLOW_LINKS;

            Class[] params = { path, Class.class, arrLocal.getClass() };
            readAttributesLocal = files.getMethod( "readAttributes", params );
            ownerMethodLocal = posixFileAttributesLocal.getMethod( "owner" );
            groupMethodLocal = posixFileAttributesLocal.getMethod( "group" );
            permissionsMethodLocal = posixFileAttributesLocal.getMethod( "permissions" );
        }
        catch ( Exception e )
        {
            isJava7x = false;
        }
        isJava7 = isJava7x;
        posixFilePermissionsToStringMethod = posixFilePermisssionsMethod;
        userPrincipalGetNameMethod = userPrincipalGetNameMethodLocal;
        readAttributes = readAttributesLocal;
        posixFileAttributes = posixFileAttributesLocal;
        ownerMethod = ownerMethodLocal;
        groupMethod = groupMethodLocal;
        permissionsMethod = permissionsMethodLocal;
        arr = arrLocal;
        pathMethod = pathMethodLocal;

    }


    @SuppressWarnings( { "NullableProblems" } )
    public static Object getPosixFileAttributes( File file )
        throws IOException
    {
/*        final Path path = file.toPath();

        final PosixFileAttributes posixFileAttributes =
            Files.readAttributes( path, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS );
  */
        final Object path = invoke( pathMethod, file );

        return invoke( readAttributes, null, path, posixFileAttributes, arr );
    }

    /*
          final UserPrincipal ownerUserPrincipal = posixFileAttributes.owner();
          this.userName = ownerUserPrincipal.getName();
          this.groupName = posixFileAttributes.group().getName();

          final Set<PosixFilePermission> permissions = posixFileAttributes.permissions();
          mode = PosixFilePermissions.toString( permissions ).toCharArray();


    */
    public static String getOwnerUserName( Object posixFileAttributes )
    {
        /*
              final UserPrincipal ownerUserPrincipal = posixFileAttributes.owner();
              this.userName = ownerUserPrincipal.getName();
        */
        final Object invoke = invoke( ownerMethod, posixFileAttributes );
        return (String) invoke( userPrincipalGetNameMethod, invoke );
    }

    public static String getOwnerGroupName( Object posixFileAttributes )
    {
        /*
              this.groupName = posixFileAttributes.group().getName();
        */
        final Object invoke = invoke( groupMethod, posixFileAttributes );
        return (String) invoke( userPrincipalGetNameMethod, invoke );
    }

    public static String getPermissions( Object posixFileAttributes )
    {
        /*
              final Set<PosixFilePermission> permissions = posixFileAttributes.permissions();
              mode = PosixFilePermissions.toString( permissions ).toCharArray();
        */
        final Set permissions = (Set) invoke( permissionsMethod, posixFileAttributes );
        return (String) invoke( posixFilePermissionsToStringMethod, posixFileAttributes, permissions );
    }

    private static Object invoke( Method method, Object obj, Object... args )
    {
        try
        {
            return method.invoke( obj, args );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
        catch ( InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
    }


    public static boolean isJava7()
    {
        return isJava7;
    }
}
