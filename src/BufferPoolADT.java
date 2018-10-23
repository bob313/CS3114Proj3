/**
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
public interface BufferPoolADT {

    /**
     * Relate a block to a buffer, returning a pointer to a buffer object
     * 
     * @param block
     * @return a pointer to a buffer object
     */
    Buffer acquireBuffer(int block);
    
    /**
     * 
     * @param bytePos
     * @return
     */
    public byte[] getBytes(int bytePos);
    
    
}
