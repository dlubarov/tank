package rst;

import ast.MethodAST;
import core.MethodDesc;
import core.NormalMethod;
import core.TMethod;
import core.TypeDesc;
import rst.stm.StatementRST;

import java.util.*;

public class MethodRST {
    public final TypeRST owner;
    public final MethodDesc desc;
    public final StatementRST code;
    public final Map<MethodDesc, Integer> methodPool;
    public final boolean isStatic;
    public int numLocals;

    public MethodRST(MethodDesc desc, boolean isStatic) {
        owner = null;
        this.desc = desc;
        code = null;
        methodPool = null;
        this.isStatic = isStatic;
        numLocals = 0;
    }

    public MethodRST(TypeRST owner, MethodAST ast, ImportContext ictx) {
        this.owner = owner;
        TypeDesc[] paramTypes = new TypeDesc[ast.params.length];
        for (int i = 0; i < ast.params.length; ++i)
            paramTypes[i] = ictx.resolve(ast.params[i].type);
        String[] paramNames = new String[ast.params.length];
        for (int i = 0; i < ast.params.length; ++i)
            paramNames[i] = ast.params[i].name;
        desc = new MethodDesc(owner.desc, ast.meth.name, paramTypes, paramNames, ictx.resolve(ast.meth.type));
        code = ast.code.toRST(ictx);
        methodPool = new HashMap<MethodDesc, Integer>();
        isStatic = ast.isStatic;
        numLocals = desc.paramNames.length;
    }

    /*public int localIndex(String name) {
        for (int i = 0; i < desc.paramNames.length; ++i)
            if (desc.paramNames[i].equals(name))
                return i;
        // FIXME: allocate new locals? see Context...
        throw new NoSuchElementException(name);
    }*/

    public int methodPoolIndex(MethodDesc desc) {
        Integer idx = methodPool.get(desc);
        if (idx == null)
            methodPool.put(desc, idx = methodPool.size());
        return idx;
    }

    public TMethod compile() {
        int[] instructions = code.compile(new CompilationContext(this)).code;
        MethodDesc[] methodPoolArr = new MethodDesc[methodPool.size()];
        for (Map.Entry<MethodDesc, Integer> e : methodPool.entrySet())
            methodPoolArr[e.getValue()] = e.getKey();
        return new NormalMethod(desc, isStatic, numLocals, methodPoolArr, instructions);
    }
}
