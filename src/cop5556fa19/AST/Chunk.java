package cop5556fa19.AST;

import java.util.List;

import cop5556fa19.Token;
import interpreter.StaticAnalysis;

public class Chunk extends ASTNode {
	
	
	public final Block block;
	int numLocals;
	SymbolTable symbolTable;
	
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public int getNumLocals() {
		return numLocals;
	}
	
	public Block getBlock() {
		return block;
	}

	public void setNumLocals(int numLocals) {
		this.numLocals = numLocals;
	}


	public Chunk(Token firstToken, Block b) {
		super(firstToken);
		this.block = b;
	}

	
	@Override
	public String toString() {
		return "Chunk [block=" + block +  "]";
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		return true;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitChunk(this,arg);
	}
	
	

}
