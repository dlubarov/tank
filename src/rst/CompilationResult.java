package rst;

public class CompilationResult {
    public final CompilationContext ctx;
    public final int[] code;

    public CompilationResult(CompilationContext ctx, int... code) {
        this.ctx = ctx;
        this.code = code;
    }
}
