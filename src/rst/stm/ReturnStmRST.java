package rst.stm;

import core.Opcodes;
import rst.CompilationContext;
import rst.CompilationResult;
import rst.exp.ExpressionRST;

public class ReturnStmRST extends StatementRST {
    private final ExpressionRST value;

    public ReturnStmRST(ExpressionRST value) {
        this.value = value;
    }
    
    public CompilationResult compile(CompilationContext ctx) {
        if (value == null)
            return new CompilationResult(ctx, Opcodes.RETURN_VOID);
        int[] valueCode = value.generationCode(ctx);
        int[] code = new int[valueCode.length + 1];
        System.arraycopy(valueCode, 0, code, 0, valueCode.length);
        code[valueCode.length] = Opcodes.RETURN_OBJECT;
        return new CompilationResult(ctx, code);
    }

    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;");
    }
}
