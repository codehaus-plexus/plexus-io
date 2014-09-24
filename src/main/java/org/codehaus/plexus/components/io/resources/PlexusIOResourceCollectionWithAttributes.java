package org.codehaus.plexus.components.io.resources;

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

public interface PlexusIOResourceCollectionWithAttributes
{

    /**
     * Sets the file and directory attributes to use as defaults.
     * 
     * @param uid
     * @param userName
     * @param gid 
     * @param groupName
     * @param fileMode The octal mode to use for files
     * @param dirMode The octal mode to use for directories
     */
  //  void setDefaultAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode );
    
    /**
     * Sets the file and directory attributes to use as overrides.
     * 
     * @param uid
     * @param userName
     * @param gid 
     * @param groupName
     * @param fileMode The octal mode to use for files
     * @param dirMode The octal mode to use for directories
     */
 //   void setOverrideAttributes( int uid, String userName, int gid, String groupName, int fileMode, int dirMode );
    
}