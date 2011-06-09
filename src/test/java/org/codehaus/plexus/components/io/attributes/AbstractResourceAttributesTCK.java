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

import junit.framework.TestCase;

public abstract class AbstractResourceAttributesTCK
    extends TestCase
{
    
    protected AbstractResourceAttributesTCK()
    {
    }
    
    protected abstract PlexusIoResourceAttributes newAttributes();
    
    public void testSetAndGetUserId()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.hasUserId());
        
        int uid = 501;
        attrs.setUserId( uid );
        
        assertTrue( attrs.hasUserId());
        assertEquals( uid, attrs.getUserId() );
    }
    
    public final void testSetAndGetGroupId()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.hasGroupId());

        int gid = 501;
        attrs.setGroupId( gid );
        
        assertTrue( attrs.hasGroupId());
        assertEquals( gid, attrs.getGroupId() );
    }
    
    public final void testSetAndGetUserName()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertNull( attrs.getUserName() );
        
        String name = "me";
        attrs.setUserName( name );
        
        assertEquals( name, attrs.getUserName() );
    }
    
    public final void testSetAndGetGroupName()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertNull( attrs.getGroupName() );
        
        String name = "me";
        attrs.setGroupName( name );
        
        assertEquals( name, attrs.getGroupName() );
    }
    
    public final void testSetAndGetOwnerReadable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isOwnerReadable() );
        
        attrs.setOwnerReadable( true );
        
        assertTrue( attrs.isOwnerReadable() );
    }
    
    public final void testSetAndGetOwnerWritable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isOwnerWritable() );
        
        attrs.setOwnerWritable( true );
        
        assertTrue( attrs.isOwnerWritable() );
    }
    
    public final void testSetAndGetOwnerExecutable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isOwnerExecutable() );
        
        attrs.setOwnerExecutable( true );
        
        assertTrue( attrs.isOwnerExecutable() );
    }
    
    public final void testSetAndGetGroupReadable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isGroupReadable() );
        
        attrs.setGroupReadable( true );
        
        assertTrue( attrs.isGroupReadable() );
    }
    
    public final void testSetAndGetGroupWritable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isGroupWritable() );
        
        attrs.setGroupWritable( true );
        
        assertTrue( attrs.isGroupWritable() );
    }
    
    public final void testSetAndGetGroupExecutable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isGroupExecutable() );
        
        attrs.setGroupExecutable( true );
        
        assertTrue( attrs.isGroupExecutable() );
    }
    
    public final void testSetAndGetWorldReadable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isWorldReadable() );
        
        attrs.setWorldReadable( true );
        
        assertTrue( attrs.isWorldReadable() );
    }
    
    public final void testSetAndGetWorldWritable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isWorldWritable() );
        
        attrs.setWorldWritable( true );
        
        assertTrue( attrs.isWorldWritable() );
    }
    
    public final void testSetAndGetWorldExecutable()
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isWorldExecutable() );
        
        attrs.setWorldExecutable( true );
        
        assertTrue( attrs.isWorldExecutable() );
    }
    
    public final void testSetOctalModeString_OwnerModes()
    {
        verifyStringOctalModeSet( "700", new boolean[]{ true, true, true, false, false, false, false, false, false } );
        verifyStringOctalModeSet( "600", new boolean[]{ true, true, false, false, false, false, false, false, false } );
        verifyStringOctalModeSet( "400", new boolean[]{ true, false, false, false, false, false, false, false, false } );
        verifyStringOctalModeSet( "200", new boolean[]{ false, true, false, false, false, false, false, false, false } );
    }

    public final void testSetOctalModeString_GroupModes()
    {
        verifyStringOctalModeSet( "070", new boolean[]{ false, false, false, true, true, true, false, false, false } );
        verifyStringOctalModeSet( "060", new boolean[]{ false, false, false, true, true, false, false, false, false } );
        verifyStringOctalModeSet( "040", new boolean[]{ false, false, false, true, false, false, false, false, false } );
        verifyStringOctalModeSet( "020", new boolean[]{ false, false, false, false, true, false, false, false, false } );
    }

    public final void testSetOctalModeString_WorldModes()
    {
        verifyStringOctalModeSet( "007", new boolean[]{ false, false, false, false, false, false, true, true, true } );
        verifyStringOctalModeSet( "006", new boolean[]{ false, false, false, false, false, false, true, true, false } );
        verifyStringOctalModeSet( "004", new boolean[]{ false, false, false, false, false, false, true, false, false } );
        verifyStringOctalModeSet( "002", new boolean[]{ false, false, false, false, false, false, false, true, false } );
    }

    public final void testSetOctalMode_OwnerModes()
    {
        verifyOctalModeSet( "700", new boolean[]{ true, true, true, false, false, false, false, false, false } );
        verifyOctalModeSet( "600", new boolean[]{ true, true, false, false, false, false, false, false, false } );
        verifyOctalModeSet( "400", new boolean[]{ true, false, false, false, false, false, false, false, false } );
        verifyOctalModeSet( "200", new boolean[]{ false, true, false, false, false, false, false, false, false } );
    }

    public final void testSetOctalMode_GroupModes()
    {
        verifyOctalModeSet( "070", new boolean[]{ false, false, false, true, true, true, false, false, false } );
        verifyOctalModeSet( "060", new boolean[]{ false, false, false, true, true, false, false, false, false } );
        verifyOctalModeSet( "040", new boolean[]{ false, false, false, true, false, false, false, false, false } );
        verifyOctalModeSet( "020", new boolean[]{ false, false, false, false, true, false, false, false, false } );
    }

    public final void testSetOctalMode_WorldModes()
    {
        verifyOctalModeSet( "007", new boolean[]{ false, false, false, false, false, false, true, true, true } );
        verifyOctalModeSet( "006", new boolean[]{ false, false, false, false, false, false, true, true, false } );
        verifyOctalModeSet( "004", new boolean[]{ false, false, false, false, false, false, true, false, false } );
        verifyOctalModeSet( "002", new boolean[]{ false, false, false, false, false, false, false, true, false } );
    }

    private void verifyStringOctalModeSet( String mode, boolean[] checkValues )
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isOwnerReadable() );
        assertFalse( attrs.isOwnerWritable() );
        assertFalse( attrs.isOwnerExecutable() );
        
        assertFalse( attrs.isGroupReadable() );
        assertFalse( attrs.isGroupWritable() );
        assertFalse( attrs.isGroupExecutable() );
        
        assertFalse( attrs.isWorldReadable() );
        assertFalse( attrs.isWorldWritable() );
        assertFalse( attrs.isWorldExecutable() );
        
        attrs.setOctalModeString( mode );
        
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
    
    private void verifyOctalModeSet( String mode, boolean[] checkValues )
    {
        PlexusIoResourceAttributes attrs = newAttributes();
        
        assertFalse( attrs.isOwnerReadable() );
        assertFalse( attrs.isOwnerWritable() );
        assertFalse( attrs.isOwnerExecutable() );
        
        assertFalse( attrs.isGroupReadable() );
        assertFalse( attrs.isGroupWritable() );
        assertFalse( attrs.isGroupExecutable() );
        
        assertFalse( attrs.isWorldReadable() );
        assertFalse( attrs.isWorldWritable() );
        assertFalse( attrs.isWorldExecutable() );
        
        attrs.setOctalMode( Integer.parseInt( mode, 8 ) );
        
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