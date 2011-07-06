package peg;

public final class Success extends ParseResult {
    public final PEG peg;
    public final Success[] children;
    public final LazyList rem;

    public Success(PEG peg, Success[] children, LazyList rem) {
        this.peg = peg;
        this.children = children;
        this.rem = rem;
    }

    public String toString() {
        return peg.toString(children);
    }
}
