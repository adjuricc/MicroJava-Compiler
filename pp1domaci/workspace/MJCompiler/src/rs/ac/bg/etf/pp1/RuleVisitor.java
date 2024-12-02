package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import org.apache.log4j.*;

public class RuleVisitor extends VisitorAdaptor{
	
	int printCallCount = 0;
	int varDeclCnt = 0;
	Logger log = Logger.getLogger(getClass());
	
	public void visit(PrintStmt PrintStmt) { 
		printCallCount++;
		log.info("Prepoznata naredba print!");
	}
	
	public void visit(VarDecl VarDecl) { 
		varDeclCnt++;
	}
}
