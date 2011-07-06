package examples;

import core.*;
import prim.PrimitiveInt;
import prim.PrimitiveObject;
import prim.PrimitiveConsole;

public class BasicMath implements Opcodes {
    public static void main(String[] args) {
        God.init();
        TypeDesc desc = new TypeDesc("daniel", "BasicMath");
        MethodDesc[] methodDescPool = {
            new MethodDesc(PrimitiveConsole.type.desc, "println",
                    new TypeDesc[] {PrimitiveObject.type.desc},
                    new String[] {"obj"}, null),
            new MethodDesc(PrimitiveInt.type.desc, "+",
                    new TypeDesc[] {PrimitiveInt.type.desc, PrimitiveInt.type.desc},
                    new String[] {"a", "b"}, PrimitiveInt.type.desc)
        };
        int[] code = {
                CONST_INT, 17, PUT_LOCAL, 0,
                CONST_INT, 25, PUT_LOCAL, 1,

                GET_LOCAL, 0, POP,
                GET_LOCAL, 1, POP,
                CONST_INT, 55, DUP, POP, POP,

                CONST_FALSE,
                BOOL_NEG,
                JUMP_COND, 1,
                PRINT_HEY,
                PRINT_SUP,

                GET_LOCAL, 0, GET_LOCAL, 1,
                INVOKE_VIRTUAL, 1, INVOKE_STATIC, 0,
                RETURN_VOID
        };
        MethodDesc mainDesc = new MethodDesc(desc, "main", new TypeDesc[0], new String[0], null);
        TMethod m = new NormalMethod(mainDesc, true, 2, methodDescPool, code);
        TypeDef type = new TypeDef(desc, new TypeDesc[] {PrimitiveObject.type.desc}, new TMethod[] {m});
        type.link(); type.runMain(args);
    }
}
