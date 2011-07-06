package core;

import java.util.Arrays;

public final class MethodDesc {
    // FIXME: get rid of paramNames, retType
    public final TypeDesc owner;
    public final String name;
    public final TypeDesc[] paramTypes;
    public final String[] paramNames;
    public final TypeDesc retType;

    public MethodDesc(TypeDesc owner, String name, TypeDesc[] paramTypes, String[] paramNames, TypeDesc retType) {
        this.owner = owner;
        this.name = name;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.retType = retType;
    }

    public boolean equals(Object o) {
        try {
            MethodDesc md = (MethodDesc) o;
            return owner.equals(md.owner) && name.equals(md.name) && Arrays.equals(paramTypes, md.paramTypes);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return owner.hashCode() ^ name.hashCode() ^ Arrays.hashCode(paramTypes);
    }

    public String toString() {
        return String.format("%s.%s%s", owner, name, Arrays.toString(paramTypes));
    }
}
