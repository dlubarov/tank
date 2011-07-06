package rst.exp;

import core.MethodDesc;
import core.Opcodes;
import core.TypeDesc;
import rst.CompilationContext;

public class InstanceMethodRST extends ExpressionRST {
    public final ExpressionRST target;
    public final String methodName;
    public final ExpressionRST[] args;

    public InstanceMethodRST(ExpressionRST target, String methodName, ExpressionRST[] args) {
        this.target = target;
        this.methodName = methodName;
        this.args = args;
    }

    private MethodDesc findMethod(CompilationContext ctx) {
        TypeDesc[] argTypes = new TypeDesc[args.length];
        for (int i = 0; i < args.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        return ctx.proj.getMatchingMethod(target.inferType(ctx), methodName, argTypes);
    }

    public TypeDesc inferType(CompilationContext ctx) {
        return findMethod(ctx).retType;
    }

    public int[] generationCode(CompilationContext ctx) {
        int[] genTarget = target.generationCode(ctx);
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

        int[] code = new int[genTarget.length + genArgs.length + 2];
        System.arraycopy(genTarget, 0, code, 0, genTarget.length);
        System.arraycopy(genArgs, 0, code, genTarget.length, genArgs.length);
        code[code.length - 2] = Opcodes.INVOKE_VIRTUAL;
        code[code.length - 1] = ctx.meth.methodPoolIndex(findMethod(ctx));
        return code;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(target).append('(');
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
