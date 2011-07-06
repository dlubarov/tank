package core;

public abstract class TMethod {
    public final MethodDesc desc;
    protected final int numParams;
    private final MethodDesc[] methodDescPool;
    protected final TMethod[] methodPool;
    private final TypeDef[] paramTypes;
    public final boolean isStatic;

    public TMethod(MethodDesc desc, MethodDesc[] methodDescPool, boolean isStatic) {
        this.desc = desc;
        numParams = desc.paramTypes.length;
        paramTypes = new TypeDef[numParams];

        this.methodDescPool = methodDescPool;
        methodPool = new TMethod[methodDescPool.length];
        this.isStatic = isStatic;
        God.newMethod(this);
    }

    public TMethod(TypeDesc owner, String name,
                   TypeDesc[] paramTypeDescs, String[] paramNames,
                   TypeDesc retType, MethodDesc[] methodDescPool, boolean isStatic) {
        this(new MethodDesc(owner, name, paramTypeDescs, paramNames, retType), methodDescPool, isStatic);
    }

    public void link() {
        for (int i = 0; i < numParams; ++i)
            paramTypes[i] = God.getType(desc.paramTypes[i]);
        for (int i = 0; i < methodDescPool.length; ++i)
            methodPool[i] = God.getMethod(methodDescPool[i]);
    }

    public boolean canOverride(TMethod that) {
        if (!desc.name.equals(that.desc.name))
            return false;
        if (numParams != that.numParams)
            return false;
        for (int i = 0; i < numParams; ++i)
            if (!paramTypes[i].subtypeOf(that.paramTypes[i]))
                return false;
        return true;
    }

    public boolean isInstanceMethod() {
        // FIXME: temp hack
        return numParams > 0 && desc.owner.equals(desc.paramTypes[0]);
    }

    public abstract boolean isAbstract();
    public abstract TObject invoke(TObject[] args);

    public String toString() {
        return desc.toString();
    }
}
