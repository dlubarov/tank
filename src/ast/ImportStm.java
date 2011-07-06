package ast;

public class ImportStm {
    public final String module, className;

    public ImportStm(String module, String className) {
        this.module = module;
        this.className = className;
    }

    public String toString() {
        return String.format("import %s.%s;", module,
                className == null? "*" : className);
    }
}
