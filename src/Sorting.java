/**
 * Contains sorting algorithm which interacts with BufferPool to sort file
 * 
 * @author bob313 cdc97
 * @version Oct 21 2018
 *
 */
public class Sorting {
    int[] array;


    public Sorting() {

    }


    /**
     * currently just insertion sort
     */
    public void sort() {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            int key = array[i];
            int j = i - 1;

            /*
             * Move elements of array[0..i-1], that are
             * greater than key, to one position ahead
             * of their current position
             */
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }


    /**
     * swaps 2 elem in array
     * 
     * @param array
     *            is array containin values
     * @param i
     *            is value to swap with
     * @param j
     *            is value to swap
     */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    /**
     * 
     * @param arr
     * @param left
     * @param right
     * @param pivot
     * @return
     */
    public int partition(int[] arr, int left, int right, int pivot) {
        while (left <= right) { // Move bounds inward until they meet
            while (arr[left] < arr[pivot]) {
                left++;
            }
            while ((right >= left) && (arr[right] >= arr[pivot])) {
                right--;
            }
            if (right > left) {
                swap(arr, left, right); // Swap out-of-place values
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
    public void quicksort(int[] arr, int start, int end) { // Quicksort
        int pivotindex = (start + end) / 2; // Pick a pivot
        swap(arr, pivotindex, end); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(arr, start, end - 1, arr[end]);
        swap(arr, k, end); // Put pivot in place
        if ((k - start) > 1) {
            quicksort(arr, start, k - 1); // Sort left partition
        }
        if ((end - k) > 1) {
            quicksort(arr, k + 1, end); // Sort right partition
        }
    }
}
