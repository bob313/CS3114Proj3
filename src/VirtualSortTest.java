import student.TestCase;

/**
 * @author CS3114 staff
 * @version 1
 */

public class VirtualSortTest extends TestCase {
    private CheckFile fileChecker;


    /**
     * This method sets up the tests that follow.
     */
    public void setUp() {
        fileChecker = new CheckFile();
    }


    // ----------------------------------------------------------
    /**
     * Test intialization
     */
    public void testInit() {
        VirtualSort mysort = new VirtualSort();
        assertNotNull(mysort);
        VirtualSort.main(null);
        assertFuzzyEquals("Hello, World", systemOut().getHistory());
    }


    /**
     * This method tests the main functionality of Quicksort on an "ascii" file
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testQuicksortAscii() throws Exception {
        String[] args = new String[3];
        args[0] = "input.txt";
        args[1] = "4";
        args[2] = "statFileA.txt";

        VirtualSort.generateFile("input.txt", "10", 'a');
        VirtualSort.generateFile("input2.txt", "10", 'b');
        VirtualSort.main(args);
        assertTrue(fileChecker.checkFile("input.txt"));
        assertFalse(fileChecker.checkFile("input2.txt"));
    }


    /**
     * This method tests the main functionality of Sort on an "ascii" file
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testSortAscii() throws Exception {
        String[] args = new String[3];
        args[0] = "initial.txt";
        args[1] = "5";
        args[2] = "statFileInitial.txt";

        VirtualSort.generateFile("initial.txt", "5", 'b');
        VirtualSort.main(args);
        assertTrue(fileChecker.checkFile("initial.txt"));
    }
    
    /**
     * This method tests Sorting a large "ascii" file
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testLargeSortAscii() throws Exception {
        String[] args = new String[3];
        args[0] = "Phat.txt";
        args[1] = "10";
        args[2] = "PhatStatFile.txt";

        VirtualSort.generateFile("Phat.txt", "1000", 'a');
        VirtualSort.main(args);
        assertTrue(fileChecker.checkFile("Phat.txt"));
    }

}
