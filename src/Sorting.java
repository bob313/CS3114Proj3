import java.io.FileOutputStream;
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
    private static int len = 4;
    private byte[] rec = new byte[2];
    private static int cut = 10;


    /**
     * 
     * @param arg
     *            is the file
     * @param size
     *            is the amount of blocks
     * @param stat
     *            is the stat file to record info
     * @throws IOException
     */
    public Sorting(String arg, String size, FileOutputStream stat)
        throws IOException {
        pool = new FakeBufferPool(arg, Integer.parseInt(size));
        long begin = System.currentTimeMillis();
        quicksort(pool.getBlocks(), 0, (int)pool.getLength() / 4 - 1);
        long finish = System.currentTimeMillis() - begin;
        stat.write(String.valueOf(finish).getBytes());
        System.out.println(finish);
        pool.writeto();
    }


    /**
     * Found by CS3114 Staff on Piazza
     * 
     * @param rec
     *            is the record to get the key from
     * @return the short key
     */
    private short getkey(byte[] reco) {
        return (ByteBuffer.wrap(reco)).getShort();
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
        } // The following while loop, goes over duplicates
        while (start < end - 2 && getRecord(arr, start * len) == getRecord(arr,
            (start + 1) * len)) {
            start++;
        }
        return start; // Return first position in right partition
    }


    /**
     * Uses median of 3 points method to pick the pivot point
     * What it does is it finds the mid point through conventional (start+end)/2
     * but you then compare the 3 points and find the median value, as that is
     * the better pivot point. You also sort the 3 points
     * 
     * @param arr
     *            byte array
     * @param start
     *            starting index
     * @param end
     *            end index
     * @return the pivot point
     */
    private int median(byte[] arr, int start, int end) {
        int mid = (start + end) / 2;
        if (arr[end] < arr[start]) {
            pool.swapBytes(start * len, end * len);
        }
        if (arr[mid] < arr[start]) {
            pool.swapBytes(mid * len, start * len);
        }
        if (arr[end] < arr[mid]) {
            pool.swapBytes(end * len, mid * len);
        }
        return mid;
    }


    /**
     * 
     * @param arr
     *            the less than size 10 array to sort
     * @param start
     *            where to start
     * @param end
     *            where to end
     */
    private void insertion(byte[] arr, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            for (int j = i; j > start && getkey(getRecord(arr, (j - 1)
                * len)) > getkey(getRecord(arr, j * len)); j--) {
                pool.swapBytes((j - 1) * len, j * len);
            }
        }
    }


    /**
     * Tail recursive quicksort
     * 
     * @param arr
     *            is the byte array
     * @param start
     *            is the starting index
     * @param end
     *            is the ending index
     */
    public void quicksort(byte[] arr, int start, int end) { // Quicksort
        if (end - start < cut) {
            insertion(arr, start, end);
        }
        else {
            while (start < end) {
                int pivot = median(arr, start, end); // Pick a pivot
                pool.swapBytes(pivot * len, end * len); // Stick pivot at end
                // k will be the first position in the right subarray
                int k = partition(arr, start, end - 1, getkey(getRecord(arr, end
                    * len)));
                pool.swapBytes(k * len, end * len); // Put pivot in place
                if ((k - start) < (end - k)) { // Find smaller partition
                    quicksort(arr, start, k - 1); // Sort smaller partition
                    start = k + 1;
                }
                else {
                    quicksort(arr, k + 1, end); // Sort other partition
                    end = k - 1;
                }
            }
        }
    }
}
