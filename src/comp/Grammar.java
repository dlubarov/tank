package comp;

import ast.SourceFile;
import core.TypeDef;
import peg.*;
import rst.Project;

import java.io.IOException;
import static peg.PEG.*;

// Based on http://java.sun.com/docs/books/jls/second_edition/html/syntax.doc.html
// and http://www.antlr.org/grammar/1152141644268/Java.g

public class Grammar {
    public static final Sequence expression;
    public static final Disjunction statement;
    public static final PEG alphaChar, numericChar, identifierStartChar, identifierPartChar,
            and, or, xor, lparen, rparen, lbrace, rbrace, comma, dot, semicolon, equals,
            plus, minus, tilde, exclamation, star, slash, perc, bslash, question, colon,
            plusPlus, minusMinus, equEqu, notEqu,
            lt, gt, le, ge, relationalOp,
            identifier,
            keyType,
            literal, number, litBool, litTrue, litFalse,
            atom, selector, arguments, chain,
            additiveExpression, multiplicativeExpression,
            equalityExpression, relationalExpression,
            inclusiveOrExpression, exclusiveOrExpression, andExpression,
            expStatement, blockStatement, emptyStatement, newLocal,
            returnStm, ifElseStm, ifStm, whileStm, forStm,
            typedVariable, paramList,
            qualifier, methodDef, fieldDef, memberDef, classDef,
            moduleStm, importStm, sourceFile;

