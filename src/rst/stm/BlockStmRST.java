package rst.stm;

import rst.CompilationContext;
import rst.CompilationResult;

public class BlockStmRST extends StatementRST {
    public final StatementRST[] parts;

    public BlockStmRST(StatementRST[] parts) {
        this.parts = parts;
    }

    public CompilationResult compile(CompilationContext ctx) {
        int[][] partsCode = new int[parts.length][];
        CompilationContext innerCtx = new CompilationContext(ctx);
        int pos = 0;
        for (int i = 0; i < parts.length; ++i) {
            CompilationResult res = parts[i].compile(innerCtx);
            pos += (partsCode[i] = res.code).length;
            innerCtx = res.ctx;
        }
        
        int[] code = new int[pos];
        pos = 0;
        for (int i = 0; i < parts.length; ++i) {
            System.arraycopy(partsCode[i], 0, code, pos, partsCode[i].length);
            pos += partsCode[i].length;
        }
        return new CompilationResult(ctx, code);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (StatementRST stm : parts)
            sb.append(stm);
        return sb.append('}').toString();
    }
}
