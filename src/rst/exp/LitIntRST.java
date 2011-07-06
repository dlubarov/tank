package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;

public class LitIntRST extends ExpressionRST {
    public final int value;

    public LitIntRST(int value) {
        this.value = value;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        return new TypeDesc("core", "Int");
    }

    public int[] generationCode(CompilationContext ctx) {
        return new int[] {Opcodes.CONST_INT, value};
    }

    public String toString() {
        return Integer.toString(value);
    }
}
