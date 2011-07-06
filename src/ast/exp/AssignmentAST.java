package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;
import rst.exp.FieldAssignmentRST;
import rst.exp.LocalAssignmentRST;

public class AssignmentAST extends ExpressionAST {
    public final ExpressionAST left, right;

    public AssignmentAST(ExpressionAST left, ExpressionAST right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return String.format("(%s = %s)", left, right);
    }

    public ExpressionRST toRST(ImportContext ictx) {
        if (left instanceof VariableAccessAST)
            return new LocalAssignmentRST(((VariableAccessAST) left).name, right.toRST(ictx));
        if (left instanceof FieldAccessAST) {
            FieldAccessAST acc = (FieldAccessAST) left;
            return new FieldAssignmentRST(acc.target.toRST(ictx), acc.fieldName, right.toRST(ictx));
        }
        if (left instanceof InvocationAST) {
            InvocationAST call = (InvocationAST) left;
            ExpressionAST[] newArgs = new ExpressionAST[call.arguments.length + 1];
            System.arraycopy(call.arguments, 0, newArgs, 0, call.arguments.length);
            newArgs[call.arguments.length] = right;
            return new InvocationAST(new FieldAccessAST(call.target, "set"), newArgs).toRST(ictx);
        }
        throw new RuntimeException("assignment target is not an lvalue");
    }
}
