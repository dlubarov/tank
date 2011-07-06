package comp;

import ast.*;
import ast.exp.*;
import ast.stm.*;
import peg.*;
import rst.stm.LocalDefStmRST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static comp.Grammar.*;

public abstract class RawToAST {
    public static final ExpressionAST parseLitInt(Success raw) {
        StringBuilder sb = new StringBuilder(raw.children.length);
        for (Success s : raw.children)
            sb.append(((Char) s.peg).val);
        return new LitIntAST(Integer.parseInt(sb.toString()));
    }

    public static final String parseIdentifier(Success raw) {
        StringBuilder sb = new StringBuilder();
        sb.append(((Char) raw.children[0].peg).val);
        for (Success c : raw.children[1].children)
            sb.append(((Char) c.peg).val);
        return sb.toString();
    }

    public static final ExpressionAST parseAtom(Success raw) {
        if (raw.peg == number)
            return parseLitInt(raw);
        else if (raw.peg == litTrue)
            return new LitBoolAST(true);
        else if (raw.peg == litFalse)
            return new LitBoolAST(false);
        assert raw.peg == identifier;
        return new VariableAccessAST(parseIdentifier(raw));
    }

    public static final ExpressionAST[] parseArgs(Success[] raw) {
        int n = raw.length;
        ExpressionAST[] res = new ExpressionAST[n];
        for (int i = 0; i < n; ++i)
            res[i] = parseExpression(raw[i]);
        return res;
    }

    public static final ExpressionAST parseChain(Success raw) {
        ExpressionAST res = parseAtom(raw.children[0]);
        Success[] rawSelectors = raw.children[1].children;
        for (Success rawSel : rawSelectors) {
            String ident = parseIdentifier(rawSel.children[0]);
            res = new FieldAccessAST(res, ident);
            if (rawSel.children[1].peg != Lambda.INST) {
                ExpressionAST[] args = parseArgs(rawSel.children[1].children);
                res = new InvocationAST(res, args);
            }
        }
        return res;
    }

    public static final ExpressionAST parseMultiplicativeExp(Success raw) {
        ExpressionAST res = parseChain(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i) {
            PEG rel = raw.children[i++].peg;
            ExpressionAST arg = parseChain(raw.children[i]);
            if (rel == star)
                res = new ProductAST(res, arg);
            else if (rel == slash)
                res = new QuotientAST(res, arg);
            else if (rel == perc)
                res = new ModAST(res, arg);
            else assert false;
        }
        return res;
    }

    public static final ExpressionAST parseAdditiveExp(Success raw) {
        ExpressionAST res = parseMultiplicativeExp(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i) {
            PEG rel = raw.children[i++].peg;
            ExpressionAST arg = parseMultiplicativeExp(raw.children[i]);
            if (rel == plus)
                res = new SumAST(res, arg);
            else if (rel == minus)
                res = new DifferenceAST(res, arg);
            else assert false;
        }
        return res;
    }

    public static final ExpressionAST parseRelationalExp(Success raw) {
        ExpressionAST res = parseAdditiveExp(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i) {
            PEG rel = raw.children[i++].peg;
            ExpressionAST e = parseAdditiveExp(raw.children[i]);
            if (rel == lt)
                res = new LessThanAST(res, e);
            else if (rel == gt)
                res = new GreaterThanAST(res, e);
            else if (rel == le)
                res = new LessOrEqualAST(res, e);
            else if (rel == ge)
                res = new GreaterOrEqualAST(res, e);
            else assert false;
        }
        return res;
    }

    public static final ExpressionAST parseEqualityExp(Success raw) {
        ExpressionAST res = parseRelationalExp(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i) {
            PEG rel = raw.children[i++].peg;
            ExpressionAST arg = parseRelationalExp(raw.children[i]);
            if (rel == equEqu)
                res = new EqualityAST(res, arg);
            else if (rel == notEqu)
                res = new InequalityAST(res, arg);
            else assert false;
        }
        return res;
    }

    public static final ExpressionAST parseConjunction(Success raw) {
        ExpressionAST res = parseEqualityExp(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i)
            res = new ConjunctionAST(res, parseEqualityExp(raw.children[i]));
        return res;
    }

    public static final ExpressionAST parseExclusiveOr(Success raw) {
        ExpressionAST res = parseConjunction(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i)
            res = new ExclusiveDisjunctionAST(res, parseConjunction(raw.children[i]));
        return res;
    }

    public static final ExpressionAST parseInclusiveDisjunction(Success raw) {
        ExpressionAST res = parseExclusiveOr(raw.children[0]);
        for (int i = 1; i < raw.children.length; ++i)
            res = new InclusiveDisjunctionAST(res, parseExclusiveOr(raw.children[i]));
        return res;
    }
    
    public static final ExpressionAST parseExpression(Success raw) {
        ExpressionAST left = parseInclusiveDisjunction(raw.children[0]);
        Success rightRaw = raw.children[1];
        if (rightRaw.peg == expression)
            return new AssignmentAST(left, parseExpression(rightRaw));
        return left;
    }

    public static final BlockStmAST parseBlockStm(Success raw) {
        int n = raw.children.length;
        StatementAST[] stms = new StatementAST[n];
        for (int i = 0; i < n; ++i)
            stms[i] = parseStatement(raw.children[i]);
        return new BlockStmAST(stms);
    }

    public static final StatementAST parseWhileStm(Success raw) {
        return new WhileStmAST(parseExpression(raw.children[1]), parseStatement(raw.children[2]));
    }

