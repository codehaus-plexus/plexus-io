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

public interface PlexusIoResourceAttributes
{
    boolean isOwnerReadable();
    
    boolean isOwnerWritable();
    
    boolean isOwnerExecutable();
    
    boolean isGroupReadable();

    boolean isGroupWritable();
    
    boolean isGroupExecutable();
    
    boolean isWorldReadable();

    boolean isWorldWritable();
    
    boolean isWorldExecutable();

    /**
     * Gets the unix user id.
     * @return The unix user id, may be null ("not set"), even on unix
     */
    Integer getUserId();
    
    /**
     * Gets the unix group id.
     * @return The unix group id, may be null ("not set"), even on unix
     */
    Integer getGroupId();

    String getUserName();
    
    String getGroupName();
    
    int getOctalMode();
    
    String getOctalModeString();

    /**
     * Indicates if this is a symnbolic link element.
     * For file-based resource attributes this value may be always "false" for versions prior to java7.
     * @return True if the file is a symlink or false if not.
     */
    boolean isSymbolicLink();
}