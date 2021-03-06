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
        
        byte[] temp = bufPool.acquireBuffer(2);
        assertEquals(1, bufPool.getPool()[0].getBlockNum());
        assertTrue(temp[2] != "0".getBytes()[0]); 
        
        byte[] temp2 = bufPool.acquireBuffer(1200);
        assertEquals(2, bufPool.getPool()[1].getBlockNum());
        assertTrue(temp2[2] != "0".getBytes()[0]);
        
        assertEquals(2, bufPool.getReads());
        
        bufPool.acquireBuffer(2);
        assertEquals(1, bufPool.getPool()[1].getBlockNum());
        
        assertEquals(1, bufPool.getCacheHits());
        
        byte[] temp3 = bufPool.acquireBuffer(2500);
        assertEquals(3, bufPool.getPool()[1].getBlockNum());
        assertTrue(temp3[3] != "0".getBytes()[0]);
        
        assertEquals(3, bufPool.getReads());
        assertEquals(0, bufPool.getWrites());
        
        byte[] test = " X  ".getBytes();
        bufPool.setBytes(test, 0);
        assertEquals(test[2], bufPool.getPool()[0].getDataPointer()[2]);
        bufPool.acquireBuffer(1200);
        assertEquals(1, bufPool.getWrites());
        assertEquals(2, bufPool.getPool()[1].getBlockNum());
        assertEquals(3, bufPool.getPool()[0].getBlockNum());
        
        
        bufPool.setBytes(test, 1024);
        assertEquals(test[2], bufPool.getPool()[1].getDataPointer()[2]);
        
        //CLEAR TEST: Testing of the clearPool method begins here
        bufPool.clearPool();
        assertNull(bufPool.getPool()[0]);
        assertNull(bufPool.getPool()[1]);
        
        bufPool.closeFile();
    }
    
    
    
    
    
}
