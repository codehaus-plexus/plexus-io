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

import javax.annotation.Nullable;

/*
 * A very simple pojo based PlexusIoResourceAttributes without any kind of backing
 */
public class UserGroupModeFileAttributes
    extends FileAttributes
{

    public UserGroupModeFileAttributes( Integer uid, String userName, Integer gid, String groupName, int mode, FileAttributes base )
    {
        super( uid, userName, gid, groupName, mode,
               base.isSymbolicLink(), base.isRegularFile(), base.isDirectory(), base.isOther(),
               base.getPermissions(), base.getSize(), base.getLastModifiedTime() );
    }

    public String toString()
    {
        return String.format(
            "%nResource Attributes:%n------------------------------%nuser: %s%ngroup: %s%nuid: %d%ngid: %d%nmode: %06o",
            getUserName() == null ? "" : getUserName(),
            getGroupName() == null ? "" : getGroupName(),
            getUserId() != null ? getUserId() : 0,
            getGroupId() != null ? getGroupId() : 0,
            getOctalMode() );
    }


}
