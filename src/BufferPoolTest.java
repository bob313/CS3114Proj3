import java.io.IOException;
import student.TestCase;

/**
 * Tests the methods of BufferPool
 * @author <Chrsitian Carminucci, cdc97>, <Bob Bao, bob313>
 * @version 10.29.2018
 */
public class BufferPoolTest extends TestCase{
    private BufferPool bufPool;
    
    
    public void testBufferBin() throws IOException {
        FileGenerator generator = new FileGenerator();
        String[] in = new String[3];
        in[0] = "-b";
        in[1] = "BinaryTest";
        in[2] = "12288";
        generator.generateFile(in);
        
        bufPool = new BufferPool(in[1], 2);
        
        Buffer temp = bufPool.acquireBuffer(1);
        assertEquals(1, temp.getBlockNum());
        
        Buffer temp2 = bufPool.acquireBuffer(0);
        assertEquals(0, temp2.getBlockNum());
        assertEquals(temp2.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        bufPool.acquireBuffer(1);
        assertEquals(temp.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        Buffer temp3 = bufPool.acquireBuffer(2);
        assertEquals(2, temp3.getBlockNum());
        assertEquals(temp3.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        
        
        bufPool.getPool()[0].markDirty();
        bufPool.acquireBuffer(0);
        assertEquals(temp.getBlockNum(), bufPool.getPool()[1].getBlockNum());
        assertEquals(temp3.getBlockNum(), bufPool.getPool()[0].getBlockNum());
        
    }
}
