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
    private byte[] currBlock = new byte[4096];
    private static int len = 4;
    private int bSize = 4096;
    private int blockNum;
    private byte[] key = new byte[2];
    private byte[] rec = new byte[4];
    private byte[] rec2 = new byte[4];
    private byte temp;
    private static int cut = 10;
    private Buffer buff;
    // private byte[] block = new byte[4096];


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
        currBlock = pool.acquireBuffer(blockNum).getDataPointer();
        long begin = System.currentTimeMillis();
        quicksort(0, (int)pool.getFileSize() / 4 - 1);
        long finish = System.currentTimeMillis() - begin;
        System.out.println(finish);
        stat.write(String.valueOf(finish).getBytes());
        pool.clearPool();
    }


//
// /**
// * Found by CS3114 Staff on Piazza
// *
// * @param rec
// * is the record to get the key from
// * @return the short key
// */
// private short getkey(byte[] reco) {
// return (ByteBuffer.wrap(reco)).getShort();
// }
//
//
    /**
     *
     * @param block
     *            byte array containing the record
     * @param index
     *            the records index
     * @return the record
     */
    private byte[] getRecord(byte[] arr, int index, int recIndex) {
        int blocknum = index / bSize;
        if (recIndex == 1) {
            rec[0] = arr[index - blocknum * bSize];
            rec[1] = arr[index - blocknum * bSize + 1];
            rec[2] = arr[index - blocknum * bSize + 2];
            rec[3] = arr[index - blocknum * bSize + 3];
            return rec;
        }
        else {
            rec2[0] = arr[index - blocknum * bSize];
            rec2[1] = arr[index - blocknum * bSize + 1];
            rec2[2] = arr[index - blocknum * bSize + 2];
            rec2[3] = arr[index - blocknum * bSize + 3];
            return rec2;
        }
    }


    /**
     * Found by CS3114 Staff on Piazza
     *
     * @param index
     *            is the index of the record to get the key from
     * @return the short key
     */
    private short getKey(int index) {
        int blocknum = index / bSize;
        if (blocknum == blockNum) {
            key[0] = currBlock[index - blocknum * bSize];
            key[1] = currBlock[index - blocknum * bSize + 1];
            return (ByteBuffer.wrap(key)).getShort();
        }
        blockNum = blocknum;
        buff = pool.acquireBuffer(blocknum + 1);
        currBlock = buff.getDataPointer();
        key[0] = currBlock[index - blocknum * bSize];
        key[1] = currBlock[index - blocknum * bSize + 1];
        return (ByteBuffer.wrap(key)).getShort();
    }


