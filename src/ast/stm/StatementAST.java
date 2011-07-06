package ast.stm;

import rst.ImportContext;
import rst.stm.StatementRST;

public abstract class StatementAST {
    public abstract String toString();

    public abstract StatementRST toRST(ImportContext ictx);
}
