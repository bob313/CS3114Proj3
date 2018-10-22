import java.nio.ByteBuffer;

/**
 * Buffer class for managing buffer pool
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
@SuppressWarnings("unused")
public class Buffer implements BufferADT {
    private short key;
    private short value;
    
    public Buffer() {
        
    }
    
    public byte[] readBlock() {
        // TODO Auto-generated method stub
        return null;
    }


    public byte[] getDataPointer() {
        // TODO Auto-generated method stub
        return null;
    }


    public void markDirty() {
        // TODO Auto-generated method stub

    }


    public void releaseBuffer() {
        // TODO Auto-generated method stub

    }


}