    static {
        PEG[] alphaChars = new PEG[26 * 2];
        for (int i = 0; i < 26; ++i) {
            alphaChars[i] = new Char((char) ('a' + i));
            alphaChars[i + 26] = new Char((char) ('A' + i));
        }
        PEG[] numericChars = new PEG[10];
        for (int i = 0; i < 10; ++i)
            numericChars[i] = new Char((char) ('0' + i));
        PEG[] underscore = new PEG[] {new Char('_')};
        PEG[] identifierStartChars = merge(alphaChars, underscore);
        PEG[] identifierPartChars = merge(alphaChars, numericChars, underscore);
        
        alphaChar = new Disjunction("alphaChar", alphaChars);
        numericChar = new Disjunction("numericChar", numericChars);
        identifierStartChar = new Disjunction("idStart", identifierStartChars);
        identifierPartChar = new Disjunction("idPart", identifierPartChars);
        identifier = new Sequence("id", identifierStartChar, new Star(null, identifierPartChar));
        
        lparen = new Char('('); rparen = new Char(')');
        lbrace = new Char('{'); rbrace = new Char('}');
        comma = new Char(','); dot = new Char('.');
        semicolon = new Char(';'); equals = new Char('=');
        plus = new Char('+'); minus = new Char('-');
        star = new Char('*'); slash = new Char('/'); perc = new Char('%');
        and = new Char('&'); or = new Char('|'); xor = new Char('^');
        tilde = new Char('~'); bslash = new Char('\\');
        question = new Char('?'); exclamation = new Char('!');
        colon = new Char(':');
        plusPlus = new Str("++"); minusMinus = new Str("--");
        equEqu = new Str("=="); notEqu = new Str("!=");
        
        le = new Str("<="); ge = new Str(">=");
        lt = new Char('<'); gt = new Char('>');
        relationalOp = new Disjunction("relOp", le, ge, lt, gt);

        keyType = new Str("type");
        
        number = new Plus("number", numericChar);
        litTrue = new Str("true");
        litFalse = new Str("false");
        litBool = new Disjunction("litBool", litTrue, litFalse);
        literal = new Disjunction("literal", number, litBool);

        expression = new Sequence("expression");
        statement = new Disjunction("statement");

        atom = new Disjunction("atom", literal, identifier);
        arguments = new MaybeEmptyHiddenDelimList("args", expression.padWS(), comma).pad(lparen, rparen);
        selector = new SequenceWS("selector", identifier.padLeft(new Sequence(null, dot, anyWS)), arguments.opt());
        chain = new SequenceWS("chain", atom, new StarWS(null, selector));

        typedVariable = new SequenceWS("typedVar", identifier, identifier);

        multiplicativeExpression = new VisibleDelimList("mulExp", chain, new Disjunction(null, star, slash, perc).padWS()); // TODO: use unaryExpression
        additiveExpression = new VisibleDelimList("addExp", multiplicativeExpression, new Disjunction("plusOrMinus", plus, minus).padWS());
        relationalExpression = new VisibleDelimList("relExp", additiveExpression, relationalOp.padWS());
        equalityExpression = new VisibleDelimList("equExp", relationalExpression, new Disjunction("equOp", equEqu, notEqu).padWS());
        andExpression = new HiddenDelimList("andExp", equalityExpression, and.padWS());
        exclusiveOrExpression = new HiddenDelimList("xorExp", andExpression, xor.padWS());
        inclusiveOrExpression = new HiddenDelimList("iorExp", exclusiveOrExpression, or.padWS());
        expression.parts = new PEG[] {inclusiveOrExpression, expression.padLeft(equals.padWS()).opt()};

        emptyStatement = semicolon;
        blockStatement = new StarWS("blockStm", statement).padWS().pad(lbrace, rbrace);
        expStatement = expression.padRight(new Sequence(null, anyWS, semicolon));
        newLocal = new SequenceWS("newLocal", typedVariable, equals, expression, semicolon);
        returnStm = new SequenceWS("returnStm", new Str("return"), expression.opt(), semicolon);
        ifElseStm = new SequenceWS("ifElseStm", new Str("if"), expression.padWS().pad(lparen, rparen), statement, new Str("else"), statement);
        ifStm = new SequenceWS("ifStm", new Str("if"), expression.padWS().pad(lparen, rparen), statement);
        whileStm = new SequenceWS("whileStm", new Str("while"), expression.padWS().pad(lparen, rparen), statement);
        forStm = new SequenceWS("forStm", new Str("for"), lparen, statement, expression, semicolon, expression, rparen, statement);
        statement.options = new PEG[] {emptyStatement, blockStatement, newLocal, returnStm, whileStm, forStm, ifElseStm, ifStm, expStatement};

        qualifier = new Disjunction("qualifier", new Str("static"));
        fieldDef = new SequenceWS("fieldDef", typedVariable, equals, expression, semicolon);
        paramList = new HiddenDelimList("paramList", typedVariable, comma.padWS()).opt().padWS().pad(lparen, rparen);
        methodDef = new SequenceWS("methodDef", new StarWS(null, qualifier), typedVariable, paramList, blockStatement);

        memberDef = new Disjunction("memberDef", fieldDef, methodDef);
        classDef = new SequenceWS("typeDef",
                new Str("type").padRight(someWS), identifier,
                new Sequence(null, new Str("extends").padWS(), new HiddenDelimList(null, identifier, comma.padWS())).opt(),
                new StarWS(null, memberDef).padWS().pad(lbrace, rbrace));

        moduleStm = identifier.pad(someWS, anyWS).pad(new Str("module"), semicolon);
        importStm = new Sequence("importStm", new Str("import").padRight(someWS), identifier, dot.padWS(), new Disjunction(null, identifier, star), semicolon);
        sourceFile = new SequenceWS("sourceFile", moduleStm, new StarWS(null, importStm), new StarWS(null, classDef)).padWS();
    }

    private static PEG[] merge(PEG[]... arrays) {
        int n = 0;
        for (PEG[] arr : arrays)
            n += arr.length;
        PEG[] result = new PEG[n];
        n = 0;
        for (PEG[] arr : arrays) {
            System.arraycopy(arr, 0, result, n, arr.length);
            n += arr.length;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        PEG peg = sourceFile;
        LazyList in = new LazyList(System.in);
        Success raw = (Success) peg.parse(in);
        SourceFile ast = RawToAST.parseSourceFile(raw);
        System.out.println(ast + "\n");
        Project rst = new Project(new SourceFile[] {ast});
        TypeDef[] allTypes = rst.compile();
        
        for (TypeDef type : allTypes)
            type.link();
        for (TypeDef type : allTypes)
            type.runMain(args);
    }
}
