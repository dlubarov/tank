package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;
import rst.exp.FieldAccessRST;

public class FieldAccessAST extends ExpressionAST {
    public final ExpressionAST target;
    public final String fieldName;

    public FieldAccessAST(ExpressionAST target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }

    public ExpressionRST toRST(ImportContext ictx) {
        return new FieldAccessRST(target.toRST(ictx), fieldName);
    }
}
