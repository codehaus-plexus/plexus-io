package org.codehaus.plexus.components.io.attributes;


public class FileAttributesTest
    extends AbstractResourceAttributesTCK
{

    protected PlexusIoResourceAttributes newAttributes()
    {
        return new FileAttributes();
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
        FileAttributes attrs = (FileAttributes) newAttributes();
        
        assertFalse( attrs.isOwnerReadable() );
        assertFalse( attrs.isOwnerWritable() );
        assertFalse( attrs.isOwnerExecutable() );
        
        assertFalse( attrs.isGroupReadable() );
        assertFalse( attrs.isGroupWritable() );
        assertFalse( attrs.isGroupExecutable() );
        
        assertFalse( attrs.isWorldReadable() );
        assertFalse( attrs.isWorldWritable() );
        assertFalse( attrs.isWorldExecutable() );
        
        attrs.setLsModeline( mode );
        
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
