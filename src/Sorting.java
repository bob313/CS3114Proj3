import java.nio.ByteBuffer;

/**
 * Contains sorting algorithm which interacts with BufferPool to sort file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
public class Sorting {
    BufferPool pool;

/**
 * 
 * @param pool
 */
    public Sorting(BufferPool pool) {
        this.pool = pool;
    }

    /**
     * Return the key
     */
    private short getkey(byte[] rec) {
        ByteBuffer bb = ByteBuffer.wrap(rec);
        return bb.getShort();
    }
    

    /**
     * currently just insertion sort
     */
    public void sort() {
        int n = pool.blocks.length / 4;
        for (int i = 0; i < n; i = i+4) {
            short key = this.getkey(pool.getBytes(i));
            int j = i - 1;
            while (j >= 0 && this.getkey(pool.getBytes(j)) > key) {
                pool.swapBytes(j, j+1);
                j = j - 1;
            }
            pool.swapBytes(j, j+1);
        }
    }


    /**
     * 
     * @param arr
     * @param left
     * @param right
     * @param pivot
     * @return
     */
    public int partition(byte[] arr, int left, int right, int pivot) {
        while (left <= right) { // Move bounds inward until they meet
            while (arr[left] < arr[pivot]) {
                left++;
            }
            while ((right >= left) && (arr[right] >= arr[pivot])) {
                right--;
            }
            if (right > left) {
                pool.swapBytes(left, right); // Swap out-of-place values
            }
        }
        return left; // Return first position in right partition
    }


    /**
     * 
     * @param arr
     * @param start
     * @param end
     */
    public void quicksort(byte[] arr, int start, int end) { // Quicksort
        int pivotindex = (start + end) / 2; // Pick a pivot
        pool.swapBytes(pivotindex, end); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(arr, start, end - 1, arr[end]);
        pool.swapBytes(k, end); // Put pivot in place
        if ((k - start) > 1) {
            quicksort(arr, start, k - 1); // Sort left partition
        }
        if ((end - k) > 1) {
            quicksort(arr, k + 1, end); // Sort right partition
        }
    }

    

}
