package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;

public abstract class ExpressionAST {
    public abstract String toString();

    public abstract ExpressionRST toRST(ImportContext ictx);
}
