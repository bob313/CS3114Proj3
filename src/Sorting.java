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
    private BufferPool pool;
    private static int cut = 10;
    private long startTime;
    private long endTime;


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

        int blockNum = Integer.parseInt(size);
        pool = new BufferPool(arg, blockNum);
        // currBlock = pool.acquireBuffer(blockNum).getDataPointer();
        // long begin = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        quicksort(0, (int)pool.getFileSize() / 4 - 1);
        // long finish = System.currentTimeMillis() - begin;
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        // stat.write(String.valueOf(finish).getBytes());
        pool.clearPool();
        generateOutput(stat, arg);
    }


    /**
     * generates stat file
     * 
     * @param stat
     *            is the stat file
     * @param arg
     *            arg to receive
     * @throws IOException
     *             throw an exception
     */
    private void generateOutput(FileOutputStream stat, String arg)
        throws IOException {

        stat.write("Name: ".getBytes());
        stat.write(arg.getBytes());
        stat.write("     Hits: ".getBytes());
        stat.write(String.valueOf(pool.getCacheHits()).getBytes());
        stat.write("     Reads: ".getBytes());
        stat.write(String.valueOf(pool.getReads()).getBytes());
        stat.write("     Writes: ".getBytes());
        stat.write(String.valueOf(pool.getWrites()).getBytes());
        stat.write("     Exec Time: ".getBytes());
        stat.write(String.valueOf(endTime - startTime).getBytes());
        stat.write("\n".getBytes());
    }


    /**
     * Found by CS3114 Staff on Piazza
     *
     * @param rec
     *            is the record to get the key from
     * @return the short key
     */
    private short getkey(byte[] rec) {
        return (ByteBuffer.wrap(rec)).getShort();
    }


    /**
     * returns the record
     * 
     * @param index
     *            index of the record
     * @return the record at index
     */
    private byte[] getRecord(int index) {
        return pool.acquireBuffer(index);
    }


    /**
     * Swaps the values
     *
     * @param left
     *            the smaller record
     * @param right
     *            the bigger record
     */
    private void swap(int left, int right) {
        byte[] temprec = getRecord(left);
        byte[] midrec = getRecord(right);
        if (getkey(midrec) != getkey(temprec)) {
            pool.setBytes(midrec, left);
            pool.setBytes(temprec, right);
        }
    }


//    /**
//     * Uses median of 3 points method to pick the pivot point
//     * What it does is it finds the mid point through conventional (start+end)/2
//     * but you then compare the 3 points and find the median value, as that is
//     * the better pivot point. You also sort the 3 points
//     * 
//     * @param start
//     *            starting index
//     * @param end
//     *            end index
//     * @return the pivot point
//     */
//    private short midpiv(int start, int end) {
//        short s = getkey(getRecord(start));
//        short e = getkey(getRecord(end));
//        short mid = getkey(getRecord((start + end) / 2));
//        if (s < e && s < mid) {
//            return s;
//        }
//        else if (mid < s && mid < e) {
//            return mid;
//        }
//        else if (e < mid && e < s) {
//            return e;
//        }
//        else if (e == mid) {
//            return s;
//        }
//        else if (mid == s) {
//            return e;
//        }
//        return mid;
//    }


    /**
     * 
     * @param start
     *            where to start
     * @param end
     *            where to end
     */
    private void insertion(int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            for (int j = i; j > start && getkey(getRecord((j - 1))) > getkey(
                getRecord(j)); j--) {
                swap((j - 1), j);
            }
        }
    }


    /**
     * Tail recursive quicksort
     * 
     * @param start
     *            is the starting index
     * @param end
     *            is the ending index
     */
    public void quicksort(int start, int end) { // Quicksort
        int left = start;
        int right = end;
        short pivot = getkey(getRecord(start));
        // midpiv(start, end);// median of 3 method
        int i = start;
        while (i <= right) { // 3 way partition, sort left and right sides
            short value = getkey(getRecord(i));
            if (value < pivot) {
                swap(left, i);
                left++;
                i++;
            }
            else if (value > pivot) {
                swap(i, right);
                right--;
            }
            else {
                i++;
            }
        }
        if ((left - start < cut)) {
            insertion(start, left - 1);
        }
        else {
            quicksort(start, left - 1);
        }
        if (end - right < cut) {
            insertion(right + 1, end);
        }
        else {
            quicksort(right + 1, end);
        }

    }
}
