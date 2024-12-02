// generated with ast extension for cup
// version 0.8
// 25/7/2024 22:27:15


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Factor Factor);
    public void visit(Mulop Mulop);
    public void visit(VarList VarList);
    public void visit(DesignatorPart DesignatorPart);
    public void visit(Expr Expr);
    public void visit(VarDeclList VarDeclList);
    public void visit(VarArrayOpt VarArrayOpt);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(Addop Addop);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(Statement Statement);
    public void visit(Relop Relop);
    public void visit(ConstVal ConstVal);
    public void visit(ConstList ConstList);
    public void visit(Term Term);
    public void visit(StatementList StatementList);
    public void visit(MulopMod MulopMod);
    public void visit(MulopDiv MulopDiv);
    public void visit(MulopMul MulopMul);
    public void visit(AddopMinus AddopMinus);
    public void visit(AddopPlus AddopPlus);
    public void visit(RelopLEqual RelopLEqual);
    public void visit(RelopLess RelopLess);
    public void visit(RelopGEqual RelopGEqual);
    public void visit(RelopGreater RelopGreater);
    public void visit(RelopNEqual RelopNEqual);
    public void visit(RelopEqual RelopEqual);
    public void visit(Assignop Assignop);
    public void visit(DesignatorNoPart DesignatorNoPart);
    public void visit(DesignatorMatrix DesignatorMatrix);
    public void visit(DesignatorArray DesignatorArray);
    public void visit(Designator Designator);
    public void visit(NewFactorMatrix NewFactorMatrix);
    public void visit(NewExpr NewExpr);
    public void visit(FactorParen FactorParen);
    public void visit(Var Var);
    public void visit(FactorChar FactorChar);
    public void visit(FactorBool FactorBool);
    public void visit(Const Const);
    public void visit(MulopTerm MulopTerm);
    public void visit(CommonTerm CommonTerm);
    public void visit(NegTermExpr NegTermExpr);
    public void visit(TermExpr TermExpr);
    public void visit(AddExpr AddExpr);
    public void visit(ReturnNoExpr ReturnNoExpr);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ReadStmt ReadStmt);
    public void visit(PrintStmt PrintStmt);
    public void visit(ErrorStmt ErrorStmt);
    public void visit(MinusMinusStmt MinusMinusStmt);
    public void visit(PlusPlusStmt PlusPlusStmt);
    public void visit(Assignment Assignment);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(MainMethod MainMethod);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(Type Type);
    public void visit(VarNoArray VarNoArray);
    public void visit(VarMatrix VarMatrix);
    public void visit(VarArray VarArray);
    public void visit(VarType VarType);
    public void visit(VarListOne VarListOne);
    public void visit(VarListMany VarListMany);
    public void visit(VarDecl VarDecl);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(ConstBool ConstBool);
    public void visit(ConstChar ConstChar);
    public void visit(ConstNumber ConstNumber);
    public void visit(ConstDef ConstDef);
    public void visit(ConstListOne ConstListOne);
    public void visit(ConstListMany ConstListMany);
    public void visit(ConstDecl ConstDecl);
    public void visit(NoConstDecl NoConstDecl);
    public void visit(ConstDeclarations ConstDeclarations);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
