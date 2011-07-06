package core;

public class OpcodeNames implements Opcodes {
    private static final String[] names;

    static {
        names = new String[256];
        names[DUP] = "DUP";
        names[POP] = "POP";
        names[CONST_INT] = "CONST_INT";
        names[CONST_TRUE] = "CONST_TRUE";
        names[CONST_FALSE] = "CONST_FALSE";
        names[GET_LOCAL] = "GET_LOCAL";
        names[PUT_LOCAL] = "PUT_LOCAL";
        names[GET_FIELD] = "GET_FIELD";
        names[PUT_FIELD] = "PUT_FIELD";
        names[JUMP] = "JUMP";
        names[JUMP_COND] = "JUMP_COND";
        names[INVOKE_STATIC] = "INVOKE_STATIC";
        names[INVOKE_VIRTUAL] = "INVOKE_VIRTUAL";
        names[RETURN_VOID] = "RETURN_VOID";
        names[RETURN_OBJECT] = "RETURN_OBJECT";
        names[BOOL_NEG] = "BOOL_NEG";
    }

    public static final String get(int op) {
        try {
            String name = names[op];
            if (name == null)
                return Integer.toString(op);
            return op + " " + name;
        } catch (ArrayIndexOutOfBoundsException e) {
            return Integer.toString(op);
        }
    }

    public static void printAll(int[] code) {
        for (int i = 0; i < code.length; ++i)
            System.out.printf("%d %s\n", i, OpcodeNames.get(code[i]));
    }
}
