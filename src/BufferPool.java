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

    public BufferPool() throws IOException {
        RandomAccessFile file = new RandomAccessFile("file.txt", "rw");
        file.close();
    }


    /**
     * @param block
     */
    public Buffer acquireBuffer(int block) {
        return null;
    }

}
