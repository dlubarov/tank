package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;

public class LitBoolRST extends ExpressionRST {
    public final boolean value;

    public LitBoolRST(boolean value) {
        this.value = value;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        return new TypeDesc("core", "Bool");
    }

    public int[] generationCode(CompilationContext ctx) {
        return new int[] {value ? Opcodes.CONST_TRUE : Opcodes.CONST_FALSE};
    }

    public String toString() {
        return Boolean.toString(value);
    }
}
