package rst.stm;

import core.Opcodes;
import rst.CompilationContext;
import rst.CompilationResult;
import rst.exp.ExpressionRST;

public class IfElseStmRST extends StatementRST implements Opcodes {
    public final ExpressionRST cond;
    public final StatementRST bodyTrue;
    public final StatementRST bodyFalse;
    
    public IfElseStmRST(ExpressionRST cond, StatementRST bodyTrue, StatementRST bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }

    public CompilationResult compile(CompilationContext ctx) {
        int[] condCode = cond.generationCode(ctx);
        int[] trueCode = bodyTrue.compile(ctx).code;
        int[] falseCode = bodyFalse.compile(ctx).code;
        int[] code = new int[condCode.length + trueCode.length + falseCode.length + 4];
        System.arraycopy(condCode, 0, code, 0, condCode.length);
        code[condCode.length] = JUMP_COND;
        code[condCode.length + 1] = falseCode.length + 2;
        System.arraycopy(falseCode, 0, code, condCode.length + 2, falseCode.length);
        code[condCode.length + falseCode.length + 2] = JUMP;
        code[condCode.length + falseCode.length + 3] = trueCode.length;
        System.arraycopy(trueCode, 0, code, condCode.length + falseCode.length + 4, trueCode.length);
        return new CompilationResult(ctx, code);
    }

    public String toString() {
        return String.format("if (%s) %s else %s", cond, bodyTrue, bodyFalse);
    }
}
