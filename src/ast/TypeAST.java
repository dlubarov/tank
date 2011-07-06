package ast;

public class TypeAST {
    public final String name;
    public final String[] superTypes;
    public final FieldDef[] fields;
    public final MethodAST[] methods;

    public TypeAST(String name, String[] superTypes, FieldDef[] fields, MethodAST[] methods) {
        this.name = name;
        this.superTypes = superTypes;
        this.fields = fields;
        this.methods = methods;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class ").append(name);
        if (superTypes != null) {
            sb.append(" extends ").append(superTypes[0]);
            for (int i = 1; i < superTypes.length; ++i)
                sb.append(", ").append(superTypes[i]);
        }
        sb.append(" {");
        for (FieldDef f : fields)
            sb.append('\n').append(f);
        for (MethodAST m : methods)
            sb.append('\n').append(m);
        return sb.append("\n}").toString();
    }
}
