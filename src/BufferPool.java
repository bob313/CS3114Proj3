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
public class BufferPool {
    private RandomAccessFile data;
    private Buffer[] pool;
    private int pSize;
    private static final int blockSize = 4096;
    private static final int recordSize = 4;
    private long fileSize;
    private int cacheHits;
    private int diskReads;
    private int diskWrites;


    /**
     * constructs buffer pool
     * 
     * @param file
     *            is the file to read from
     * @param size
     *            the size of the buffer pool.
     * @throws IOException
     *             in case file doesn't exist
     */
    public BufferPool(String file, int size) throws IOException {
        data = new RandomAccessFile(file, "rw");
        data.seek(0);
        fileSize = data.length();
        pool = new Buffer[size];
        pSize = 0;
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;

    }


    /**
     * Sets the bytes at a given index in a buffer in the pool.
     */
    public void setBytes(byte[] bytes, int index) {
        int numRecs = blockSize / recordSize;
        int block = (index / numRecs) + 1;
        Buffer tempBuff = checkPool(block);
        for (int i = 0; i < bytes.length; i++) {
            tempBuff.getDataPointer()[index - ((block - 1) * numRecs) + i] =
                bytes[i];
        }
        tempBuff.markDirty();
    }


    /**
     * Acquires the Buffer for block 'block' and adds it to the pool.
     * The buffer is made up of the 4 byte sequence where the first 2
     * are the key and the second 2 are the value.
     * 
     * @param block
     *            the block number of the buffer to be acquired
     */
    public byte[] acquireBuffer(int index) {
        int numRecs = blockSize / recordSize;
        int block = (index / numRecs) + 1;
        Buffer newBuff = checkPool(block);
        if (newBuff == null) {
            byte[] temp = new byte[blockSize];
            try {
                data.seek((block - 1) * blockSize);
                data.read(temp);
                diskReads++;
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
        int temp = (index - ((block - 1) * numRecs)) * recordSize;
        byte[] record = new byte[] { newBuff.getDataPointer()[temp], newBuff
            .getDataPointer()[temp + 1], newBuff.getDataPointer()[temp + 2],
            newBuff.getDataPointer()[temp + 3] };
        return record;
    }


    /**
     * Marks the given Buffer as most recently used by
     * moving it to the end of the array.
     * 
     * @param newBuff
     *            the Buffer to be moved
     */
    private void setRecent(Buffer newBuff) {
        int index = pSize;
        for (int i = 0; i < pSize; i++) {
            if (pool[i] != null && pool[i].getBlockNum() == newBuff
                .getBlockNum()) {
                index = i;
                break;
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
     * 
     * @param block
     *            the block number to look for
     * @return the Buffer with that block number, null if none exists
     */
    private Buffer checkPool(int block) {
        for (int i = 0; i < pSize; i++) {
            if (pool[i] != null && pool[i].getBlockNum() == block) {
                cacheHits++;
                return pool[i];
            }
        }
        return null;
    }


    /**
     * Adds a Buffer to the pool.
     * 
     * @param newBuff
     *            the buffer to be added
     */
    private void addToPool(Buffer newBuff) {
        if (pSize == pool.length) {
            dumpLRU();
            pool[pSize - 1] = newBuff;
        }
        else {
            pool[pSize] = newBuff;
            pSize++;
        }

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
                data.seek((oldBuff.getBlockNum() - 1) * blockSize);
                data.write(oldBuff.getDataPointer());
                diskWrites++;
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
                    data.seek((pool[i].getBlockNum() - 1) * blockSize);
                    data.write(pool[i].getDataPointer());
                    diskWrites++;
                }
                catch (IOException e) {
                    System.out.println(
                        "IOException: Failed to update file on clear");
                    e.printStackTrace();
                }
            }
            pool[i] = null; // clears element from array
        }
    }


    /**
     * Closes the data file of BufferPool
     * 
     * @throws IOException
     */
    public void closeFile() throws IOException {
        data.close();
    }


    /**
     * gets the pool array of the buffer pool.
     * 
     * @return the pool array
     */
    public Buffer[] getPool() {
        return pool;
    }


    /**
     * Gets the file size of the input file.
     * 
     * @return the size of the file
     */
    public long getFileSize() {
        return fileSize;
    }


    /**
     * Gets the cache hits; the number of times a block is found in the pool
     * when requested.
     * 
     * @return the cacheHits integer
     */
    public int getCacheHits() {
        return cacheHits;
    }


    /**
     * Gets the number of disk reads
     * 
     * @return number of disk reads
     */
    public int getReads() {
        return diskReads;
    }


    /**
     * Gets the number of disk writes
     * 
     * @return number of disk writes
     */
    public int getWrites() {
        return diskWrites;
    }

}
