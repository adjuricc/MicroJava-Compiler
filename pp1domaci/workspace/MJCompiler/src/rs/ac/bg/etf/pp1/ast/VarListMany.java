// generated with ast extension for cup
// version 0.8
// 25/7/2024 22:27:15


package rs.ac.bg.etf.pp1.ast;

public class VarListMany extends VarList {

    private VarList VarList;
    private VarType VarType;

    public VarListMany (VarList VarList, VarType VarType) {
        this.VarList=VarList;
        if(VarList!=null) VarList.setParent(this);
        this.VarType=VarType;
        if(VarType!=null) VarType.setParent(this);
    }

    public VarList getVarList() {
        return VarList;
    }

    public void setVarList(VarList VarList) {
        this.VarList=VarList;
    }

    public VarType getVarType() {
        return VarType;
    }

    public void setVarType(VarType VarType) {
        this.VarType=VarType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarList!=null) VarList.accept(visitor);
        if(VarType!=null) VarType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarList!=null) VarList.traverseTopDown(visitor);
        if(VarType!=null) VarType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarList!=null) VarList.traverseBottomUp(visitor);
        if(VarType!=null) VarType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarListMany(\n");

        if(VarList!=null)
            buffer.append(VarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarType!=null)
            buffer.append(VarType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarListMany]");
        return buffer.toString();
    }
}
