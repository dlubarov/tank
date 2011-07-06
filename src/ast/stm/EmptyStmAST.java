package ast.stm;

import rst.ImportContext;
import rst.stm.EmptyStmRST;

public class EmptyStmAST extends StatementAST {
    public static final EmptyStmAST INST = new EmptyStmAST();
    
    private EmptyStmAST() {}

    public EmptyStmRST toRST(ImportContext ictx) {
        return EmptyStmRST.INST;
    }
    
    public String toString() {
        return ";";
    }
}
