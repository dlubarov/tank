package ast;

public class TypedVariable {
    public final String type, name;
    
    public TypedVariable(String type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
