package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;
	private int jmpPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(PrintStmt visitor) {
		
		if(visitor.getExpr().struct.getKind() == Struct.Array) {
			if((visitor.getExpr().struct.getElemType() == MyTab.intType || visitor.getExpr().struct.getElemType() == MyTab.boolType)) {
				Code.loadConst(5);
				Code.put(Code.print);
			}
			else {
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
		}
		else if((visitor.getExpr().struct.getKind() != Struct.Array && (visitor.getExpr().struct == MyTab.intType || visitor.getExpr().struct == MyTab.boolType))) {
			Code.loadConst(5);
			Code.put(Code.print);
		}
		else if(visitor.getExpr().struct.getKind() != Struct.Array) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ReadStmt visitor) {
		if(visitor.getDesignator().obj.getType().getKind() == Struct.Array) {
			if((visitor.getDesignator().obj.getType().getElemType() == MyTab.intType  || visitor.getDesignator().obj.getType().getElemType() == MyTab.boolType)) {
	
				Code.put(Code.read);
				Code.put(Code.astore);
			}
			else {
				Code.put(Code.bread);
				Code.put(Code.bastore);
			}
		}
		else if(visitor.getDesignator().obj.getType().getKind() != Struct.Array && (visitor.getDesignator().obj.getType().getKind() == Struct.Int || visitor.getDesignator().obj.getType().getKind() == Struct.Bool)) {
			
			Code.put(Code.read);
		    Code.store(visitor.getDesignator().obj);
		}
		else if(visitor.getDesignator().obj.getType().getKind() != Struct.Array && visitor.getDesignator().obj.getType().getKind() == Struct.Char) {
			Code.put(Code.bread);
	        Code.store(visitor.getDesignator().obj);
		}
		
	}
	
	public void visit(MainMethod visitor) {
		mainPc = Code.pc;
		visitor.obj.setAdr(Code.pc);
		
		SyntaxNode methodNode = visitor.getParent();
		
		VarCounter var_cnt = new VarCounter();
		methodNode.traverseTopDown(var_cnt);
		
		// Generate entry
		Code.put(Code.enter);
		Code.put(0);
		Code.put(visitor.obj.getLocalSymbols().size()); // var_cnt.getCount()
		
		// exit u method decl
	}
	
	public void visit(MethodDecl visitor) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(Const visitor) {
//		System.out.println(visitor.getParent().getParent().getParent().getClass());
		Obj con = Tab.insert(Obj.Con, "$", visitor.struct);
		con.setLevel(0);
        con.setAdr(visitor.getI1());
        Code.load(con);
	}
	
	public void visit(FactorChar visitor) {
//		System.out.println("uslo u char " + visitor.getC1().charAt(1));
		Obj con = Tab.insert(Obj.Con, "$", visitor.struct);
		con.setLevel(0);
        con.setAdr(visitor.getC1().charAt(1));
        Code.load(con);
	}
	
	public void visit(NewExpr visitor) {
		Code.put(Code.newarray);
		if(visitor.getType().struct == MyTab.intType || visitor.getType().struct == MyTab.boolType)
			Code.put(1);
		else
			Code.put(0);
	}
	
	private void dup_3() {
		Code.put(Code.dup_x2);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		inv_dupx2();
		
		invert_3();
	}
	
	private void invert_3() {
		Code.put(Code.dup_x2);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}
	
	private void inv_dupx2() {
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		
		Code.put(Code.dup_x2);
	}
	
	public void visit(NewFactorMatrix visitor) {
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		Code.put(Code.newarray);
		Code.put(1);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		
		jmpPc = Code.pc;
		// pocetak iteracije?
		
		inv_dupx2();
		
		Code.put(Code.newarray);
		if(visitor.getType().struct == MyTab.intType || visitor.getType().struct == MyTab.boolType)
			Code.put(1);
		else
			Code.put(0);
		
		// dup3
		dup_3();
		
		Code.put(Code.astore);
		invert_3();
//		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		
		Code.put(Code.const_1);
		Code.put(Code.sub);
		
		Code.put(Code.dup);
		Code.loadConst(-1);
		Code.put(Code.jcc + Code.ne);
	
		int s = -(Code.pc - jmpPc - 1);
		Code.put2(s);
		
		// ??
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}
	
	public void visit(FactorBool visitor) {
		Obj con = Tab.insert(Obj.Con, "$", visitor.struct);
		con.setLevel(0);
		int flag = 1;
		if(visitor.getB1().equals("false"))
			flag = 0;
        con.setAdr(flag);
        Code.load(con);
	}
	
	public void visit(Assignment visitor) {
		// treba da napravimo razliku izmedju assignment za niz i obicnog
		// if za nizs
		Designator designator = visitor.getDesignator();
		
		if(designator.getDesignatorPart() instanceof DesignatorArray) {
			if (designator.obj.getType().getElemType().getKind() == Struct.Char)
				Code.put(Code.bastore);
			else {
		      	Code.put(Code.astore);
		     }
		}
		else if(designator.getDesignatorPart() instanceof DesignatorMatrix) {
			
			if (designator.obj.getType().getElemType().getKind() == Struct.Char)
				Code.put(Code.bastore);
			else {
		      	Code.put(Code.astore);
		     }
		}
		else {
//			System.out.println(designator.getName());
			Code.store(designator.obj);
		}
		
	}
	
	public void visit(PlusPlusStmt visitor) {
		Designator designator = visitor.getDesignator();
		
		if(designator.getDesignatorPart() instanceof DesignatorMatrix) {
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.load(designator.obj);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.aload);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.put(Code.astore);
		}
		
		else if(designator.obj.getType().getKind() == Struct.Array) {
			Code.load(designator.obj);
			Code.put(Code.dup_x1);
			Code.put(Code.dup2);
			Code.put(Code.pop);
			Code.put(Code.aload);
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.put(Code.astore);
		}
		else {
			Code.load(designator.obj);
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(designator.obj);
		}
		
		
	}
	
	public void visit(MinusMinusStmt visitor) {
		Designator designator = visitor.getDesignator();
		
		if(designator.getDesignatorPart() instanceof DesignatorMatrix) {
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.load(designator.obj);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.aload);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.put(Code.astore);
		}
		else if(designator.obj.getType().getKind() == Struct.Array) {
			Code.load(designator.obj);
			Code.put(Code.dup_x1);
			Code.put(Code.dup2);
			Code.put(Code.pop);
			Code.put(Code.aload);
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.put(Code.astore);
		}
		else {
			Code.load(designator.obj);
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(designator.obj);
		}
		
		
	}
	
	public void visit(Designator visitor) {
		SyntaxNode parent = visitor.getParent();
		
		// provera ako je niz[ind] lvalue
		DesignatorPart designator_part = visitor.getDesignatorPart();
		
		if(designator_part instanceof DesignatorArray) {
//			System.out.println(parent.getClass());
			if(Assignment.class == parent.getClass()) {
//				System.out.println(visitor.getName());
//				System.out.println(visitor.obj);
				Code.load(visitor.obj);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
			}
			else if(Var.class == parent.getClass()) {
				Code.load(visitor.obj);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				
				if(visitor.obj.getType().getElemType() == MyTab.intType || visitor.obj.getType().getElemType() == MyTab.boolType){
					Code.put(Code.aload);
				}
				else {
					Code.put(Code.baload);
				}
			}
			else if(ReadStmt.class == parent.getClass()) {
				Code.load(visitor.obj);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
			}
		}
		
		else if(designator_part instanceof DesignatorMatrix) {
//			System.out.println("matrix");
//			System.out.println(parent.getClass());
			if(Assignment.class == parent.getClass()) {
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
//				System.out.println(visitor.getName());
//				System.out.println(visitor);
//				Code.put(Code.const_2);
				Code.load(visitor.obj); // zasto ne stavlja adr mat ??
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				Code.put(Code.aload);
//				Code.put(Code.pop);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
			}
			else if(Var.class == parent.getClass()) {
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
//				System.out.println(visitor.getName());
//				System.out.println(visitor.obj);
				Code.load(visitor.obj);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				Code.put(Code.aload);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				if(visitor.obj.getType().getElemType() == MyTab.intType || visitor.obj.getType().getElemType() == MyTab.boolType){
					Code.put(Code.aload);
				}
				else {
					Code.put(Code.baload);
				}
			}
			else if(ReadStmt.class == parent.getClass()) {
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				Code.load(visitor.obj);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
				Code.put(Code.aload);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);
			}
		}
		
		// ako je rvalue treba samo da ga ucitamo
		else if(!(designator_part instanceof DesignatorArray) && !(designator_part instanceof DesignatorMatrix) && Assignment.class != parent.getClass()) {
			Code.load(visitor.obj);
		}
	}
	
	public void visit(AddExpr visitor) {
		if(visitor.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
			
		else if(visitor.getAddop() instanceof AddopMinus)
			Code.put(Code.sub);
	}
	
	public void visit(NegTermExpr visitor) {
		Code.put(Code.neg);
	}
	
	public void visit(MulopTerm visitor) {
		if(visitor.getMulop() instanceof MulopMul)
			Code.put(Code.mul);
		else if(visitor.getMulop() instanceof MulopDiv)
			Code.put(Code.div);
		else if(visitor.getMulop() instanceof MulopMod)
			Code.put(Code.rem);
	}
}
