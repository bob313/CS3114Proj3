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
    RandomAccessFile data;
    byte[] blocks = new byte[4096];
    long length = 0;
    long block = 0;
    long recs = 0;
    long count = 0;
    byte[] record;
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
        data = new RandomAccessFile(file, "rw");
        length = data.length();
        block = length / 4096;
        recs = block * 4;
        // while (count < recs) {
        count++;
        data.readFully(blocks);
        data.seek(0);
        record = Arrays.copyOfRange(blocks, 0, 4);
        key = Arrays.copyOfRange(record, 0, 2);
        value = Arrays.copyOfRange(record, 2, 4);
        // }
    }


    /**
     * @param block
     */
    public Buffer acquireBuffer(int block) {
        return null;
    }


    /**
     * 
     * @param bytePos
     * @return
     */
    public byte[] getRecord(int bytePos) {
        return Arrays.copyOfRange(blocks, bytePos, bytePos + 1);
    }


    /**
     * 
     * @param bytePos
     * @return
     */
    public byte[] getBytes(int bytePos) {
        return Arrays.copyOfRange(blocks, bytePos, bytePos + 4);
    }


    /**
     * 
     * @param pre
     * @param post
     */
    public void swapBytes(int pre, int post) {
        try {
            data.write(blocks, pre, 1);
            data.seek(0);
            data.write(blocks, post, 1);
            data.seek(0);
            byte temp = blocks[pre];
            blocks[pre] = blocks[post];
            blocks[post] = temp;
        }
        catch (Exception e) {
            System.out.println("FAILED");
        }
// for (int i = 0; i < 4; i++) {
// byte temp = blocks[post];
// blocks[post + i] = blocks[pre + i];
// blocks[pre + i] = temp;
// }
    }
}
