package peg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sequence extends PEG {
    public PEG[] parts;

    public Sequence(String name, PEG... parts) {
        super(name);
        this.parts = parts;
    }

    public ParseResult parse(LazyList in) throws IOException {
        try {
            List<Success> children = new ArrayList<Success>();
            for (PEG part : parts) {
                Success res = (Success) part.parse(in);
                children.add(res); in = res.rem;
            }
            return new Success(this, children.toArray(new Success[0]), in);
        } catch (ClassCastException e) {
            return Failure.INST;
        }
    }
}
