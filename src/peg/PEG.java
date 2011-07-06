package peg;

import java.io.IOException;
import java.util.Arrays;

public abstract class PEG {
    public final String name;
    
    public PEG(String name) {
        this.name = name;
    }

    public abstract ParseResult parse(LazyList in) throws IOException;

    public final boolean match(LazyList in) throws IOException {
        return parse(in) instanceof Success;
    }

    public final String toString() {
        return name;
    }

    public String toString(Success[] children) {
        String s = name;
        if (children != null)
            s += Arrays.toString(children);
        return s;
    }

    public static final PEG oneWS = new Disjunction("oneWS",
            new Char(' '), new Char('\t'), new Char('\n'), new Char('\r'));
    public static final PEG anyWS = new Star("anyWS", oneWS);
    public static final PEG someWS = new Plus("someWS", oneWS);

    public PEG pr() {
        return new Packrat(this);
    }

    public PEG opt() {
        return new Option(name + "?", this);
    }

    public PEG pad(PEG lpad, PEG rpad) {
        return new Pad(name, lpad, this, rpad);
    }

    public PEG pad(PEG rlpad) {
        return new Pad(name, this, rlpad);
    }

    public PEG padLeft(PEG lpad) {
        return new Pad(name, lpad, this, Lambda.INST);
    }

    public PEG padRight(PEG rpad) {
        return new Pad(name, Lambda.INST, this, rpad);
    }

    public PEG padWS() {
        return new Pad(name, this, anyWS);
    }
}
