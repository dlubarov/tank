package rst.stm;

import rst.CompilationContext;
import rst.CompilationResult;

public abstract class StatementRST {
    public abstract CompilationResult compile(CompilationContext ctx);
    public abstract String toString();
}
