package rst;

import ast.*;
import core.*;

import java.util.*;

public class Project {
    public final Map<String, Set<String>> typeNames;
    public final Map<TypeDesc, TypeRST> allTypes;
    
    public Project(SourceFile[] sources) {
        typeNames = new HashMap<String, Set<String>>();
        Set<String> coreTypes = new HashSet<String>();
        coreTypes.addAll(Arrays.asList("Object", "Int", "Bool", "String", "Console", "Nothing"));
        typeNames.put("core", coreTypes);
        for (SourceFile src : sources)
            for (TypeAST typeAST : src.typeASTs) {
                if (!typeNames.containsKey(src.module))
                    typeNames.put(src.module, new HashSet<String>());
                if (!typeNames.get(src.module).add(typeAST.name))
                    throw new RuntimeException(String.format("Two types named %s in module %s",
                            typeAST.name, src.module));
            }
        
        allTypes = new HashMap<TypeDesc, TypeRST>();
        for (TypeDef primType : God.nativeTypes)
            allTypes.put(primType.desc, new PrimitiveTypeRST(this, primType));
        for (SourceFile src : sources) {
            ImportContext ictx = new ImportContext(this, src);
            for (TypeAST typeAST : src.typeASTs) {
                TypeDesc desc = new TypeDesc(src.module, typeAST.name);
                TypeDesc[] supers;
                if (typeAST.superTypes == null)
                    supers = new TypeDesc[] {new TypeDesc("core", "Object")};
                else {
                    supers = new TypeDesc[typeAST.superTypes.length];
                    for (int i = 0; i < supers.length; ++i)
                        supers[i] = ictx.resolve(typeAST.superTypes[i]);
                }
                TypeRST type = new TypeRST(this, desc, supers, typeAST, ictx);
                if (allTypes.put(desc, type) != null)
                    throw new RuntimeException("Multiple definitions of type " + desc);
            }
        }
    }

    public MethodDesc getMatchingMethod(TypeDesc targetDesc, String methodName, TypeDesc[] argTypes) {
        List<MethodDesc> options = new ArrayList<MethodDesc>();
        TypeRST targetType = allTypes.get(targetDesc);
        for (MethodRST meth : targetType.methods)
            if (meth.desc.name.equals(methodName)) {
                boolean match = true;
                for (int i = 0; i < argTypes.length; ++i) {
                    TypeRST argRST = allTypes.get(argTypes[i]);
                    TypeDesc paramDesc = meth.desc.paramTypes[i];
                    match &= argRST.subtypeOf(paramDesc);
                }
                if (match)
                    options.add(meth.desc);
            }

        if (options.isEmpty())
            for (TypeDesc targetSup : targetType.supers)
                try {
                    options.add(getMatchingMethod(targetSup, methodName, argTypes));
                } catch (NoSuchElementException e) {}

        if (options.isEmpty())
            throw new NoSuchElementException(String.format("No such method: %s.%s%s",
                    targetDesc, methodName, Arrays.toString(argTypes)));
        if (options.size() > 1)
            throw new RuntimeException(String.format("Ambiguous method call: %s.%s%s; options are %s",
                    targetDesc, methodName, Arrays.toString(argTypes), options));
        return options.get(0);
    }

    public MethodDesc getMatchingClassMethod(TypeDesc owner, String methodName, TypeDesc[] argTypes) {
        List<MethodDesc> options = new ArrayList<MethodDesc>();
        TypeRST ownerType = allTypes.get(owner);
        for (MethodRST meth : ownerType.methods)
            if (meth.desc.name.equals(methodName)) {
                boolean match = true;
                for (int i = 0; i < argTypes.length; ++i) {
                    TypeRST argRST = allTypes.get(argTypes[i]);
                    TypeDesc paramDesc = meth.desc.paramTypes[i];
                    match &= argRST.subtypeOf(paramDesc);
                }
                if (match)
                    options.add(meth.desc);
            }
        
        if (options.isEmpty())
            throw new NoSuchElementException(String.format("No such method: %s.%s%s",
                    owner, methodName, Arrays.toString(argTypes)));
        if (options.size() > 1)
            throw new RuntimeException(String.format("Ambiguous method call: %s.%s%s; options are %s",
                    owner, methodName, Arrays.toString(argTypes), options));
        return options.get(0);
    }

    public TypeDef[] compile() {
        TypeDef[] result = new TypeDef[allTypes.size()];
        int i = 0;
        for (TypeRST type : allTypes.values())
            result[i++] = type.compile();
        return result;
    }
}
