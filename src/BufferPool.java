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
public class BufferPool implements BufferPoolADT {
    RandomAccessFile data;
    byte[] blocks;
    long len = 0;
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
    public BufferPool(String file, int size) throws IOException {
        data = new RandomAccessFile(file, "rw");
        data.seek(0);
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
            data.seek((pre * 4) + 1);
            data.write(blocks[post]);
            data.seek((post * 4) + 1);
            data.write(blocks[pre]);
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
