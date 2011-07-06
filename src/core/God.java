package core;

import prim.*;

import java.util.*;

public final class God {
    private static final Map<TypeDesc, TypeDef> loadedTypes;
    private static final Map<MethodDesc, TMethod> loadedMethods;

    public static TypeDef[] nativeTypes;

    static {
        loadedTypes = new HashMap<TypeDesc, TypeDef>();
        loadedMethods = new HashMap<MethodDesc, TMethod>();
        init();
    }

    public static void init() {
        nativeTypes = new TypeDef[] {
            PrimitiveObject.type,
            PrimitiveInt.type,
            PrimitiveBool.type,
            PrimitiveString.type,
            PrimitiveConsole.type,
            NothingType.INST
        };
        for (TypeDef type : nativeTypes)
            type.link();
    }

    public static TypeDef getType(TypeDesc desc) {
        return loadedTypes.get(desc);
    }

    public static TMethod getMethod(MethodDesc desc) {
        return loadedMethods.get(desc);
    }

    public static void newType(TypeDef type) {
        loadedTypes.put(type.desc, type);
    }

    public static void newMethod(TMethod m) {
        loadedMethods.put(m.desc, m);
    }
}
