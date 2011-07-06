package rst.stm;

import rst.CompilationContext;
import rst.CompilationResult;

public class EmptyStmRST extends StatementRST {
    public static final EmptyStmRST INST = new EmptyStmRST();

    private EmptyStmRST() {}

    public CompilationResult compile(CompilationContext ctx) {
        return new CompilationResult(ctx);
    }
    
    public String toString() {
        return ";";
    }
}
