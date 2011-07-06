package peg;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Char extends PEG {
    public final char val;

    public Char(char val) {
        super(Character.toString(val));
        this.val = val;
    }
    
    public ParseResult parse(LazyList in) throws IOException {
        try {
            if (in.head() == val)
                return new Success(this, null, in.tail());
            return Failure.INST;
        } catch (NoSuchElementException e) {
            return Failure.INST;
        }
    }

    public String toString(Success[] children) {
        return String.valueOf(val);
    }
}
