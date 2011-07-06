package ast.stm;

import ast.exp.ExpressionAST;
import rst.ImportContext;
import rst.stm.IfElseStmRST;

public class IfElseStmAST extends StatementAST {
    public final ExpressionAST cond;
    public final StatementAST bodyTrue, bodyFalse;

    public IfElseStmAST(ExpressionAST cond, StatementAST bodyTrue, StatementAST bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }

    public IfElseStmRST toRST(ImportContext ictx) {
        return new IfElseStmRST(cond.toRST(ictx), bodyTrue.toRST(ictx), bodyFalse.toRST(ictx));
    }

    public String toString() {
        return String.format("if (%s) %s else %s", cond, bodyTrue, bodyFalse);
    }
}
