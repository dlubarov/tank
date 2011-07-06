package ast.stm;

import rst.ImportContext;
import rst.stm.BlockStmRST;
import rst.stm.StatementRST;

public class BlockStmAST extends StatementAST {
    public final StatementAST[] parts;

    public BlockStmAST(StatementAST... parts) {
        this.parts = parts;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (StatementAST stm : parts)
            sb.append(stm);
        return sb.append('}').toString();
    }

    public StatementRST toRST(ImportContext ictx) {
        StatementRST[] rParts = new StatementRST[parts.length];
        for (int i = 0; i < parts.length; ++i)
            rParts[i] = parts[i].toRST(ictx);
        return new BlockStmRST(rParts);
    }
}
