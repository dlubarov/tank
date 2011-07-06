package ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SourceFile {
    public final String module;
    public final Set<String> wildcardImports;
    public final Map<String, Set<String>> specificImports;
    public final TypeAST[] typeASTs;

    public SourceFile(String module, ImportStm[] importStms, TypeAST[] typeASTs) {
        this.module = module;
        wildcardImports = new HashSet<String>();
        specificImports = new HashMap<String, Set<String>>();
        for (ImportStm imp : importStms)
            if (imp.className == null)
                wildcardImports.add(imp.module);
            else {
                if (!specificImports.containsKey(imp.module))
                    specificImports.put(imp.module, new HashSet<String>());
                specificImports.get(imp.module).add(imp.className);
            }
        this.typeASTs = typeASTs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("module ").append(module).append(";");

        if (!wildcardImports.isEmpty())
            sb.append('\n');
        for (String mod : wildcardImports)
            sb.append(String.format("\nimport %s.*;", mod));

        if (!specificImports.isEmpty())
            sb.append('\n');
        for (String mod : specificImports.keySet())
            for (String cls : specificImports.get(mod))
                sb.append(String.format("\nimport %s.%s;", mod, cls));

        for (TypeAST cls : typeASTs)
            sb.append("\n\n").append(cls);
        
        return sb.toString();
    }
}
