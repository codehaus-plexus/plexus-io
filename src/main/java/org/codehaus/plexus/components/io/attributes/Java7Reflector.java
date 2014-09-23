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

/**
 * This class is used *only* because we need to build plexus-io with
 * java 1.5/1.6.  Once we require 1.7 to build plexus-io the reflection
 * can be removed.
 *
 * @author Kristian Rosenvold
 */
public class Java7Reflector
{

    private static final boolean isJava7;

    static
    {
        boolean isJava7x = true;
        try
        {
            Class.forName( "java.nio.file.Files" );
        }
        catch ( Exception e )
        {
            isJava7x = false;
        }
        isJava7 = isJava7x;
    }


    public static boolean isAtLeastJava7()
    {
        return isJava7;
    }
}
