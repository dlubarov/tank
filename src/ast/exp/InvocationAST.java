package ast.exp;

import rst.ImportContext;
import rst.exp.ClassMethodRST;
import rst.exp.ExpressionRST;
import rst.exp.InstanceMethodRST;

public class InvocationAST extends ExpressionAST {
    public final ExpressionAST target;
    public final ExpressionAST[] arguments;

    public InvocationAST(ExpressionAST target, ExpressionAST[] arguments) {
        this.target = target;
        this.arguments = arguments;
    }

    public ExpressionRST toRST(ImportContext ictx) {
        ExpressionRST[] argsRST = new ExpressionRST[arguments.length];
        for (int i = 0; i < arguments.length; ++i)
            argsRST[i] = arguments[i].toRST(ictx);
        
        if (target instanceof VariableAccessAST)
            return new InstanceMethodRST(target.toRST(ictx), "get", argsRST);
        
        FieldAccessAST targetField = (FieldAccessAST) target;
        if (targetField.target instanceof VariableAccessAST) {
            String typeName = ((VariableAccessAST) targetField.target).name;
            if (ictx.typeExists(typeName))
                return new ClassMethodRST(ictx.resolve(typeName), targetField.fieldName, argsRST);
        }
        return new InstanceMethodRST(targetField.target.toRST(ictx), targetField.fieldName, argsRST);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(target).append('(');
        boolean first = true;
        for (ExpressionAST arg : arguments) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(arg);
        }
        return sb.append(')').toString();
    }
}
