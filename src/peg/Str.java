package peg;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Str extends PEG {
    public final String s;

    public Str(String s) {
        super('"' + s + '"');
        this.s = s;
    }
    
    public ParseResult parse(LazyList in) throws IOException {
        try {
            for (char c : s.toCharArray()) {
                if (in.head() != c)
                    return Failure.INST;
                in = in.tail();
            }
            return new Success(this, null, in);
        } catch (NoSuchElementException e) {
            return Failure.INST;
        }
    }

    public String toString(Success[] children) {
        return s;
    }
}
