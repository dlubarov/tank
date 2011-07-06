package ast.stm;

import ast.exp.ExpressionAST;
import rst.ImportContext;
import rst.stm.StatementRST;
import rst.stm.WhileStmRST;

public class WhileStmAST extends StatementAST {
    public final ExpressionAST cond;
    public final StatementAST body;

    public WhileStmAST(ExpressionAST cond, StatementAST body) {
        this.cond = cond;
        this.body = body;
    }

    public StatementRST toRST(ImportContext ictx) {
        return new WhileStmRST(cond.toRST(ictx), body.toRST(ictx));
    }

    public String toString() {
        return String.format("while (%s) %s", cond, body);
    }
}
