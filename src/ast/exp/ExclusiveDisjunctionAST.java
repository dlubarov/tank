package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;

public class ExclusiveDisjunctionAST extends ExpressionAST {
    public final ExpressionAST left, right;

    public ExclusiveDisjunctionAST(ExpressionAST left, ExpressionAST right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return String.format("(%s ^ %s)", left, right);
    }

    public ExpressionRST toRST(ImportContext ictx) {
        return new InvocationAST(new FieldAccessAST(left, "^"), new ExpressionAST[] {right}).toRST(ictx);
    }
}
