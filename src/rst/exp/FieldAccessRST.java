package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;
import rst.FieldRST;
import rst.TypeRST;

public class FieldAccessRST extends ExpressionRST {
    public final ExpressionRST target;
    public final String fieldName;

    public FieldAccessRST(ExpressionRST target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        TypeDesc targetDesc = target.inferType(ctx);
        TypeRST targetRST = ctx.proj.allTypes.get(targetDesc);
        return targetRST.fieldType(fieldName);
    }

    public int[] generationCode(CompilationContext ctx) {
        int idx = ctx.type.fieldIndex(fieldName);
        return new int[] {Opcodes.GET_FIELD, idx};
    }

    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}
