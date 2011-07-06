package rst.stm;

import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;
import rst.CompilationResult;
import rst.exp.ExpressionRST;

public class LocalDefStmRST extends StatementRST {
    private final TypeDesc type;
    private final String name;
    private final ExpressionRST initVal;

    public LocalDefStmRST(TypeDesc type, String name, ExpressionRST initVal) {
        this.type = type;
        this.name = name;
        this.initVal = initVal;
    }

    public CompilationResult compile(CompilationContext ctx) {
        int[] genInitVal = initVal.generationCode(ctx);
        ctx = ctx.newLocal(type, name);
        int[] code = new int[genInitVal.length + 2];
        System.arraycopy(genInitVal, 0, code, 0, genInitVal.length);
        code[genInitVal.length] = Opcodes.PUT_LOCAL;
        code[genInitVal.length + 1] = ctx.localIndex(name);
        return new CompilationResult(ctx, code);
    }

    public String toString() {
        return String.format("%s %s = %s;", type, name, initVal);
    }
}
