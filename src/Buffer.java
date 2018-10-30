import java.nio.ByteBuffer;

/**
 * Buffer class for managing buffer pool
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
@SuppressWarnings("unused")
public class Buffer{
    private byte[] block;
    private boolean dirt;
    private int blockNum;
    
    /**
     * Sets up the Buffer setting dirt to false and setting the
     * key and value. Also sets pPosition to the position of 
     * the Buffer in the file.
     * @param k the key
     * @param v the value
     */
    public Buffer(byte[] blk, int pos) {
        block = blk;
        dirt = false;
        blockNum = pos;
    }
    
    /**
     * Gets the blockNum of the buffer.
     * @return the blockNum
     */
    public int getBlockNum() {
        return blockNum;
    }


    /**
     * Gets the byte array of the buffer.
     * @return the byte array
     */
    public byte[] getDataPointer() {
        
        return block;
    }


    /**
     * Sets the Buffer to be dirty.
     * This signifies that a change has been made.
     */
    public void markDirty() {
        dirt = true;

    }
    
    /**
     * Checks if the Buffer is dirty.
     * @return true if dirty false if not
     */
    public boolean getDirt() {
        return dirt;
    }



}
