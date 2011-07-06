package ast.stm;

import ast.exp.ExpressionAST;
import rst.ImportContext;
import rst.stm.ExpStmRST;
import rst.stm.ReturnStmRST;

public class ReturnStmAST extends StatementAST {
    public final ExpressionAST exp;

    public ReturnStmAST(ExpressionAST exp) {
        this.exp = exp;
    }

    public ReturnStmRST toRST(ImportContext ictx) {
        if (exp == null)
            return new ReturnStmRST(null);
        return new ReturnStmRST(exp.toRST(ictx));
    }

    public String toString() {
        if (exp == null)
            return "return;";
        return String.format("return %s;", exp);
    }
}
