package peg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SequenceWS extends Sequence {
    public SequenceWS(String name, PEG... parts) {
        super(name, parts);
    }

    public ParseResult parse(LazyList in) throws IOException {
        try {
            List<Success> children = new ArrayList<Success>();
            boolean first = true;
            for (PEG part : parts) {
                Success res;
                if (first)
                    first = false;
                else
                    in = ((Success) PEG.anyWS.parse(in)).rem;
                res = (Success) part.parse(in);
                children.add(res); in = res.rem;
            }
            return new Success(this, children.toArray(new Success[0]), in);
        } catch (ClassCastException e) {
            return Failure.INST;
        }
    }
}
