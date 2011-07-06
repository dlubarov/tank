package ast;

import ast.stm.BlockStmAST;

public class MethodAST {
    public final TypedVariable meth;
    public final TypedVariable[] params;
    public final BlockStmAST code;
    public final boolean isStatic;

    public MethodAST(TypedVariable meth, TypedVariable[] params, BlockStmAST code, boolean isStatic) {
        this.meth = meth;
        this.params = params;
        this.code = code;
        this.isStatic = isStatic;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        sb.append(meth).append('(');
        boolean first = true;
        for (TypedVariable p : params) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(p);
        }
        sb.append(") ").append(code);
        return sb.toString();
    }
}
