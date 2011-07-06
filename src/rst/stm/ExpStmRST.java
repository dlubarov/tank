package rst.stm;

import rst.CompilationContext;
import rst.CompilationResult;
import rst.exp.ExpressionRST;

public class ExpStmRST extends StatementRST {
    public final ExpressionRST exp;

    public ExpStmRST(ExpressionRST exp) {
        this.exp = exp;
    }

    public CompilationResult compile(CompilationContext ctx) {
        int[] genExp = exp.generationCode(ctx);
        int[] code = new int[genExp.length];
        System.arraycopy(genExp, 0, code, 0, genExp.length);
        return new CompilationResult(ctx, code);
    }

    public String toString() {
        return exp.toString() + ';';
    }
}
