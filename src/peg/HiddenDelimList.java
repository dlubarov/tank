package peg;

import java.io.IOException;
import java.util.*;

public class HiddenDelimList extends PEG {
    private final PEG item, sep;

    public HiddenDelimList(String name, PEG item, PEG sep) {
        super(name);
        this.item = item;
        this.sep = sep;
    }

    public ParseResult parse(LazyList in) throws IOException {
        List<Success> children = new ArrayList<Success>();
        try {
            Success res = (Success) item.parse(in);
            children.add(res); in = res.rem;
        } catch (ClassCastException e) {
            return Failure.INST;
        }
        for (;;)
            try {
                Success res = (Success) sep.parse(in);
                res = (Success) item.parse(res.rem);
                children.add(res); in = res.rem;
            } catch (ClassCastException e) {
                return new Success(this, children.toArray(new Success[0]), in);
            }
    }
}
