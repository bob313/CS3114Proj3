import student.TestCase;

/**
 * @author CS3114 staff
 * @version 1
 */

public class VirtualSortTest
    extends TestCase {
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
    public void testQuicksortAscii()
        throws Exception
    {
        String[] args = new String[3];
        args[0] = "input.txt";
        args[1] = "4";
        args[2] = "statFileA.txt";

        VirtualSort.generateFile("input.txt", "10", 'a');
        VirtualSort.generateFile("input2.txt", "10", 'b');
        VirtualSort.main(args);
        assertFalse(fileChecker.checkFile("input.txt"));
        assertFalse(fileChecker.checkFile("input2.txt"));
    }

}
