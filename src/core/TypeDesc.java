package core;

public class TypeDesc {
    public final String module;
    public final String name;

    public TypeDesc(String module, String name) {
        this.module = module;
        this.name = name;
    }

    public boolean equals(Object o) {
        try {
            TypeDesc td = (TypeDesc) o;
            return module.equals(td.module) && name.equals(td.name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return module.hashCode() ^ name.hashCode();
    }

    public String toString() {
        return String.format("%s.%s", module, name);
    }
}
