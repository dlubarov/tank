package core;

public class NormalObject extends TObject {
    public final TObject[] members;

    public NormalObject(TypeDef type, int memberCount) {
        super(type);
        members = new TObject[memberCount];
    }
}
