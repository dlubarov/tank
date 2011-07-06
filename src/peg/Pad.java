package peg;

import java.io.IOException;

public class Pad extends PEG {
    private final PEG lpad, elem, rpad;

    public Pad(String name, PEG lpad, PEG elem, PEG rpad) {
        super(name);
        this.lpad = lpad;
        this.elem = elem;
        this.rpad = rpad;
    }

    public Pad(String name, PEG elem, PEG lrpad) {
        this(name, lrpad, elem, lrpad);
    }

    public ParseResult parse(LazyList in) throws IOException {
        try {
            in = ((Success) lpad.parse(in)).rem;
            Success res = (Success) elem.parse(in);
            in = ((Success) rpad.parse(res.rem)).rem;
            return new Success(res.peg, res.children, in);
        } catch (ClassCastException e) {
            return Failure.INST;
        }
    }
}
