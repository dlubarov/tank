package core;

import prim.PrimitiveBool;
import prim.PrimitiveInt;

import java.util.*;

public class NormalMethod extends TMethod implements Opcodes {
    private final int numLocals;
    private final int[] code;
    
    public NormalMethod(MethodDesc desc, boolean isStatic, int numLocals, MethodDesc[] methodDescPool, int[] code) {
        super(desc, methodDescPool, isStatic);
        this.numLocals = numLocals;
        this.code = code;
    }

    public boolean isAbstract() {
        return code == null;
    }

    public TObject invoke(TObject[] args) {
        TObject[] locals = new TObject[numLocals];
        System.arraycopy(args, 0, locals, 0, args.length);
        Deque<TObject> stack = new ArrayDeque<TObject>();
        int ip = 0, i;
        for (;;) {
            int op = code[ip++];
            TObject a, b;
            TMethod m;
            TObject[] newArgs;

            switch (op) {
                case DUP:
                    a = stack.pop();
                    stack.push(a);
                    stack.push(a);
                    break;

                case POP:
                    stack.pop();
                    break;

                case CONST_INT:
                    stack.push(new PrimitiveInt(code[ip++]));
                    break;

                case CONST_TRUE:
                    stack.push(PrimitiveBool.TRUE);
                    break;

                case CONST_FALSE:
                    stack.push(PrimitiveBool.FALSE);
                    break;

                case GET_LOCAL:
                    stack.push(locals[code[ip++]]);
                    break;

                case PUT_LOCAL:
                    locals[code[ip++]] = stack.pop();
                    break;

                case JUMP:
                    i = code[ip++];
                    ip += i;
                    break;

                case JUMP_COND:
                    i = code[ip++];
                    if (((PrimitiveBool) stack.pop()).value)
                        ip += i;
                    break;

                case INVOKE_STATIC:
                    m = methodPool[code[ip++]];
                    newArgs = new TObject[m.numParams];
                    for (i = newArgs.length - 1; i >= 0; --i)
                        newArgs[i] = stack.pop();
                    a = m.invoke(newArgs);
                    if (a != null)
                        stack.push(a);
                    break;

                case INVOKE_VIRTUAL:
                    m = methodPool[code[ip++]];
                    newArgs = new TObject[m.numParams];
                    for (i = newArgs.length - 1; i >= 0; --i)
                        newArgs[i] = stack.pop();
                    TObject o = newArgs[0];
                    m = o.type.vtable.get(m);
                    a = m.invoke(newArgs);
                    if (a != null)
                        stack.push(a);
                    break;

                case RETURN_OBJECT:
                    return stack.pop();

                case RETURN_VOID:
                    return null;

                case BOOL_NEG:
                    stack.push(((PrimitiveBool) stack.pop()).value ? PrimitiveBool.FALSE : PrimitiveBool.TRUE);
                    break;

                case PRINT_HEY:
                    System.out.println("hey");
                    break;

                case PRINT_SUP:
                    System.out.println("sup");
                    break;

                default:
                    for (int insn : code)
                        System.out.println(OpcodeNames.get(insn));
                    throw new RuntimeException("bad opcode: " + op);
            }
        }
    }
}
