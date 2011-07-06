package rst;

import ast.FieldDef;
import core.TypeDesc;

public class FieldRST {
    public final String name;
    public final TypeDesc type;
    //public final ExpressionQST initVal;

    public FieldRST(FieldDef ast, ImportContext ictx) {
        name = ast.field.name;
        type = ictx.resolve(ast.field.type);
    }
}
