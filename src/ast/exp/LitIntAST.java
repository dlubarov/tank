package ast.exp;

import rst.ImportContext;
import rst.exp.ExpressionRST;
import rst.exp.LitIntRST;

public class LitIntAST extends ExpressionAST {
    public final int val;

    public LitIntAST(int val) {
        this.val = val;
    }
    
    public String toString() {
        return Integer.toString(val);
    }

    public ExpressionRST toRST(ImportContext ictx) {
        return new LitIntRST(val);
    }
}
