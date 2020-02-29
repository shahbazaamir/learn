package error;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Error extends PrintStream {

    public Error(String fileName) throws FileNotFoundException {
        super(fileName);
    }


}
