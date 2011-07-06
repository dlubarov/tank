package prim;

import core.*;

public class PrimitiveBool extends TObject {
    public final boolean value;
    public static final TypeDef type;

    public static final PrimitiveBool TRUE, FALSE;

    static {
        TypeDesc desc = new TypeDesc("core", "Bool");
        TypeDesc[] superTypeDescs = {new TypeDesc("core", "Object")};

        TMethod and = new NativeMethod(desc, "&",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                return new PrimitiveBool(((PrimitiveBool) args[0]).value & ((PrimitiveBool) args[1]).value);
            }
        };

        TMethod or = new NativeMethod(desc, "|",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                return new PrimitiveBool(((PrimitiveBool) args[0]).value | ((PrimitiveBool) args[1]).value);
            }
        };

        TMethod xor = new NativeMethod(desc, "^",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                return new PrimitiveBool(((PrimitiveBool) args[0]).value ^ ((PrimitiveBool) args[1]).value);
            }
        };

        // TODO: multiply by TypeWithAdditiveIdentity
        // e.g. false*"abc" = "", true*42 = 42

        TMethod toString = new NativeMethod(desc, "toString",
                new TypeDesc[] {desc}, new String[] {"target"}, new TypeDesc("core", "String"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 1;
                PrimitiveBool x = (PrimitiveBool) args[0];
                return new PrimitiveString(String.valueOf(x.value));
            }
        };

        TMethod[] ownedMethods = {and, or, xor, toString};
        type = new TypeDef(desc, superTypeDescs, ownedMethods);
        
        TRUE = new PrimitiveBool(true);
        FALSE = new PrimitiveBool(false);
    }

    public PrimitiveBool(boolean value) {
        super(type);
        this.value = value;
    }
}
