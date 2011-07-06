package rst;

import core.TypeDef;

public class PrimitiveTypeRST extends TypeRST {
    private final TypeDef def;
    
    public PrimitiveTypeRST(Project proj, TypeDef def) {
        super(proj, def.desc, def.superTypeDescs, new FieldRST[0], def.getMethodRSTs());
        this.def = def;
    }

    @Override
    public TypeDef compile() {
        return def;
    }
}
