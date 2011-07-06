package rst.exp;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;
import rst.TypeRST;

public class FieldAssignmentRST extends ExpressionRST {
    public final ExpressionRST target;
    public final String fieldName;
    public final ExpressionRST value;

    public FieldAssignmentRST(ExpressionRST target, String fieldName, ExpressionRST value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    public TypeDesc inferType(CompilationContext ctx) {
        // TODO: In Java, type of assignment is based on destination, but source would be more flexible...?
        TypeDesc targetDesc = target.inferType(ctx);
        TypeRST targetRST = ctx.proj.allTypes.get(targetDesc);
        return targetRST.fieldType(fieldName);
    }

    public int[] generationCode(CompilationContext ctx) {
        int[] valueGen = value.generationCode(ctx);
        int[] targetGen = target.generationCode(ctx);
        int[] code = new int[targetGen.length + valueGen.length + 3];
        System.arraycopy(valueGen, 0, code, 0, valueGen.length);
        code[valueGen.length] = Opcodes.DUP;
        System.arraycopy(targetGen, 0, code, valueGen.length + 1, targetGen.length);
        code[code.length - 2] = Opcodes.PUT_FIELD;
        code[code.length - 1] = ctx.type.fieldIndex(fieldName);
        return code;
    }

    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}
