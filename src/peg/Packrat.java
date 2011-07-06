package peg;

import java.io.IOException;
import java.util.*;

public class Packrat extends PEG {
    private final PEG inner;
    private final Map<LazyList, ParseResult> mem;

    public Packrat(PEG inner) {
        super(inner.name);
        this.inner = inner;
        mem = new HashMap<LazyList, ParseResult>();
    }

    public ParseResult parse(LazyList in) throws IOException {
        ParseResult res = mem.get(in);
        if (res == null)
            mem.put(in, res = inner.parse(in));
        return res;
    }
    
    public String toString(Success[] children) {
        return inner.toString(children);
    }
}
