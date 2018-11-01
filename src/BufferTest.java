import student.TestCase;

/**
 * Tests the Buffer class
 * 
 * @author <Chrsitian Carminucci, cdc97>, <Bob Bao, bob313>
 * @version 10.29.2018
 *
 */
public class BufferTest extends TestCase {
    private Buffer buff;


    /**
     * Tests the getBlockNum and getDataPointer methods.
     */
    public void testGetterMethods() {
        byte[] blk = { '1', '1', '0', '1', '0' };
        buff = new Buffer(blk, 2);
        assertEquals(blk, buff.getDataPointer());
        assertEquals(2, buff.getBlockNum());
    }


    /**
     * Tests the Dirty methods, makeDirty and getDirty.
     */
    public void testDirty() {
        byte[] blk = { '1', '1', '0', '1', '0' };
        buff = new Buffer(blk, 2);
        assertFalse(buff.getDirt());
        buff.markDirty();
        assertTrue(buff.getDirt());

    }
}
