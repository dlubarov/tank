package ast.stm;

import ast.TypedVariable;
import ast.exp.ExpressionAST;
import rst.ImportContext;
import rst.stm.LocalDefStmRST;

public class LocalDefStmAST extends StatementAST {
    public final TypedVariable local;
    public final ExpressionAST initVal;

    public LocalDefStmAST(TypedVariable local, ExpressionAST initVal) {
        this.local = local;
        this.initVal = initVal;
    }
    
    public LocalDefStmRST toRST(ImportContext ictx) {
        return new LocalDefStmRST(ictx.resolve(local.type), local.name, initVal.toRST(ictx));
    }
    
    public String toString() {
        return String.format("%s = %s;", local, initVal);
    }
}
