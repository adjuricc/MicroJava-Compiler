// generated with ast extension for cup
// version 0.8
// 25/7/2024 22:27:15


package rs.ac.bg.etf.pp1.ast;

public class Const extends Factor {

    private Integer I1;

    public Const (Integer I1) {
        this.I1=I1;
    }

    public Integer getI1() {
        return I1;
    }

    public void setI1(Integer I1) {
        this.I1=I1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Const(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Const]");
        return buffer.toString();
    }
}
