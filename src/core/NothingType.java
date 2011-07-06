package core;

public class NothingType extends TypeDef {
    public final static NothingType INST = new NothingType();
    
    private NothingType() {
        super(new TypeDesc("core", "Nothing"), new TypeDesc[0], new TMethod[0]);
    }

    public boolean strictSubtypeOf(TypeDef that) {
        return that != this;
    }
}
