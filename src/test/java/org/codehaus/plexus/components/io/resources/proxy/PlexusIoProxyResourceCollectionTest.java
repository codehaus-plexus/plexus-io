package org.codehaus.plexus.components.io.resources.proxy;

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

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.components.io.resources.proxy.PlexusIoProxyResourceCollection;


/**
 * Test case for {@link PlexusIoProxyResourceCollection}.
 */
public class PlexusIoProxyResourceCollectionTest extends PlexusTestCase
{
    private final String [] SAMPLE_INCLUDES = {"junk.*", "test/**", "dir*/file.xml"};
    
    private final String [] SAMPLE_EXCLUDES = {"*.junk", "somwhere/**"};
    
    public void testGetDefaultFileSelector() throws Exception
    {
        PlexusIoProxyResourceCollection resCol = new PlexusIoProxyResourceCollection(null);

        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();

        resCol.setIncludes( SAMPLE_INCLUDES );
        resCol.setExcludes( SAMPLE_EXCLUDES );
        
        // This will throw an exception if there is a bug
        resCol.getDefaultFileSelector();
        
    }
}