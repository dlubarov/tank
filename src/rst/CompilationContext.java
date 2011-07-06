package rst;

import core.TypeDesc;

import java.util.*;

public class CompilationContext {
    public final Project proj;
    public final TypeRST type;
    public final MethodRST meth;
    private final List<String> locals;
    private final Map<String, TypeDesc> localTypes;

    public CompilationContext(MethodRST meth) {
        type = meth.owner;
        this.meth = meth;
        proj = type.proj;
        
        int nParams = meth.desc.paramNames.length;
        locals = new ArrayList<String>();
        for (int i = 0; i < nParams; ++i)
            locals.add(meth.desc.paramNames[i]);
        localTypes = new HashMap<String, TypeDesc>();
        for (int i = 0; i < nParams; ++i)
            localTypes.put(meth.desc.paramNames[i], meth.desc.paramTypes[i]);
    }

    public CompilationContext(CompilationContext src) {
        type = src.type; meth = src.meth; proj = src.proj;
        locals = new ArrayList<String>(src.locals);
        localTypes = new HashMap<String, TypeDesc>(src.localTypes);
    }

    public TypeDesc getLocalType(String name) {
        if (localTypes.containsKey(name))
            return localTypes.get(name);
        throw new NoSuchElementException("Local not found: " + name);
    }

    public int localIndex(String name) {
        for (int i = 0; i < locals.size(); ++i)
            if (locals.get(i).equals(name))
                return i;
        throw new NoSuchElementException("Local not found: " + name);
    }

    public CompilationContext newLocal(TypeDesc type, String name) {
        CompilationContext newCtx = new CompilationContext(this);
        newCtx.locals.add(name);
        newCtx.localTypes.put(name, type);
        meth.numLocals = Math.max(meth.numLocals, newCtx.locals.size());
        return newCtx;
    }
}
