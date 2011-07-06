package rst;

import ast.*;
import core.TypeDesc;

import java.util.*;

public class ImportContext {
    private final Project proj;
    private final Set<String> wildcardImports;
    private final Map<String, Set<String>> specificImports;
    
    public ImportContext(Project proj, SourceFile src) {
        this.proj = proj;
        wildcardImports = new HashSet<String>(src.wildcardImports);
        specificImports = new HashMap<String, Set<String>>(src.specificImports);
        wildcardImports.add(src.module);
        wildcardImports.add("core");
    }

    public TypeDesc resolve(String typeName) {
        List<TypeDesc> options = new ArrayList<TypeDesc>();
        for (String mod : specificImports.keySet())
            if (specificImports.get(mod).contains(typeName))
                options.add(new TypeDesc(mod, typeName));
        if (options.isEmpty())
            for (String mod : wildcardImports)
                if (proj.typeNames.get(mod).contains(typeName))
                    options.add(new TypeDesc(mod, typeName));
        if (options.isEmpty())
            throw new NoSuchElementException(String.format("No type found with name %s. Did you miss an import?", typeName));
        if (options.size() > 1)
            throw new NoSuchElementException(String.format("Ambiguous type name %s; options are %s", typeName, options));
        return options.get(0);
    }

    public boolean typeExists(String typeName) {
        try {
            resolve(typeName);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
