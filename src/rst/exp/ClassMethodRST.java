package rst.exp;

import core.MethodDesc;
import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;
import rst.TypeRST;

public class ClassMethodRST extends ExpressionRST {
    public final TypeDesc owner;
    public final String methodName;
    public final ExpressionRST[] args;

    public ClassMethodRST(TypeDesc owner, String methodName, ExpressionRST[] args) {
        this.owner = owner;
        this.methodName = methodName;
        this.args = args;
    }

    private MethodDesc findMethod(CompilationContext ctx) {
        TypeDesc[] argTypes = new TypeDesc[args.length];
        for (int i = 0; i < args.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        return ctx.proj.getMatchingClassMethod(owner, methodName, argTypes);
    }

    public TypeDesc inferType(CompilationContext ctx) {
        return findMethod(ctx).retType;
    }

    public int[] generationCode(CompilationContext ctx) {
        int[][] genArgsParts = new int[args.length][];
        int genArgsPos = 0;
        for (int i = 0; i < args.length; ++i)
            genArgsPos += (genArgsParts[i] = args[i].generationCode(ctx)).length;
        int[] genArgs = new int[genArgsPos];
        genArgsPos = 0;
        for (int i = 0; i < args.length; ++i) {
            System.arraycopy(genArgsParts[i], 0, genArgs, genArgsPos, genArgsParts[i].length);
            genArgsPos += genArgsParts[i].length;
        }

        int[] code = new int[genArgs.length + 2];
        System.arraycopy(genArgs, 0, code, 0, genArgs.length);
        code[genArgs.length] = Opcodes.INVOKE_STATIC;
        code[genArgs.length + 1] = ctx.meth.methodPoolIndex(findMethod(ctx));
        return code;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(owner).append('.').append(methodName).append('(');
        boolean first = true;
        for (ExpressionRST arg : args) {
            if (first)
                first = false;
            else
                sb.append(", ");
            sb.append(arg);
        }
        return sb.append(')').toString();
    }
}