    public static final StatementAST parseForStm(Success raw) {
        StatementAST a = parseStatement(raw.children[2]);
        ExpressionAST b = parseExpression(raw.children[3]);
        StatementAST c = new ExpStmAST(parseExpression(raw.children[5]));
        StatementAST body = parseStatement(raw.children[7]);
        return new BlockStmAST(a, new WhileStmAST(b, new BlockStmAST(body, c)));
    }

    public static final StatementAST parseIfElseStm(Success raw) {
        return new IfElseStmAST(
                parseExpression(raw.children[1]),
                parseStatement(raw.children[2]),
                parseStatement(raw.children[4]));
    }

    public static final StatementAST parseIfStm(Success raw) {
        return new IfElseStmAST(
                parseExpression(raw.children[1]),
                parseStatement(raw.children[2]),
                EmptyStmAST.INST);
    }

    public static final ReturnStmAST parseReturnStm(Success raw) {
        raw = raw.children[1];
        if (raw.peg == Lambda.INST)
            return new ReturnStmAST(null);
        return new ReturnStmAST(parseExpression(raw));
    }

    public static final StatementAST parseNewLocal(Success raw) {
        TypedVariable field = parseTypedVar(raw.children[0]);
        ExpressionAST initVal = parseExpression(raw.children[2]);
        return new LocalDefStmAST(field, initVal);
    }

    public static final StatementAST parseStatement(Success raw) {
        if (raw.peg == emptyStatement)
            return EmptyStmAST.INST;
        if (raw.peg.name.equals("blockStm"))
            return parseBlockStm(raw);
        if (raw.peg == newLocal)
            return parseNewLocal(raw);
        if (raw.peg == returnStm)
            return parseReturnStm(raw);
        if (raw.peg == whileStm)
            return parseWhileStm(raw);
        if (raw.peg == forStm)
            return parseForStm(raw);
        if (raw.peg == ifElseStm)
            return parseIfElseStm(raw);
        if (raw.peg == ifStm)
            return parseIfStm(raw);
        assert raw.peg == expression;
        return new ExpStmAST(parseExpression(raw));
    }

    public static final TypedVariable parseTypedVar(Success raw) {
        String type = parseIdentifier(raw.children[0]);
        String name = parseIdentifier(raw.children[1]);
        return new TypedVariable(type, name);
    }

    public static final TypedVariable[] parseParamList(Success raw) {
        if (raw.children == null)
            return new TypedVariable[0];
        int n = raw.children.length;
        TypedVariable[] res = new TypedVariable[n];
        for (int i = 0; i < n; ++i)
            res[i] = parseTypedVar(raw.children[i]);
        return res;
    }

    private static final Set<String> parseQualifiers(Success raw) {
        Set<String> res = new HashSet<String>();
        for (Success q : raw.children)
            res.add(((Str) q.peg).s);
        return res;
    }

    public static final MethodAST parseMethodDef(Success raw) {
        Set<String> qualifiers = parseQualifiers(raw.children[0]);
        TypedVariable meth = parseTypedVar(raw.children[1]);
        TypedVariable[] params = parseParamList(raw.children[2]);
        BlockStmAST code = parseBlockStm(raw.children[3]);
        return new MethodAST(meth, params, code, qualifiers.contains("static"));
    }

    public static final FieldDef parseFieldDef(Success raw) {
        TypedVariable field = parseTypedVar(raw.children[0]);
        ExpressionAST initVal = parseExpression(raw.children[2]);
        return new FieldDef(field, initVal);
    }

    public static final TypeAST parseClassDef(Success raw) {
        String name = parseIdentifier(raw.children[1]);

        String[] supers;
        Success rawSupers = raw.children[2];
        if (rawSupers.peg == Lambda.INST)
            supers = null;
        else {
            rawSupers = rawSupers.children[1];
            int n = rawSupers.children.length;
            supers = new String[n];
            for (int i = 0; i < n; ++i)
                supers[i] = parseIdentifier(rawSupers.children[i]);
        }

        Success[] members = raw.children[3].children;
        List<FieldDef> fields = new ArrayList<FieldDef>();
        List<MethodAST> methods = new ArrayList<MethodAST>();
        for (Success mem : members)
            if (mem.peg == fieldDef)
                fields.add(parseFieldDef(mem));
            else {
                assert mem.peg == methodDef;
                methods.add(parseMethodDef(mem));
            }

        return new TypeAST(name, supers, fields.toArray(new FieldDef[0]), methods.toArray(new MethodAST[0]));
    }

    public static final TypeAST[] parseClassDefs(Success raw) {
        int n = raw.children.length;
        TypeAST[] res = new TypeAST[n];
        for (int i = 0; i < n; ++i)
            res[i] = parseClassDef(raw.children[i]);
        return res;
    }

    public static final ImportStm parseImportStm(Success raw) {
        String mod = parseIdentifier(raw.children[1]);
        Success clsRaw = raw.children[3];
        if (clsRaw.peg == star)
            return new ImportStm(mod, null);
        return new ImportStm(mod, parseIdentifier(clsRaw));
    }

    public static final ImportStm[] parseImportStms(Success raw) {
        int n = raw.children.length;
        ImportStm[] res = new ImportStm[n];
        for (int i = 0; i < n; ++i)
            res[i] = parseImportStm(raw.children[i]);
        return res;
    }

    public static final SourceFile parseSourceFile(Success raw) {
        String mod = parseIdentifier(raw.children[0]);
        ImportStm[] imps = parseImportStms(raw.children[1]);
        TypeAST[] defs = parseClassDefs(raw.children[2]);
        return new SourceFile(mod, imps, defs);
    }
}
