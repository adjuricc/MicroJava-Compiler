// generated with ast extension for cup
// version 0.8
// 25/7/2024 22:27:15


package rs.ac.bg.etf.pp1.ast;

public class VarType implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String name;
    private VarArrayOpt VarArrayOpt;

    public VarType (String name, VarArrayOpt VarArrayOpt) {
        this.name=name;
        this.VarArrayOpt=VarArrayOpt;
        if(VarArrayOpt!=null) VarArrayOpt.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public VarArrayOpt getVarArrayOpt() {
        return VarArrayOpt;
    }

    public void setVarArrayOpt(VarArrayOpt VarArrayOpt) {
        this.VarArrayOpt=VarArrayOpt;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarArrayOpt!=null) VarArrayOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarArrayOpt!=null) VarArrayOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarArrayOpt!=null) VarArrayOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarType(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(VarArrayOpt!=null)
            buffer.append(VarArrayOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarType]");
        return buffer.toString();
    }
}
