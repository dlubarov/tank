package ast;

import ast.exp.ExpressionAST;

public class FieldDef {
    public final TypedVariable field;
    public final ExpressionAST initVal;

    public FieldDef(TypedVariable field, ExpressionAST initVal) {
        this.field = field;
        this.initVal = initVal;
    }
    
    public String toString() {
        return String.format("%s = %s;", field, initVal);
    }
}
