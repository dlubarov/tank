package prim;

import core.*;

public class PrimitiveConsole extends TObject {
    public static final TypeDef type;

    static {
        TypeDesc desc = new TypeDesc("core", "Console");
        TypeDesc[] superTypeDescs = {new TypeDesc("core", "Object")};

        TMethod println = new NativeMethod(desc, "println",
                                           new TypeDesc[] {new TypeDesc("core", "Object")},
                                           new String[] {"obj"},
                                           null,
                                           new MethodDesc[] {new MethodDesc(new TypeDesc("core", "Object"),
                                                                            "toString",
                                                                            new TypeDesc[] {new TypeDesc("core", "Object")},
                                                                            new String[] {"target"},
                                                                            null)},
                                           false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 1;
                TObject arg = args[0];
                TMethod m = arg.type.vtable.get(methodPool[0]);
                String s = ((PrimitiveString) m.invoke(new TObject[] {arg})).value;
                System.out.println(s);
                return null;
            }
        };

        TMethod[] ownedMethods = {println};
        type = new TypeDef(desc, superTypeDescs, ownedMethods);
    }

    public PrimitiveConsole() {
        super(type);
        assert false;
    }
}
