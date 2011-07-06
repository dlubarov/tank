package rst;

import ast.TypeAST;
import core.TMethod;
import core.TypeDef;
import core.TypeDesc;

import java.util.NoSuchElementException;

public class TypeRST {
    public final Project proj;
    public final TypeDesc desc;
    public final TypeDesc[] supers;
    public final FieldRST[] fields;
    public final MethodRST[] methods;

    public TypeRST(Project proj, TypeDesc desc, TypeDesc[] supers, FieldRST[] fields, MethodRST[] methods) {
        this.proj = proj;
        this.desc = desc;
        this.supers = supers;
        this.fields = fields;
        this.methods = methods;
    }

    public TypeRST(Project proj, TypeDesc desc, TypeDesc[] supers, TypeAST ast, ImportContext ictx) {
        this.proj = proj;
        this.desc = desc;
        this.supers = supers;
        fields = new FieldRST[ast.fields.length];
        for (int i = 0; i < ast.fields.length; ++i)
            fields[i] = new FieldRST(ast.fields[i], ictx);
        methods = new MethodRST[ast.methods.length];
        for (int i = 0; i < ast.methods.length; ++i)
            methods[i] = new MethodRST(this, ast.methods[i], ictx);
    }

    public int fieldIndex(String name) {
        for (int i = 0; i < fields.length; ++i)
            if (fields[i].name.equals(name))
                return i;
        throw new NoSuchElementException();
    }

    public TypeDesc fieldType(String name) {
        for (int i = 0; i < fields.length; ++i)
            if (fields[i].name.equals(name))
                return fields[i].type;
        throw new NoSuchElementException();
    }

    private boolean strictSubtypeOf(TypeDesc type) {
        for (TypeDesc sup : supers)
            if (proj.allTypes.get(sup).subtypeOf(type))
                return true;
        return false;
    }

    public boolean subtypeOf(TypeDesc type) {
        return desc.equals(type) || strictSubtypeOf(type);
    }

    public TypeDef compile() {
        TMethod[] compiledMethods = new TMethod[methods.length];
        for (int i = 0; i < methods.length; ++i)
            compiledMethods[i] = methods[i].compile();
        return new TypeDef(desc, supers, compiledMethods);
    }
}
