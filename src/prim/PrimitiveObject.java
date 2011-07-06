package prim;

import core.*;

public class PrimitiveObject extends TObject {
    public PrimitiveObject(TypeDef type) {
        super(type);
    }

    public static final TypeDef type;

    static {
        TypeDesc desc = new TypeDesc("core", "Object");
        TMethod toString = new NativeMethod(desc, "toString", new TypeDesc[] {desc}, new String[] {"target"},
                new TypeDesc("core", "String"), new MethodDesc[0], false) {
            public TObject invoke(TObject[] args) {
                assert args.length == 1;
                return new PrimitiveString(String.valueOf(args[0]));
            }
        };
        TMethod[] ownedMethods = {toString};
        type = new TypeDef(desc, new TypeDesc[0], ownedMethods);
    }
}
