package peg;

import java.io.IOException;

public class Option extends PEG {
    private final PEG inner;

    public Option(String name, PEG inner) {
        super(name);
        this.inner = inner;
    }

    public Success parse(LazyList in) throws IOException {
        try {
            return (Success) inner.parse(in);
        } catch (ClassCastException e) {
            return new Success(Lambda.INST, null, in);
        }
    }
}
