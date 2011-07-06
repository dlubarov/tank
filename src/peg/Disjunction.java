package peg;

import java.io.IOException;

public class Disjunction extends PEG {
    public PEG[] options;

    public Disjunction(String name, PEG... options) {
        super(name);
        this.options = options;
    }

    public ParseResult parse(LazyList in) throws IOException {
        for (PEG opt : options) {
            ParseResult res = opt.parse(in);
            if (res instanceof Success)
                return res;
        }
        return Failure.INST;
    }
}
