import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
    byte[] record = new byte[4];
    long length = 0;
    long recs = 0;
    long count = 0;
    byte[] key;
    byte[] value;


    /**
     * constructs buffer pool
     * 
     * @param file
     *            is the file to read from
     * @throws IOException
     *             in case file doesn't exist
     */
    public BufferPool(String file) throws IOException {
        RandomAccessFile data = new RandomAccessFile(file, "rw");
        length = data.length();
        // div by 4, becuz multiple of 4096 bytes, but blocks hold 1024 records
        recs = length / 4;
// while (cnt < recs) {
// cnt++;
// data.readFully(record);
// key = Arrays.copyOfRange(record, 0, 2);
// value = Arrays.copyOfRange(record, 2, 4);
// }
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
