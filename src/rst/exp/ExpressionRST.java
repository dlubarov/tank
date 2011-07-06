package rst.exp;

import core.TypeDesc;
import rst.CompilationContext;

public abstract class ExpressionRST {
    public abstract String toString();

    public abstract TypeDesc inferType(CompilationContext ctx);
    public abstract int[] generationCode(CompilationContext ctx);
}
