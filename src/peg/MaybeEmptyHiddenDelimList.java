package peg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaybeEmptyHiddenDelimList extends PEG {
    private final PEG item, sep;

    public MaybeEmptyHiddenDelimList(String name, PEG item, PEG sep) {
        super(name);
        this.item = item;
        this.sep = sep;
    }

    public ParseResult parse(LazyList in) throws IOException {
        // TODO: make better?
        List<Success> children = new ArrayList<Success>();
        try {
            Success res = (Success) item.parse(in);
            children.add(res); in = res.rem;
        } catch (ClassCastException e) {
            return new Success(this, new Success[0], in);
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
