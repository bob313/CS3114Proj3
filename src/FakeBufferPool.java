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
public class FakeBufferPool {
    private RandomAccessFile data;
    private byte[] blocks;
    private long len = 0;


    /**
     * constructs buffer pool
     * 
     * @param file
     *            is the file to read from
     * @param size
     *            the amount of buffers
     * @throws IOException
     *             in case file doesn't exist
     */
    public FakeBufferPool(String file, int size) throws IOException {
        data = new RandomAccessFile(file, "rw");
        len = data.length();
        data.seek(0);
        blocks = new byte[(int)(len)];
        data.seek(0);
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = (byte)data.read();
        }
        data.seek(0);
    }


    /**
     * 
     * @return the blocks byte array
     */
    public byte[] getBlocks() {
        return blocks;
    }


    /**
     * 
     * @return the length
     */
    public long getLength() {
        return len;
    }


    /**
     * Writes to the data file
     * 
     * @throws IOException
     */
    public void writeto() throws IOException {
        data.seek(0);
        data.write(blocks, 0, blocks.length);
    }


    /**
     * Swaps in blocks array
     * 
     * @param pre
     *            is the left one
     * @param post
     *            is the right one
     */
    public void swapBytes(int pre, int post) {
        try {
            byte temp = blocks[pre];
            blocks[pre] = blocks[post];
            blocks[post] = temp;
            temp = blocks[pre + 1];
            blocks[pre + 1] = blocks[post + 1];
            blocks[post + 1] = temp;
            temp = blocks[pre + 2];
            blocks[pre + 2] = blocks[post + 2];
            blocks[post + 2] = temp;
            temp = blocks[pre + 3];
            blocks[pre + 3] = blocks[post + 3];
            blocks[post + 3] = temp;
        }
        catch (Exception e) {
            System.out.println("FAILED");
        }
    }

}
