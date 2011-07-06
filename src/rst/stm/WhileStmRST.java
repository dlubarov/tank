package rst.stm;

import core.OpcodeNames;
import core.Opcodes;
import rst.CompilationContext;
import rst.CompilationResult;
import rst.exp.ExpressionRST;

public class WhileStmRST extends StatementRST implements Opcodes {
    public final ExpressionRST cond;
    public final StatementRST body;
    
    public WhileStmRST(ExpressionRST cond, StatementRST body) {
        this.cond = cond;
        this.body = body;
    }
    
    public CompilationResult compile(CompilationContext ctx) {
        // Let's hope there are no off-by-one errors :-)
        int[] condCode = cond.generationCode(ctx);
        int[] bodyCode = body.compile(ctx).code;
        int[] code = new int[condCode.length + 3 + bodyCode.length + 2];
        System.arraycopy(condCode, 0, code, 0, condCode.length);
        code[condCode.length] = BOOL_NEG;
        code[condCode.length + 1] = JUMP_COND;
        code[condCode.length + 2] = bodyCode.length + 2;
        System.arraycopy(bodyCode, 0, code, condCode.length + 3, bodyCode.length);
        code[condCode.length + bodyCode.length + 3] = JUMP;
        code[condCode.length + bodyCode.length + 4] = -(bodyCode.length + condCode.length + 5);
        return new CompilationResult(ctx, code);
    }
    
    public String toString() {
        return String.format("{while (%s) %s}", cond, body);
    }
}