// private void swap(int left, int right) {
// int blockA = left / bSize;
// int blockB = right / bSize;
// byte temp;
// buff = pool.acquireBuffer(blockB + 1);
// System.out.println("L: " + currBlock[left - blockA * bSize + 1] + " R: "
// + currBlock[right - blockA * bSize + 1]);
// for (int i = 0; i < 4; i++) {
// temp = currBlock[left - blockA * bSize + i];
// currBlock[left - blockA * bSize + i] = currBlock[right - blockB
// * bSize + i];
// currBlock[right - blockB * bSize + i] = temp;
// }
// buff.setByteArray(currBlock);
// buff.markDirty();
// }

    /**
     * Swaps the values
     *
     * @param left
     * @param right
     */
    private void swap(int left, int right) {
        int blockA = left / bSize;
        int blockB = right / bSize;
        if (blockA == blockB && blockA == blockNum) {
            System.out.println("PRE 1");
            if (getRecord(currBlock, left, 1) != getRecord(currBlock, right,
                2)) {
                System.out.println("Faile 1");
                for (int i = 0; i < 4; i++) {
                    temp = currBlock[left - blockA * bSize + i];
                    currBlock[left - blockA * bSize + i] = currBlock[right
                        - blockA * bSize + i];
                    currBlock[right - blockA * bSize + i] = temp;
// temp = buff.getDataPointer()[left - blockA * bSize + i];
// buff.getDataPointer()[left - blockA * bSize + i] = buff
// .getDataPointer()[right - blockA * bSize + i];
// buff.getDataPointer()[right - blockA * bSize + i] = temp;
                }
                buff.setByteArray(currBlock);
                buff.markDirty();
            }
        }
        else if (blockA == blockB) {
            buff = pool.acquireBuffer(blockB + 1);
            blockNum = blockA;
            currBlock = buff.getDataPointer();
            System.out.println("PRE 2");
            if (getRecord(currBlock, left, 1) != getRecord(currBlock, right,
                2)) {
                System.out.println("Faile 2");
                for (int i = 0; i < 4; i++) {
                    temp = currBlock[left - blockA * bSize + i];
                    currBlock[left - blockA * bSize + i] = currBlock[right
                        - blockA * bSize + i];
                    currBlock[right - blockA * bSize + i] = temp;
                }
                buff.setByteArray(currBlock);
                buff.markDirty();
            }
        }
        else if (blockA == blockNum || blockB == blockNum) {
            Buffer buffB;
            if (blockA == blockNum) {
                buffB = pool.acquireBuffer(blockB + 1);
                System.out.println("PRE 3 A: " + blockA + " B: " + blockB
                    + " Nu: " + blockNum);
                // rec2 = getRecord(buffB.getDataPointer(), right);
                // rec3 = getRecord(currBlock, left);
                if (getRecord(currBlock, left, 1) != getRecord(buffB
                    .getDataPointer(), right, 2)) {
                    System.out.println("Faile 3");
                    for (int i = 0; i < 4; i++) {
                        temp = currBlock[left - blockA * bSize + i];
                        currBlock[left - blockA * bSize + i] = buffB
                            .getDataPointer()[right - blockB * bSize + i];
                        buffB.getDataPointer()[right - blockB * bSize + i] =
                            temp;
                    }
                    buff.setByteArray(currBlock);
                    buff.markDirty();
                    buffB.markDirty();
                }
            }
            else {
                buffB = pool.acquireBuffer(blockA + 1);
                System.out.println("PRE 4");
                // rec2 = getRecord(buffB.getDataPointer(), left);
                if (getRecord(currBlock, right, 1) != getRecord(buffB
                    .getDataPointer(), left, 2)) {
                    System.out.println("Faile 4");
                    for (int i = 0; i < 4; i++) {
                        temp = currBlock[right - blockB * bSize + i];
                        currBlock[right - blockB * bSize + i] = buffB
                            .getDataPointer()[left - blockA * bSize + i];
                        buffB.getDataPointer()[left - blockA * bSize + i] =
                            temp;
                    }
                    buff.setByteArray(currBlock);
                    buff.markDirty();
                    buffB.markDirty();
                }
            }
        }
        else {
            buff = pool.acquireBuffer(blockA + 1);
            currBlock = buff.getDataPointer();
            blockNum = blockA;
            Buffer buffB = pool.acquireBuffer(blockB + 1);
            // System.out.println("PRE 5");
            if (getRecord(currBlock, left, 1) != getRecord(buffB
                .getDataPointer(), right, 2)) {
                // System.out.println("Faile 5");
                for (int i = 0; i < 4; i++) {
                    temp = currBlock[left - blockA * bSize + i];
                    currBlock[left - blockA * bSize + i] = buffB
                        .getDataPointer()[right - blockB * bSize + i];
                    buffB.getDataPointer()[right - blockB * bSize + i] = temp;
                }
                buff.setByteArray(currBlock);
                buff.markDirty();
                buffB.markDirty();
            }
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
//            System.out.println("SKEY: " + (int)getKey(start * len) + " EKY: "
//                + getKey(end * len) + " PI " + pivot);
            while (getKey(start * len) < pivot) {
                start++;
            }
            while ((end >= start) && (getKey(end * len) >= pivot)) {
                end--;
            }
            if (end > start) {
                swap(start * len, end * len); // Swap out-of-place
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
//        System.out.println("Med: " + mid + " S: " + start + " E: " + end);
        // System.out.println("M: " + currBlock[mid*len +1] + " S: " +
        // currBlock[start*len +1] + " E: " + currBlock[end*len +1]);
        if (getKey(end * len) < getKey(start * len)) {
            swap(start * len, end * len);
        }
        if (getKey(mid * len) < getKey(start * len)) {
            swap(mid * len, start * len);
        }
        if (getKey(end * len) < getKey(mid * len)) {
            swap(end * len, mid * len);
        }
        // System.out.println("M: " + currBlock[mid*len +1] + " S: " +
        // currBlock[start*len +1] + " E: " + currBlock[end*len +1]);
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
            for (int j = i; j > start && getKey((j - 1) * len) > getKey(j
                * len); j--) {
                swap((j - 1) * len, j * len);
            }
        }
    }


    public void setarr(byte[] arr) {
        currBlock = arr;
    }


    private void testswap(int left, int right) {
        for (int i = 0; i < 4; i++) {
            temp = currBlock[left + i];
            currBlock[left + i] = currBlock[right + i];
            currBlock[right + i] = temp;
        }
    }


    public void testInsertion(int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            for (int j = i; j > start && currBlock[(j - 1) * len
                + 1] > currBlock[j * len + 1]; j--) {
                testswap((j - 1) * len, j * len);
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
                swap(pivot * len, end * len); // Stick pivot at end
                // k will be the first position in the right subarray
                int k = partition(start, end - 1, getKey(end * len));
// System.out.println("S: " + start + " P: " + pivot + " E: " + end
// + " K: " + k);
                swap(k * len, end * len); // Put pivot in place
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
