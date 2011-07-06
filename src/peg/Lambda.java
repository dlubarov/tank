package peg;

public class Lambda extends PEG {
    public final static Lambda INST = new Lambda();

    private Lambda() {
        super("\u03BB");
    }

    public ParseResult parse(LazyList in) {
        return new Success(this, null, in);
    }
}
