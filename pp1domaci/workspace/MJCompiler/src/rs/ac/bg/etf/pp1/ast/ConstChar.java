// generated with ast extension for cup
// version 0.8
// 25/7/2024 22:27:15


package rs.ac.bg.etf.pp1.ast;

public class ConstChar extends ConstVal {

    private String C1;

    public ConstChar (String C1) {
        this.C1=C1;
    }

    public String getC1() {
        return C1;
    }

    public void setC1(String C1) {
        this.C1=C1;
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
        buffer.append("ConstChar(\n");

        buffer.append(" "+tab+C1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstChar]");
        return buffer.toString();
    }
}
