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
import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.util.Os;

import junit.framework.TestCase;

/**
 * @author Kristian Rosenvold
 */
public class FileAttributesTest
    extends TestCase
{
    public void testGetPosixFileAttributes()
        throws Exception
    {

        if ( Os.isFamily( Os.FAMILY_WINDOWS ) )
        {
            return;
        }

        File file = new File( "." );
        Map<Integer, String> userCache = new HashMap<>();
        Map<Integer, String> groupCache = new HashMap<>();

        PlexusIoResourceAttributes fa = new FileAttributes( file, userCache, groupCache );
        assertNotNull( fa );
    }

}
