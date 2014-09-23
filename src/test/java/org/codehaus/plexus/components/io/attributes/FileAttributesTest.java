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


public class FileAttributesTest
    extends AbstractResourceAttributesTCK
{

    protected PlexusIoResourceAttributes newAttributes( int mode )
    {
        return new FileAttributes(mode);
    }

    protected PlexusIoResourceAttributes newAttributes( String lsModeLine )
    {
        return new FileAttributes(lsModeLine);
    }

    public void testSetLsMode_OwnerModes()
    {
        verifyLsModeSet( "-rwS------", new boolean[]{ true, true, true, false, false, false, false, false, false } );
        verifyLsModeSet( "-rwx------", new boolean[]{ true, true, true, false, false, false, false, false, false } );
        verifyLsModeSet( "-rw-------", new boolean[]{ true, true, false, false, false, false, false, false, false } );
        verifyLsModeSet( "-r--------", new boolean[]{ true, false, false, false, false, false, false, false, false } );
        verifyLsModeSet( "--w-------", new boolean[]{ false, true, false, false, false, false, false, false, false } );
    }

    public void testSetLsMode_GroupModes()
    {
        verifyLsModeSet( "----rwS---", new boolean[]{ false, false, false, true, true, true, false, false, false } );
        verifyLsModeSet( "----rwx---", new boolean[]{ false, false, false, true, true, true, false, false, false } );
        verifyLsModeSet( "----rw----", new boolean[]{ false, false, false, true, true, false, false, false, false } );
        verifyLsModeSet( "----r-----", new boolean[]{ false, false, false, true, false, false, false, false, false } );
        verifyLsModeSet( "-----w----", new boolean[]{ false, false, false, false, true, false, false, false, false } );
    }

    public void testSetLsMode_WorldModes()
    {
        verifyLsModeSet( "-------rwx", new boolean[]{ false, false, false, false, false, false, true, true, true } );
        verifyLsModeSet( "-------rw-", new boolean[]{ false, false, false, false, false, false, true, true, false } );
        verifyLsModeSet( "-------r--", new boolean[]{ false, false, false, false, false, false, true, false, false } );
        verifyLsModeSet( "--------w-", new boolean[]{ false, false, false, false, false, false, false, true, false } );
    }

    private void verifyLsModeSet( String mode, boolean[] checkValues )
    {
        PlexusIoResourceAttributes attrs = newAttributes( mode );
        

        assertEquals( checkValues[0], attrs.isOwnerReadable() );
        assertEquals( checkValues[1], attrs.isOwnerWritable() );
        assertEquals( checkValues[2], attrs.isOwnerExecutable() );
        
        assertEquals( checkValues[3], attrs.isGroupReadable() );
        assertEquals( checkValues[4], attrs.isGroupWritable() );
        assertEquals( checkValues[5], attrs.isGroupExecutable() );
        
        assertEquals( checkValues[6], attrs.isWorldReadable() );
        assertEquals( checkValues[7], attrs.isWorldWritable() );
        assertEquals( checkValues[8], attrs.isWorldExecutable() );
    }
    
}