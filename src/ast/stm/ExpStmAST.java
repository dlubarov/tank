package ast.stm;

import ast.exp.ExpressionAST;
import rst.ImportContext;
import rst.stm.ExpStmRST;

public class ExpStmAST extends StatementAST {
    public final ExpressionAST exp;

    public ExpStmAST(ExpressionAST exp) {
        this.exp = exp;
    }

    public ExpStmRST toRST(ImportContext ictx) {
        return new ExpStmRST(exp.toRST(ictx));
    }

    public String toString() {
        return exp + ";";
    }
}
