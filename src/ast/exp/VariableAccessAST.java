package ast.exp;

import rst.ImportContext;
import rst.exp.VariableAccessRST;

public class VariableAccessAST extends ExpressionAST {
    public final String name;

    public VariableAccessAST(String name) {
        this.name = name;
    }

    public VariableAccessRST toRST(ImportContext ictx) {
        return new VariableAccessRST(name);
    }

    public String toString() {
        return name;
    }
}
