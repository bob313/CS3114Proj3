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
    private int blockNum;
    private byte[] key = new byte[2];
    private int startIndex = -1;
    private int midIndex = -1;
    private int endIndex = -1;
    private byte[] startrec = new byte[4];
    private byte[] midrec = new byte[4];
    private byte[] endrec = new byte[4];
    private byte[] temprec = new byte[4];
    private static int cut = 10;
    // private byte[] block = new byte[4096];
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

        blockNum = Integer.parseInt(size);
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
    private short getkey(byte[] reco) {
        key[0] = reco[0];
        key[1] = reco[1];
        return (ByteBuffer.wrap(key)).getShort();
    }


    private byte[] getRecord(int index) {
        return pool.acquireBuffer(index);
    }
//
//
// /**
// *
// * @param block
// * byte array containing the record
// * @param index
// * the records index
// * @return the record
// */
// private byte[] getRecord(byte[] arr, int index, int recIndex) {
// int blocknum = index / bSize;
// if (recIndex == 1) {
// rec[0] = arr[index - blocknum * bSize];
// rec[1] = arr[index - blocknum * bSize + 1];
// rec[2] = arr[index - blocknum * bSize + 2];
// rec[3] = arr[index - blocknum * bSize + 3];
// return rec;
// }
// else {
// rec2[0] = arr[index - blocknum * bSize];
// rec2[1] = arr[index - blocknum * bSize + 1];
// rec2[2] = arr[index - blocknum * bSize + 2];
// rec2[3] = arr[index - blocknum * bSize + 3];
// return rec2;
// }
// }
    
    private void swapArray(byte[] orig, byte[] copy) {
        byte temp;
        for (int i=0; i<4; i++) {
            temp = orig[i];
            orig[i] = copy[i];
            copy[i] = temp;
        }
    }

    /**
     * Swaps the values
     *
     * @param left
     * @param right
     */
    private void swap(int left, int right) {
        System.out.println("L: " + left + " R:" + right);
        if (left == startIndex && right == endIndex) {
 //           System.out.println("swap1");
            if (getkey(startrec) != getkey(endrec)) {
                pool.setBytes(startrec, right);
                pool.setBytes(endrec, left);
                swapArray(startrec, endrec);
            }
 //           System.out.println("fin 1");
        }
        else if (left == midIndex && right == startIndex) {
//            System.out.println("swap2");
            if (getkey(midrec) != getkey(startrec)) {
                pool.setBytes(midrec, right);
                pool.setBytes(startrec, left);
                swapArray(startrec, midrec);
            }
//            System.out.println("fin 2");
        }
        else if (left == endIndex && right == midIndex) {
//            System.out.println("swap3");
            if (getkey(endrec) != getkey(midrec)) {
                pool.setBytes(endrec, right);
                pool.setBytes(midrec, left);
                swapArray(midrec, endrec);
            }
 //           System.out.println("fin 3");
        }
        else if (left == midIndex && right == endIndex) {
 //           System.out.println("swap4");
            if (getkey(midrec) != getkey(endrec)) {
                pool.setBytes(midrec, right);
                pool.setBytes(endrec, left);
                swapArray(midrec, endrec);
            }
  //          System.out.println("fin 4");
        }
        else if (right == midIndex) {
  //          System.out.println("swap5");
            temprec = getRecord(left);
  //          System.out.println(midrec[1]+"  AND  "+temprec[1]);
            if (getkey(midrec) != getkey(temprec)) {
                pool.setBytes(midrec, left);
                pool.setBytes(temprec, right);
                swapArray(midrec, temprec);
            }
  //          System.out.println("midIn: "+midIndex+"  midrec:  "+midrec[1]);
        }
        else {
  //          System.out.println("swap6");
            midIndex = right;
            temprec = getRecord(left);
            midrec = getRecord(right);
            if (getkey(midrec) != getkey(temprec)) {
                pool.setBytes(midrec, left);
                pool.setBytes(temprec, right);
                swapArray(temprec, midrec);
            }
  //          System.out.println("fin 6");
        }
    }


    /**
     * 
     * @param start
     *            is the starting index
     * @param end
     *            is the end index
     * @param pivot
     *            is what to compare the value to
     * @return the index of the new partition
     */
    private int partition(int start, int end, short pivot) {
        while (start <= end) { // Move bounds inward until they meet
// System.out.println("SKEY: " + (int)getKey(start * len) + " EKY: "
// + getKey(end * len) + " PI " + pivot);
            while (getkey(getRecord(start)) < pivot) {
                start++;
            }
            while ((end >= start) && (getkey(getRecord(end)) >= pivot)) {
                end--;
            }
            if (end > start) {
//                System.out.println("loop here? S and E" + start + "  "+end);
//                System.out.println("Indexs "+midIndex + "  midrec: "+midrec[1]);
//                byte[] t1 = getRecord(start);
//                byte[] t2 = getRecord(end);
 //               System.out.println("StartKey: "+t1[1] + "  EndKey: " + t2[1]);
 //               System.out.println(temprec[1]);
                while (getkey(getRecord(start)) == getkey(getRecord(end)) && getkey(getRecord(end)) == pivot) {
                    start++;
                    end--;
                }
                swap(start, end); // Swap out-of-place
                                  // values
            }
        }
        return start; // Return first position in right partition
    }


    /**
     * Uses median of 3 points method to pick the pivot point
     * What it does is it finds the mid point through conventional (start+end)/2
     * but you then compare the 3 points and find the median value, as that is
     * the better pivot point. You also sort the 3 points
     * 
     * @param start
     *            starting index
     * @param end
     *            end index
     * @return the pivot point
     */
    private int median(int start, int end) {
        int mid = (start + end) / 2;
        if (startIndex != start) {
            startIndex = start;
            startrec = getRecord(start);
        }
        if (midIndex != mid) {
            midIndex = mid;
            midrec = getRecord(mid);
        }
        if (endIndex != end) {
            endIndex = end;
            endrec = getRecord(end);
        }
        if (getkey(endrec) < getkey(startrec)) {
            swap(start, end);
        }
        if (getkey(midrec) < getkey(startrec)) {
            swap(mid, start);
        }
        if (getkey(endrec) < getkey(midrec)) {
            swap(end, mid);
        }
        return mid;
    }


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
        if (end - start < cut) {
            insertion(start, end);
        }
        else {
            while (start < end) {
                int pivot = median(start, end); // Pick a pivot
                swap(pivot, end); // Stick pivot at end
                // k will be the first position in the right subarray
                temprec = getRecord(end);
                int k = partition(start, end - 1, getkey(getRecord(end)));
// System.out.println("S: " + start + " P: " + pivot + " E: " + end
// + " K: " + k);
                swap(k, end); // Put pivot in place
                if ((k - start) < (end - k)) { // Find smaller partition
                    quicksort(start, k - 1); // Sort smaller partition
                    start = k + 1;
                }
                else {
                    quicksort(k + 1, end); // Sort other partition
                    end = k - 1;
                }
            }
        }
    }
}
