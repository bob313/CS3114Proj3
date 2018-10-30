import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Buffer pool to store info from binary file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
@SuppressWarnings("unused")
public class BufferPool{
    private RandomAccessFile data;
    private Buffer[] pool;
    private int pSize;


    /**
     * constructs buffer pool
     * 
     * @param file
     *            is the file to read from
     * @param size the size of the buffer pool.
     * @throws IOException
     *             in case file doesn't exist
     */
    public BufferPool(String file, int size) throws IOException {
        data = new RandomAccessFile(file, "rw");
        data.seek(0);
        pool = new Buffer[size];
        pSize = 0;
        
    }

    /**
     * Acquires the Buffer for block 'block' and adds it to the pool. 
     * The buffer is made up of the 4 byte sequence where the first 2 
     * are the key and the second 2 are the value.
     * @param block the block number of the buffer to be acquired
     */
    public Buffer acquireBuffer(int block) {
        Buffer newBuff = checkPool(block);
        if (newBuff == null) {
            byte[] temp = new byte[4096];
            try {
                data.read(temp, block * 4096, 4096);
                data.seek(0);
            }
            catch (IOException e) {
                System.out.println("IOException: Failed to acquire Buffer");
                e.printStackTrace();
            }
            newBuff = new Buffer(temp, block);
            addToPool(newBuff);
        }
        else {
            setRecent(newBuff);
        }
        return newBuff;
    }


    /**
     * Marks the given Buffer as most recently used by
     * moving it to the end of the array.
     * @param newBuff the Buffer to be moved
     */
    private void setRecent(Buffer newBuff) {
        int index = pSize;
        for (int i = 0; i < pSize; i++) {
            if (pool[i].getBlockNum() == newBuff.getBlockNum()) {
                index = i;
            }
        }
        for (int i = index + 1; i < pSize; i++) {
            pool[i - 1] = pool[i];
        }
        pool[pSize - 1] = newBuff;
        
    }

    /**
     * Checks the pool to see if a Buffer of block 'block'
     * exists in it.
     * @param block the block number to look for
     * @return the Buffer with that block number, null if none exists
     */
    private Buffer checkPool(int block) {
        for (int i = 0; i < pSize; i++) {
            if (pool[i].getBlockNum() == block) {
                return pool[i];
            }
        }
        return null;
    }

    /**
     * Adds a Buffer to the pool.
     * @param newBuff the buffer to be added
     */
    private void addToPool(Buffer newBuff) {
       if (pool[pSize] == pool[pool.length - 1]) { 
           dumpLRU();
           pool[pSize] = newBuff;
       }
       pool[pSize] = newBuff;
       pSize++;
        
    }

    /**
     * Dumps the least recently used element from the pool.
     * If the dumped Buffer was dirty the data file is updated.
     */
    private void dumpLRU() {
        Buffer oldBuff = pool[0];
        for (int i = 1; i < pool.length; i++) {
            pool[i - 1] = pool[i];
        }
        pool[pool.length - 1] = null;
        if (oldBuff.getDirt()) {
            try {
                data.write(oldBuff.getDataPointer(), oldBuff.getBlockNum() * 4096, 4096);
            }
            catch (IOException e) {
                System.out.println("IOException: Failed to update file");
                e.printStackTrace();
            }
        }
    }

    /**
     * Clears the BufferPool, writing dirty Buffers back to the file in memory.
     */
    public void clearPool() {
        for (int i = 0; i < pSize; i++) {
            if (pool[i].getDirt()) {
                try {
                    data.write(pool[i].getDataPointer(), pool[i].getBlockNum() * 4096, 4096);
                }
                catch (IOException e) {
                    System.out.println("IOException: Failed to update file on clear");
                    e.printStackTrace();
                }
            }
            pool[i] = null; //clears element from array
        }
    }
    
    /**
     * gets the pool array of the buffer pool.
     * @return the pool array
     */
    public Buffer[] getPool() {
        return pool;
    }
}
