package prim;

import core.TMethod;
import core.TObject;
import core.TypeDef;
import core.TypeDesc;

public class PrimitiveString extends TObject {
    public final String value;
    public static final TypeDef type;

    static {
        TypeDesc desc = new TypeDesc("core", "String");
        TypeDesc[] superTypeDescs = {new TypeDesc("core", "Object")};
        TMethod[] ownedMethods = {};
        type = new TypeDef(desc, superTypeDescs, ownedMethods);
    }

    public PrimitiveString(String value) {
        super(type);
        this.value = value;
    }
}
