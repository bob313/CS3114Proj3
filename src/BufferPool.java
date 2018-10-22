import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Buffer pool to store info from binary file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
@SuppressWarnings("unused")
public class BufferPool implements BufferPoolADT {
    Queue<Integer> q = new LinkedList<>();


    public BufferPool(String file) throws IOException {
        RandomAccessFile data = new RandomAccessFile(file, "rw");
        long length = data.length();
        long recs = length / 4096;
        long cnt = 0;
    }


    /**
     * @param block
     */
    public Buffer acquireBuffer(int block) {
        return null;
    }


    /**
     * Return the key
     */
    private short getkey(byte[] rec) {
        ByteBuffer bb = ByteBuffer.wrap(rec);
        return bb.getShort();
    }

}
