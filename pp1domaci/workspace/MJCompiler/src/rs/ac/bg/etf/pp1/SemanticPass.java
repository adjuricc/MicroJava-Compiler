package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {
	int printCallCount = 0;
	int varDeclCnt = 0;
	boolean errorDetected = false;
	Obj currentMethod = null;
	
	Logger log = Logger.getLogger(getClass());
	

	public boolean passed(){
        return !errorDetected;
    }
	
	public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" on line ").append(line);
        log.info(msg.toString());
    }

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" on line").append(line);
        log.error(msg.toString());
    }

    public void visit(VarType visitor) {
    	varDeclCnt++;
    }
	
	public void visit(VarDecl vardecl) { 
		
		VarList vardecl_list = vardecl.getVarList();
		
		while(vardecl_list instanceof VarListMany) {
			VarListMany varlist_many = (VarListMany) vardecl_list;
			
			VarType var_type = varlist_many.getVarType();
			VarArrayOpt var_array_opt = var_type.getVarArrayOpt();
			
			
			if(var_array_opt instanceof VarNoArray) {
				
				Obj var_node = Tab.insert(Obj.Var, var_type.getName(), vardecl.getType().struct);
			}
			else {
				Struct s = new Struct(Struct.Array, vardecl.getType().struct);
				Obj var_node = Tab.insert(Obj.Var, var_type.getName(), s);
			}
			
			vardecl_list = varlist_many.getVarList();
		}
		
		if(vardecl_list instanceof VarListOne){
			VarType var_type = ((VarListOne) vardecl_list).getVarType();
			VarArrayOpt var_array_opt = var_type.getVarArrayOpt();
			
			if(var_array_opt instanceof VarNoArray) {
				Obj var_node = Tab.insert(Obj.Var, var_type.getName(), vardecl.getType().struct);
			}
			else {
				Struct s = new Struct(Struct.Array, vardecl.getType().struct);
				Obj var_node = Tab.insert(Obj.Var, var_type.getName(), s);
			}
		}
			
	}
	
	public void visit(Type type) {
		Obj type_node = Tab.find(type.getI1());
		
		if(type_node == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getI1() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		}
		else {
			if(Obj.Type == type_node.getKind()) {
				type.struct = type_node.getType();
			}
			else {
				report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
	}
	
	public void visit(ConstDecl constdecl) {
		
		ConstList const_list = constdecl.getConstList();
		
		while(const_list instanceof ConstListMany) {
			ConstListMany const_list_many = (ConstListMany) const_list;
			
			ConstDef const_def = const_list_many.getConstDef();
			
			Obj const_node = Tab.insert(Obj.Con, const_def.getI1(), constdecl.getType().struct);
			
			ConstVal const_val = const_def.getConstVal();
			
			if(const_val instanceof ConstNumber)
				const_node.setAdr(((ConstNumber)const_val).getI1());
			else if(const_val instanceof ConstChar)
				const_node.setAdr((int)((ConstChar)const_val).getC1().charAt(0));
			else if(const_val instanceof ConstBool) {
				boolean flag = true;
				int intValue = flag ? 1 : 0;
				
				const_node.setAdr(intValue);
			}
				
			
			const_list = constdecl.getConstList();
		}
		
		if(const_list instanceof ConstListOne) {
			ConstListOne const_list_one = (ConstListOne) const_list;
			
			ConstDef const_def = const_list_one.getConstDef();
			
			Obj const_node = Tab.insert(Obj.Con, const_def.getI1(), constdecl.getType().struct);
			
			ConstVal const_val = const_def.getConstVal();
			
			if(const_val instanceof ConstNumber)
				const_node.setAdr(((ConstNumber)const_val).getI1());
			else if(const_val instanceof ConstChar)
				const_node.setAdr((int)(((ConstChar)const_val).getC1().charAt(1)));
			else if(const_val instanceof ConstBool) {
				int intValue = ((ConstBool)const_val).getB1().equals("true") ? 1 : 0;
				
				const_node.setAdr(intValue);
			}
		}
	}
	
	public void visit(ConstNumber visitor) {
		visitor.struct = MyTab.intType;
	}
	
	public void visit(ConstBool visitor) {
		visitor.struct = MyTab.boolType;
	}
	
	public void visit(ConstChar visitor) {
		visitor.struct = MyTab.charType;
	}
	
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) {
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	
	public void visit(MainMethod mainmethod) {
		mainmethod.obj = Tab.insert(Obj.Meth, "main", Tab.noType);
		currentMethod = mainmethod.obj;
		Tab.openScope();
		
		report_info("Obradjuje se funkija main", mainmethod);
	}
	
	public void visit(MethodDecl methoddecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		currentMethod = null;
	}
	
	public void visit(Designator designator) {
		Obj obj = MyTab.find(designator.getName());
		
		if(obj == MyTab.noObj) {
			report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getName() + " nije deklarisano!", null);
		}
		else {
			designator.obj = obj;
			
			DesignatorPart designator_part = designator.getDesignatorPart();
			
			if(obj.getType().getKind() != Struct.Array && !(designator_part instanceof DesignatorNoPart)) {
				report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getName() + " mora biti niz!", null);
			}
		}
		
//		designator.obj = obj;
	}
	
	public void visit(DesignatorArray visitor) {
		
	}
	
	public void visit(DesignatorNoPart visitor) {
		
	}
	
	private boolean compatibility(Struct dst, Struct src) {
		if(dst.getKind() == Struct.Array){
			dst = dst.getElemType();
		}
		if(src.getKind() == Struct.Array) {
			src = src.getElemType();
		}
		
		return dst == src;
	}
	
	
	public void visit(Assignment assignment) {
		Struct dest_type = assignment.getDesignator().obj.getType();
		Struct expr_type = assignment.getExpr().struct;
		
//		if(dest_type.getKind() == Struct.Array && assignment.getExpr() instanceof TermExpr) {
//			if(((TermExpr) assignment.getExpr()).getTerm() instanceof CommonTerm) {
//				CommonTerm common_term = ((CommonTerm)((TermExpr) assignment.getExpr()).getTerm());
//				
//				if(!(common_term.getFactor() instanceof NewExpr)) {
//					NewExpr new_expr = ((NewExpr)common_term.getFactor());
//				}
//			}
//		}
		
		if(!compatibility(dest_type, expr_type)) {
			report_error("Greska na liniji " + assignment.getLine() + ": Tipovi moraju biti isti! ", null);	
		}
	}
	
	public void visit(PlusPlusStmt visitor) {
		Obj designator = visitor.getDesignator().obj;
		
		
		if(designator.getType().getKind() == Struct.Array && designator.getType().getElemType() != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operand mora biti tipa int! ", null);
		}
		else if(designator.getType().getKind() != Struct.Array && designator.getType() != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operand mora biti tipa int! ", null);
		}
	}
	
	public void visit(MinusMinusStmt visitor) {
		Obj designator = visitor.getDesignator().obj;
		
		if(designator.getType().getKind() == Struct.Array && designator.getType().getElemType() != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operand mora biti tipa int! ", null);
		}
		else if(designator.getType().getKind() != Struct.Array && designator.getType() != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operand mora biti tipa int! ", null);
		}
	}
	
	public void visit(PrintStmt visitor) { 
		
		Struct expr_struct = null;
		
		if(visitor.getExpr().struct.getKind() == Struct.Array)
			expr_struct = visitor.getExpr().struct.getElemType();
		
		if(expr_struct != null && (expr_struct != MyTab.intType && expr_struct != MyTab.charType && expr_struct != MyTab.boolType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Argument mora biti int, char ili bool ", null);
		}
		else if(expr_struct == null && visitor.getExpr().struct != MyTab.intType && visitor.getExpr().struct != MyTab.charType && visitor.getExpr().struct != MyTab.boolType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Argument mora biti int, char ili bool ", null);
		}
	}
	
	public void visit(ReadStmt visitor) {
		Struct designator_struct = null;
		
		
		
		if(visitor.getDesignator().obj.getType().getKind() == Struct.Array)
			designator_struct = visitor.getDesignator().obj.getType().getElemType();
		
		log.info(designator_struct);
		
		if(designator_struct != null && (designator_struct != MyTab.intType && designator_struct != MyTab.charType && designator_struct != MyTab.boolType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Argument mora biti int, char ili bool ", null);
		}
		else if(designator_struct == null && visitor.getDesignator().obj.getType() != MyTab.intType && visitor.getDesignator().obj.getType() != MyTab.charType && visitor.getDesignator().obj.getType() != MyTab.boolType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Argument mora biti int, char ili bool ", null);
		}
	}
	
	public void visit(AddExpr visitor) {
		visitor.struct = visitor.getTerm().struct;
		
		log.info(visitor.getExpr());
		log.info(visitor.getTerm());
		
		log.info(visitor.getExpr().struct.getKind());
		log.info(visitor.getTerm().struct.getKind());
		
		// provera da li su i leva i desna strana int
		
		Struct expr_struct = null;
		Struct term_struct = null;
		
		if(visitor.getExpr().struct.getKind() == Struct.Array)
			expr_struct = visitor.getExpr().struct.getElemType();
		if(visitor.getTerm().struct.getKind() == Struct.Array)
			term_struct = visitor.getTerm().struct.getElemType();
		
		// ako su obe strane niz treba da proverimo da li su elem type jednaki
		if((expr_struct != null && term_struct != null) && 
				(expr_struct != MyTab.intType || term_struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((expr_struct != null && term_struct == null) && 
				(expr_struct != MyTab.intType || visitor.getTerm().struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((expr_struct == null && term_struct != null) && 
				(visitor.getExpr().struct != MyTab.intType || term_struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((expr_struct == null && term_struct == null) && 
				(visitor.getExpr().struct != MyTab.intType || visitor.getTerm().struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
	}
	
	public void visit(NegTermExpr visitor) {
		visitor.struct = visitor.getTerm().struct;
		
		if(visitor.getTerm().struct != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operand mora biti tipa int! ", null);
		}
	}
	
	public void visit(TermExpr termexpr) {
		termexpr.struct = termexpr.getTerm().struct;
	}
	
	public void visit(CommonTerm commonterm) {
		commonterm.struct = commonterm.getFactor().struct;
	}
	
	public void visit(MulopTerm visitor) {
		visitor.struct = visitor.getFactor().struct;
		
		Struct term_struct = null;
		Struct factor_struct = null;
		
		if(visitor.getTerm().struct.getKind() == Struct.Array)
			term_struct = visitor.getTerm().struct.getElemType();
		if(visitor.getFactor().struct.getKind() == Struct.Array)
			factor_struct = visitor.getFactor().struct.getElemType();
		
		
		if((term_struct != null && factor_struct != null) && 
				(term_struct != MyTab.intType || factor_struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((term_struct != null && factor_struct == null) && 
				(term_struct != MyTab.intType || visitor.getFactor().struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((term_struct == null && factor_struct != null) && 
				(visitor.getTerm().struct != MyTab.intType || factor_struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
		else if((term_struct == null && factor_struct == null) && 
				(visitor.getTerm().struct != MyTab.intType || visitor.getFactor().struct != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Operandi moraju biti tipa int! ", null);
		}
	}
	
	public void visit(Const visitor) {
		visitor.struct = MyTab.intType;
	}
	
	public void visit(FactorBool visitor) {
		visitor.struct = MyTab.boolType;
	}
	
	public void visit(FactorChar visitor) {
		visitor.struct = MyTab.charType;
	}
	
	public void visit(Var visitor) {
		visitor.struct = visitor.getDesignator().obj.getType();
	}
	
	public void visit(NewExpr visitor) {
		visitor.struct = visitor.getType().struct;
		
		if(visitor.getType().struct != MyTab.intType && visitor.getType().struct != MyTab.charType && visitor.getType().struct != MyTab.boolType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Tip mora biti tipa int, char ili bool! ", null);
		}
		
		if((visitor.getExpr().struct.getKind() == Struct.Array && visitor.getExpr().struct.getElemType() != MyTab.intType)) {
			report_error("Greska na liniji " + visitor.getLine() + ": Duzina niza mora biti tipa int! ", null);
		}
		else if((visitor.getExpr().struct.getKind() != Struct.Array) &&
				visitor.getExpr().struct != MyTab.intType) {
			report_error("Greska na liniji " + visitor.getLine() + ": Duzina niza mora biti tipa int! ", null);
		}
	}
	
	public void visit(NewFactorMatrix visitor) {
		visitor.struct = visitor.getType().struct;
	}
	
	public void visit(FactorParen visitor) {
		visitor.struct = visitor.getExpr().struct;
	}
	
}
