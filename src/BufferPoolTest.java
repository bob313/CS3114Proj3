import java.io.IOException;
import student.TestCase;

/**
 * Tests the methods of BufferPool
 * @author <Chrsitian Carminucci, cdc97>, <Bob Bao, bob313>
 * @version 10.29.2018
 */
public class BufferPoolTest extends TestCase{
    private BufferPool bufPool;
    
    
    public void testBufferASCII() throws IOException {
        VirtualSort.generateFile("BinaryTest.txt", "3", 'a');
        
        bufPool = new BufferPool("BinaryTest.txt", 2);
        
        Buffer temp = bufPool.acquireBuffer(2);
        assertEquals(2, temp.getBlockNum());
        
        Buffer temp2 = bufPool.acquireBuffer(1);
        assertEquals(1, temp2.getBlockNum());
        assertEquals(temp2.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        assertEquals(2, bufPool.getReads());
        
        bufPool.acquireBuffer(2);
        assertEquals(temp.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        assertEquals(1, bufPool.getCacheHits());
        
        Buffer temp3 = bufPool.acquireBuffer(3);
        assertEquals(3, temp3.getBlockNum());
        assertEquals(temp3.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        assertEquals(3, bufPool.getReads());
        assertEquals(0, bufPool.getWrites());
        
        
        bufPool.getPool()[0].markDirty();
        bufPool.acquireBuffer(1);
        assertEquals(1, bufPool.getWrites());
        assertEquals(temp2.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        assertEquals(temp3.getBlockNum(), bufPool.getPool()[0].getBlockNum());
        
        //CLEAR TEST: Testing of the clearPool method begins here
        bufPool.clearPool();
        assertNull(bufPool.getPool()[0]);
        assertNull(bufPool.getPool()[1]);
        
        
    }
    
    
    
    
    
}
