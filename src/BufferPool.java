import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Buffer pool to store info from binary file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
@SuppressWarnings("unused")
public class BufferPool implements BufferPoolADT {

    public BufferPool() {
        try (RandomAccessFile file = new RandomAccessFile("file.txt", "rw")) {
            file.close();
          } catch (IOException e) {
            // Exception handling
          }
    }
    
    /**
     * @param block
     */
    public Buffer acquireBuffer(int block) {
        return null;
    }

}
