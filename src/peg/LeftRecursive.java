package peg;

import java.io.IOException;
import java.util.*;

public class LeftRecursive extends PEG {
    private final PEG inner;
    private final Map<LazyList, ParseResult> mem;

    public LeftRecursive(PEG inner) {
        super(inner.name);
        this.inner = inner;
        mem = new HashMap<LazyList, ParseResult>();
    }

    public ParseResult parse(LazyList in) throws IOException {
        throw new UnsupportedOperationException(); // FIXME: left-recursive PEGs
    }

    public String toString(Success[] children) {
        return inner.toString(children);
    }
}
