import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Contains sorting algorithm which interacts with BufferPool to sort file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
public class Sorting {
    private FakeBufferPool pool;
    private int len = 4;


    /**
     * 
     * @param arg
     *            is the file
     * @param size
     *            is the amount of blocks
     * @throws IOException
     */
    public Sorting(String arg, String size) throws IOException {
        pool = new FakeBufferPool(arg, Integer.parseInt(size));
        quicksort(pool.getBlocks(), 0, (int)pool.getLength() / 4 - 1);
        pool.writeto();
    }


    /**
     * 
     * @param rec
     *            is the record to get the key from
     * @return the short key
     */
    private short getkey(byte[] rec) {
        ByteBuffer bb = ByteBuffer.wrap(rec);
        return bb.getShort();
    }


    /**
     * 
     * @param block
     *            byte array containing the record
     * @param index
     *            the records index
     * @return the record
     */
    private byte[] getRecord(byte[] block, int index) {
        byte[] rec = new byte[2];
        rec[0] = block[index];
        rec[1] = block[index + 1];
        return rec;
    }


// /**
// * Return the value
// */
// private int getValue(byte[] rec) {
// return (int)rec[0];
// }

    /**
     * 
     * @param arr
     *            is the byte array
     * @param start
     *            is the starting index
     * @param end
     *            is the end index
     * @param pivot
     *            is what to compare the value to
     * @return the index of the new partition
     */
    private int partition(byte[] arr, int start, int end, short pivot) {
        while (start <= end) { // Move bounds inward until they meet
            while (getkey(getRecord(arr, start * len)) < pivot) {
                start++;
            }
            while ((end >= start) && (getkey(getRecord(arr, end
                * len)) >= pivot)) {
                end--;
            }
            if (end > start) {
                pool.swapBytes(start * len, end * len); // Swap out-of-place
                                                        // values
            }
        }
        return start; // Return first position in right partition
    }


    /**
     * @param arr
     *            is the byte array
     * @param start
     *            is the starting index
     * @param end
     *            is the ending index
     */
    public void quicksort(byte[] arr, int start, int end) { // Quicksort
        int pivot = (start + end) / 2; // Pick a pivot
        pool.swapBytes(pivot * len, end * len); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(arr, start, end - 1, getkey(getRecord(arr, end
            * len)));
        pool.swapBytes(k * len, end * len); // Put pivot in place
        if ((k - start) > 1) {
            quicksort(arr, start, k - 1); // Sort left partition
        }
        if ((end - k) > 1) {
            quicksort(arr, k + 1, end); // Sort right partition
        }
    }

}
