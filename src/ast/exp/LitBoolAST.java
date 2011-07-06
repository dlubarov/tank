package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;
import rst.exp.LitBoolRST;

public class LitBoolAST extends ExpressionAST {
    // TODO: private constructor
    
    public final boolean val;

    public LitBoolAST(boolean val) {
        this.val = val;
    }
    
    public String toString() {
        return Boolean.toString(val);
    }

    public ExpressionRST toRST(ImportContext ictx) {
        return new LitBoolRST(val);
    }
}
