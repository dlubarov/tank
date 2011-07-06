package core;

public interface Opcodes {
    public static final int
            DUP = 0,
            POP = 1,
            CONST_INT = 2,
            CONST_TRUE = 3,
            CONST_FALSE = 4,
            GET_LOCAL = 5,
            PUT_LOCAL = 6,
            GET_FIELD = 13,
            PUT_FIELD = 14,
            JUMP = 7,
            JUMP_COND = 8,
            INVOKE_STATIC = 9,
            INVOKE_VIRTUAL = 10,
            RETURN_VOID = 11,
            RETURN_OBJECT = 12,
            BOOL_NEG = 15,
            PRINT_HEY = 100,
            PRINT_SUP = 101;
}
