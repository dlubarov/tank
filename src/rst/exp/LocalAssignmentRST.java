package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;

public class LocalAssignmentRST extends ExpressionRST {
    public final String localName;
    public final ExpressionRST value;

    public LocalAssignmentRST(String localName, ExpressionRST value) {
        this.localName = localName;
        this.value = value;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        // TODO: In Java, type of assignment is based on destination, but source would be more flexible...?
        return ctx.getLocalType(localName);
    }

    public int[] generationCode(CompilationContext ctx) {
        int[] genValue = value.generationCode(ctx);
        int[] code = new int[genValue.length + 3];
        System.arraycopy(genValue, 0, code, 0, genValue.length);
        code[genValue.length] = Opcodes.DUP;
        code[genValue.length + 1] = Opcodes.PUT_LOCAL;
        code[genValue.length + 2] = ctx.localIndex(localName);
        return code;
    }

    public String toString() {
        return String.format("%s = %s", localName, value);
    }
}
