package peg;

import java.io.IOException;
import java.util.*;

public class Star extends PEG {
    private final PEG inner;

    public Star(String name, PEG inner) {
        super(name);
        this.inner = inner;
    }

    public Success parse(LazyList in) throws IOException {
        List<Success> children = new ArrayList<Success>();
        for (;;)
            try {
                Success res = (Success) inner.parse(in);
                children.add(res); in = res.rem;
            } catch (ClassCastException e) {
                return new Success(this, children.toArray(new Success[0]), in);
            }
    }
}
