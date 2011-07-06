package core;

import rst.MethodRST;

import java.util.*;

public class TypeDef {
    public final TypeDesc desc;
    public final TypeDesc[] superTypeDescs;
    private final TypeDef[] superTypes;
    private final TMethod[] ownedMethods;
    public final Map<TMethod, TMethod> vtable;
    private boolean linked = false;
    
    public TypeDef(TypeDesc desc, TypeDesc[] superTypeDescs, TMethod[] ownedMethods) {
        this.desc = desc;
        this.superTypeDescs = superTypeDescs;
        superTypes = new TypeDef[superTypeDescs.length];
        this.ownedMethods = ownedMethods;
        vtable = new HashMap<TMethod, TMethod>();
        God.newType(this);
    }

    private Collection<TMethod> findBaseMethods(TMethod m) {
        Collection<TMethod> result = new HashSet<TMethod>();
        for (TMethod myMethod : ownedMethods)
            if (m.canOverride(myMethod))
                result.add(myMethod);
        for (TypeDef sup : superTypes)
            result.addAll(sup.findBaseMethods(m));
        return result;
    }

    private Collection<TMethod> findInstanceMethods() {
        Collection<TMethod> result = new HashSet<TMethod>();
        for (TMethod myMethod : ownedMethods)
            if (myMethod.isInstanceMethod())
                result.add(myMethod);
        for (TypeDef sup : superTypes)
            result.addAll(sup.findInstanceMethods());
        return result;
    }

    private TMethod findBestImplementation(TMethod m) {
        for (TMethod myMethod : ownedMethods)
            if (myMethod.isInstanceMethod() && !myMethod.isAbstract() && myMethod.canOverride(m))
                return myMethod;
        for (TypeDef sup : superTypes) {
            TMethod impl = sup.findBestImplementation(m);
            if (impl != m)
                return impl;
        }
        return m;
    }

    public void link() {
        if (linked)
            return;
        for (int i = 0; i < superTypeDescs.length; ++i)
            (superTypes[i] = God.getType(superTypeDescs[i])).link();
        for (TMethod m : ownedMethods)
            m.link();
        for (TMethod m : findInstanceMethods()) {
            TMethod impl = findBestImplementation(m);
            for (TMethod base : findBaseMethods(impl))
                vtable.put(base, impl);
        }
        linked = true;
    }

    public boolean strictSubtypeOf(TypeDef that) {
        for (TypeDef superType : superTypes)
            if (superType.subtypeOf(that))
                return true;
        return false;
    }

    public boolean subtypeOf(TypeDef that) {
        return this == that || strictSubtypeOf(that);
    }

    public void runMain(String[] args) {
        // TODO: use args
        assert linked;
        for (TMethod m : ownedMethods)
            if (m.desc.name.equals("main") && m.numParams == 0)
                m.invoke(new TObject[0]);
    }

    public MethodRST[] getMethodRSTs() {
        MethodRST[] res = new MethodRST[ownedMethods.length];
        for (int i = 0; i < res.length; ++i)
            res[i] = new MethodRST(ownedMethods[i].desc, ownedMethods[i].isStatic);
        return res;
    }

    public String toString() {
        return desc.toString();
    }
}
