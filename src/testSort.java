import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import student.TestCase;
public class testSort extends TestCase{

    private Sorting sort;
    
    public void testSwap() throws IOException {
        FileOutputStream f = new FileOutputStream("op");
        byte[] arr = {32, 72, 32, 32, 32, 80, 32, 32, 32, 90, 32, 32, 32, 71, 32, 32, 32, 84, 32, 32, 32, 89, 32, 32};
        sort = new Sorting("SD", "bs", f);
        sort.setarr(arr);
        sort.testInsertion(0, (arr.length/4)-1);
        System.out.println("hi");
    }
}
