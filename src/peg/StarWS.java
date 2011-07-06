package peg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class StarWS extends PEG {
    private final PEG inner;

    public StarWS(String name, PEG inner) {
        super(name);
        this.inner = inner;
    }

    public Success parse(LazyList in) throws IOException {
        List<Success> children = new ArrayList<Success>();
        boolean first = true;
        for (;;)
            try {
                if (first)
                    first = false;
                else
                    in = ((Success) anyWS.parse(in)).rem;
                Success res = (Success) inner.parse(in);
                children.add(res); in = res.rem;
            } catch (ClassCastException e) {
                return new Success(this, children.toArray(new Success[0]), in);
            }
    }
}
