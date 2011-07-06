package core;

public abstract class NativeMethod extends TMethod {
    public NativeMethod(TypeDesc owner, String name, TypeDesc[] paramTypeDescs, String[] paramNames,
                        TypeDesc retType, MethodDesc[] methodDescsPool, boolean isStatic) {
        super(owner, name, paramTypeDescs, paramNames, retType, methodDescsPool, isStatic);
    }

    public boolean isAbstract() {
        return false;
    }
}
