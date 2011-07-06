package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;

public class VariableAccessRST extends ExpressionRST {
    public final String name;

    public VariableAccessRST(String name) {
        this.name = name;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        return ctx.getLocalType(name);
    }

    public int[] generationCode(CompilationContext ctx) {
        int idx = ctx.localIndex(name);
        return new int[] {Opcodes.GET_LOCAL, idx};
    }

    public String toString() {
        return name;
    }
}
