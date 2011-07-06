package prim;

import core.*;

public class PrimitiveInt extends TObject {
    public final int value;
    public static final TypeDef type;

    static {
        TypeDesc desc = new TypeDesc("core", "Int");
        TypeDesc[] superTypeDescs = {new TypeDesc("core", "Object")};

        TMethod add = new NativeMethod(desc, "+",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveInt(a.value + b.value);
            }
        };

        TMethod sub = new NativeMethod(desc, "-",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveInt(a.value - b.value);
            }
        };

        TMethod mul = new NativeMethod(desc, "*",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveInt(a.value * b.value);
            }
        };

        TMethod div = new NativeMethod(desc, "/",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveInt(a.value / b.value);
            }
        };

        TMethod mod = new NativeMethod(desc, "%",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                desc, new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveInt(a.value % b.value);
            }
        };

        TMethod le = new NativeMethod(desc, "<=",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value <= b.value);
            }
        };

        TMethod lt = new NativeMethod(desc, "<",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value < b.value);
            }
        };

        TMethod ge = new NativeMethod(desc, ">=",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value >= b.value);
            }
        };

        TMethod gt = new NativeMethod(desc, ">",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value > b.value);
            }
        };

        TMethod eq = new NativeMethod(desc, "==",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value == b.value);
            }
        };

        TMethod ne = new NativeMethod(desc, "!=",
                new TypeDesc[] {desc, desc}, new String[] {"a", "b"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 2;
                PrimitiveInt a = (PrimitiveInt) args[0], b = (PrimitiveInt) args[1];
                return new PrimitiveBool(a.value != b.value);
            }
        };
        
        TMethod toString = new NativeMethod(desc, "toString",
                new TypeDesc[] {desc}, new String[] {"target"},
                new TypeDesc("core", "Bool"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 1;
                PrimitiveInt x = (PrimitiveInt) args[0];
                return new PrimitiveString(String.valueOf(x.value));
            }
        };

        TMethod[] ownedMethods = {add, sub, mul, div, mod, le, lt, ge, gt, eq, ne, toString};
        type = new TypeDef(desc, superTypeDescs, ownedMethods);
    }
    
    public PrimitiveInt(int value) {
        super(type);
        this.value = value;
    }
}
