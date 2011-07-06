package peg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisibleDelimList extends PEG {
    private final PEG item, sep;
    
    public VisibleDelimList(String name, PEG item, PEG sep) {
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
                Success resSep = (Success) sep.parse(in),
                        resItem = (Success) item.parse(resSep.rem);
                children.add(resSep); children.add(resItem);
                in = resItem.rem;
            } catch (ClassCastException e) {
                return new Success(this, children.toArray(new Success[0]), in);
            }
    }
}
