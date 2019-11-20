package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cop5556fa19.Token;
import cop5556fa19.Token.Kind;
import cop5556fa19.AST.ASTVisitor;
import cop5556fa19.AST.Block;
import cop5556fa19.AST.Chunk;
import cop5556fa19.AST.Exp;
import cop5556fa19.AST.ExpBinary;
import cop5556fa19.AST.ExpFalse;
import cop5556fa19.AST.ExpFunction;
import cop5556fa19.AST.ExpFunctionCall;
import cop5556fa19.AST.ExpInt;
import cop5556fa19.AST.ExpList;
import cop5556fa19.AST.ExpName;
import cop5556fa19.AST.ExpNil;
import cop5556fa19.AST.ExpString;
import cop5556fa19.AST.ExpTable;
import cop5556fa19.AST.ExpTableLookup;
import cop5556fa19.AST.ExpTrue;
import cop5556fa19.AST.ExpUnary;
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.FieldList;
import cop5556fa19.AST.FieldNameKey;
import cop5556fa19.AST.FuncBody;
import cop5556fa19.AST.FuncName;
import cop5556fa19.AST.Name;
import cop5556fa19.AST.ParList;
import cop5556fa19.AST.RetStat;
import cop5556fa19.AST.Stat;
import cop5556fa19.AST.StatAssign;
import cop5556fa19.AST.StatBreak;
import cop5556fa19.AST.StatDo;
import cop5556fa19.AST.StatFor;
import cop5556fa19.AST.StatForEach;
import cop5556fa19.AST.StatFunction;
import cop5556fa19.AST.StatGoto;
import cop5556fa19.AST.StatIf;
import cop5556fa19.AST.StatLabel;
import cop5556fa19.AST.StatLocalAssign;
import cop5556fa19.AST.StatLocalFunc;
import cop5556fa19.AST.StatRepeat;
import cop5556fa19.AST.StatWhile;

public abstract class ASTVisitorAdapter implements ASTVisitor {
	
	@SuppressWarnings("serial")
	public static class StaticSemanticException extends Exception{
		
			public StaticSemanticException(Token first, String msg) {
				super(first.line + ":" + first.pos + " " + msg);
			}
		}
	
	
	@SuppressWarnings("serial")
	public
	static class TypeException extends Exception{

		public TypeException(String msg) {
			super(msg);
		}
		
		public TypeException(Token first, String msg) {
			super(first.line + ":" + first.pos + " " + msg);
		}
		
	}
	
	public abstract List<LuaValue> load(Reader r) throws Exception;

	@Override
	public Object visitExpNil(ExpNil expNil, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpBin(ExpBinary expBin, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
	LuaValue value = null;
	Exp e0 = expBin.e0;
	Exp e1 = expBin.e1;
	LuaValue lhs = (expEval(e0, arg).size()>0)? expEval(e0, arg).get(0):LuaNil.nil;
	LuaValue rhs = (expEval(e1, arg).size()>0)? expEval(e1, arg).get(0):LuaNil.nil;
	Kind op = expBin.op;
	switch(op) {
		case REL_GT:{
			if(!(lhs instanceof LuaInt && rhs instanceof LuaInt)) {
				throw new StaticSemanticException(expBin.firstToken, "LHS and RHS are not comparable");
			}
			if(((LuaInt)lhs).v > ((LuaInt)rhs).v) {
				value = new LuaBoolean(true);
			}
			else {
				value = new LuaBoolean(false);
			}
			break;
		}
		case REL_LT:{
			if(!(lhs instanceof LuaInt && rhs instanceof LuaInt)) {
				throw new StaticSemanticException(expBin.firstToken, "LHS and RHS are not comparable");
			}
			if(((LuaInt)lhs).v < ((LuaInt)rhs).v) {
				value = new LuaBoolean(true);
			}
			else {
				value = new LuaBoolean(false);
			}
			break;
		}
		case REL_EQEQ:{

			if(!(lhs instanceof LuaInt && rhs instanceof LuaInt)) {
				throw new StaticSemanticException(expBin.firstToken, "LHS and RHS are not comparable");
			}
			if(((LuaInt)lhs).v == ((LuaInt)rhs).v) {
				value = new LuaBoolean(true);
			}
			else {
				value = new LuaBoolean(false);
			}
		break;
		}
		case OP_PLUS:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v+((LuaInt)rhs).v);
			}}break;
		case OP_MINUS:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v-((LuaInt)rhs).v);
			}	
			break;
		}
		case OP_TIMES:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v*((LuaInt)rhs).v);
			}
			break;
		}
		case OP_DIV:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v/((LuaInt)rhs).v);
			}break;	
		}
		case OP_MOD:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v%((LuaInt)rhs).v);
			}break;	
		}
		case OP_POW:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt((int)Math.pow((((LuaInt)lhs).v),(((LuaInt)rhs).v)));
			}
			break;
		}
		case OP_DIVDIV:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				
		}
			break;
		}
		case DOTDOT:{
			if(lhs instanceof LuaString && rhs instanceof LuaString) {
				value = new LuaString(((LuaString)lhs).value + ((LuaString)rhs).value);
			}
			break;
		}
		case BIT_SHIFTL:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v << ((LuaInt)rhs).v);
			}
			break;
		}
		case BIT_SHIFTR:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v >> ((LuaInt)rhs).v);
			}
			break;
		}
		case BIT_AMP:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v & ((LuaInt)rhs).v);
			}
			break;
		}
		case BIT_XOR:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v^((LuaInt)rhs).v);
			}
			break;
		}
		case BIT_OR:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaInt(((LuaInt)lhs).v|((LuaInt)rhs).v);
			}
			break;
		}
		case REL_GE:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaBoolean(((LuaInt)lhs).v >= ((LuaInt)rhs).v);
			}
			break;
		}
		case REL_LE:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaBoolean(((LuaInt)lhs).v <= ((LuaInt)rhs).v);
			}
			break;
		}
		case REL_NOTEQ:{
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				value = new LuaBoolean(((LuaInt)lhs).v != ((LuaInt)rhs).v);
			}
			break;
		}
		case KW_and:{
			if(lhs instanceof LuaNil && (lhs instanceof LuaBoolean && lhs.equals(new LuaBoolean(false)))) {
				value = lhs;
			}else {
				value = rhs;
			}
			break;
		}
		case KW_or:{
			if(!(lhs instanceof LuaNil && (lhs instanceof LuaBoolean && lhs.equals(new LuaBoolean(false))))) {
				value = lhs;
			}else {
				value = rhs;
			}
			break;
		}
	}
	return value;
	}

	@Override
	public Object visitUnExp(ExpUnary unExp, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpInt(ExpInt expInt, Object arg) {
		return new LuaInt(expInt.v);
	}

	@Override
	public Object visitExpString(ExpString expString, Object arg) {
		return new LuaString(expString.v);

	}

	@Override
	public Object visitExpTable(ExpTable expTableConstr, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<Field> fieldList = expTableConstr.fields;
		LuaValue value = null;
		List<LuaValue> fieldValueList = new ArrayList<LuaValue>();
		Map<LuaValue,LuaValue> map = new HashMap<>();
		for(Field f: fieldList) {
			if(f instanceof FieldExpKey) {
				map.putAll((Map<LuaValue,LuaValue>)visitFieldExpKey((FieldExpKey)f, arg));
				
			}
			else if(f instanceof FieldImplicitKey) {
				fieldValueList.addAll((List<LuaValue>)visitFieldImplicitKey((FieldImplicitKey)f, arg));
			}
			else if(f instanceof FieldNameKey) {
				map.putAll((Map<LuaValue,LuaValue>)visitFieldNameKey((FieldNameKey)f, arg));
			}
		}
		int fieldSize = (fieldList.size()<LuaTable.DEFAULT_ARRAY_SIZE)? LuaTable.DEFAULT_ARRAY_SIZE : fieldValueList.size();
		if(fieldValueList.size()< fieldSize) {
			int size = fieldValueList.size();
			for(int i =0; i<(fieldSize-size);i++) {
				fieldValueList.add(LuaNil.nil);
			}
		}
		LuaValue[] valueArray = new LuaValue[fieldSize];
		if(fieldValueList.size()!=0) {
			valueArray= (LuaValue[]) fieldValueList.toArray(new LuaValue[0]);
		}
		
		
		value = new LuaTable(valueArray, map,fieldSize);
		return value;
	}

	@Override
	public Object visitExpList(ExpList expList, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitParList(ParList parList, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitName(Name name, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<LuaValue> valueList = null;
			List<Stat>statList = block.getStats();
			for (Stat stat : statList) {
				valueList = new ArrayList<>();
				if(stat instanceof StatAssign) {
					arg = visitStatAssign((StatAssign)stat, arg);
				}
				else if(stat instanceof RetStat) {
					valueList = (List<LuaValue>)visitRetStat((RetStat)stat, arg);
				}
				else if(stat instanceof StatDo) {
					Object a = visitStatDo((StatDo)stat, arg);
					if(((List<LuaValue>)a).size()!=0) {
						valueList = ((List<LuaValue>)a);
						break;
					}
				}
				else if(stat instanceof StatIf) {
					Object a = visitStatIf((StatIf)stat,arg);
					if(((List<LuaValue>)a).size()!=0) {
						valueList = ((List<LuaValue>)a);
						break;
					}
				}
				else if(stat instanceof StatWhile) {
					Object a = visitStatWhile((StatWhile)stat, arg);
					if(((List<LuaValue>)a).size()!=0) {
						valueList = ((List<LuaValue>)a);
						break;
					}
				}else if(stat instanceof StatBreak) {
					Object a = visitStatBreak((StatBreak)stat,arg);
					if(((List<LuaValue>)a).size()!=0) {
						valueList = ((List<LuaValue>)a);
						break;
					}
				}
			}
		return valueList;
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		Block block = statDo.b;
		List<LuaValue> valueList = new ArrayList<>();
		try {
			Object blockValue = visitBlock(block, arg);
			valueList = (List<LuaValue>)blockValue;
		}
		catch(UnsupportedOperationException ex) {
			/*if(!((LuaTable)arg).get("break").equals(new LuaBoolean(true))) {
				throw ex;
			}*/
		}
		return valueList;
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<LuaValue> valueList = null;
		Exp e = statWhile.e;
		Block b = statWhile.b;
		try {
		while(( (expEval(e,arg).size()>0)?(expEval(e,arg).get(0)):LuaNil.nil).equals(new LuaBoolean(true))) {
			valueList =(List<LuaValue>) visitBlock(b, arg);
		}
	}
	catch(UnsupportedOperationException ex) {
		
	}
	return valueList;
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		
		List<Exp> es = statIf.es;
		List<Block> bs = statIf.bs;
		List<LuaValue> valueList = new ArrayList<>();
		for(int i=0;i<es.size();i++) {
			Exp ifCondition = es.get(i);
			LuaValue key = expEval(ifCondition,arg).get(0);
			if(( key instanceof LuaBoolean && ((LuaBoolean)key).value) || (key instanceof LuaInt)) {
				Block b=null;
					b = bs.get(i);
				Object blockValue = visitBlock(b,arg);
				valueList = (List<LuaValue>)blockValue;
				break;
			}
			if(bs.size()-es.size()==1) {
				Block b = bs.get(bs.size());
				Object blockValue = visitBlock(b,arg);
				valueList = (List<LuaValue>)blockValue;
				break;  
			}
		}
		//throw new UnsupportedOperationException();
	return valueList;
	}

	@Override
	public Object visitStatFor(StatFor statFor1, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatForEach(StatForEach statForEach, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFuncName(FuncName funcName, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatFunction(StatFunction statFunction, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalFunc(StatLocalFunc statLocalFunc, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalAssign(StatLocalAssign statLocalAssign, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitRetStat(RetStat retStat, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<Exp> expList = retStat.getEl();
		List<LuaValue> valueList = new ArrayList<>();
		LuaTable table = (LuaTable)arg;
		for(Exp e:expList) {
			valueList.addAll(expEval(e, arg));
		}
	return valueList;
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		Block block = chunk.getBlock();
		List<LuaValue> valueList = null;
		valueList = (List<LuaValue>)visitBlock(block, arg);
		//return ((LuaTable)arg).get(new LuaInt(1));
		return valueList;
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object object) throws Exception {
		//throw new UnsupportedOperationException();
		Exp key = fieldExpKey.key;
		Exp value = fieldExpKey.value;
		List<LuaValue> valueList = new ArrayList<>();
		LuaValue keyValue = (expEval(key, (LuaTable)object).size()>0)?expEval(key, (LuaTable)object).get(0):LuaNil.nil;
		LuaValue valueValue = (expEval(value, (LuaTable)object).size()>0)?expEval(value, (LuaTable)object).get(0):LuaNil.nil;
		Map<LuaValue,LuaValue> map = new HashMap<>();
		map.put(keyValue, valueValue);
		return map;
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		Map<LuaValue,LuaValue> map = new HashMap<>();
		Exp exp = fieldNameKey.exp;
		Name name = fieldNameKey.name;
		LuaValue expValue = ((List<LuaValue>) expEval(exp, arg)).get(0);
		LuaValue nameValue = new LuaString(name.name);
		map.put(nameValue, expValue);
		return map;
	}
	
	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<LuaValue> valueList = new ArrayList<>();
		Exp ee = fieldImplicitKey.exp;
		valueList = expEval(ee, arg);
		return valueList;
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) {
		//throw new UnsupportedOperationException();
		return new LuaBoolean(true);
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) {
		//throw new UnsupportedOperationException();
		return new LuaBoolean(false);
	}

	@Override
	public Object visitFuncBody(FuncBody funcBody, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpVarArgs(ExpVarArgs expVarArgs, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatAssign(StatAssign statAssign, Object arg) throws Exception {
		List<Exp>expList = statAssign.getExpList();
		List<Exp>varList = statAssign.getVarList();
		if(varList.size()== expList.size()) {
			for(int i=0;i<varList.size();i++) {
				Exp e = varList.get(i);
				LuaValue key = variableStore(e,arg);
				Exp ee = expList.get(i);
				LuaValue value =(expEval(ee, arg).size()>0)? expEval(ee, arg).get(0):LuaNil.nil;
				if(e instanceof ExpTableLookup) {
				 AssignExpTableLookup((ExpTableLookup)e,ee,arg);
				}
				else{
					((LuaTable)arg).put(key,value);
				};
			}
			return arg;
		}
		throw new UnsupportedOperationException();
	}


	private void AssignExpTableLookup(ExpTableLookup e, Exp ee, Object arg) throws Exception {
		// TODO Auto-generated method stub
		ExpName table = (ExpName)e.table;
		Exp key = e.key;
		LuaValue tableLookup = ((LuaTable)arg).get(new LuaString(table.name));
		//LuaTable tableValue = (LuaTable)((LuaTable)arg).map.get(tableLookup); 
		LuaValue eevalue = (expEval(ee,arg).size()>0)? expEval(ee,arg).get(0) : LuaNil.nil ;
		LuaValue keyValue = (expEval(key,arg).size()>0)? expEval(key,arg).get(0) : LuaNil.nil ;	
		((LuaTable)tableLookup).put(keyValue, eevalue);
	}

	private List<LuaValue> expEval(Exp e,Object arg) throws Exception{
		// TODO Auto-generated method stub
		List<LuaValue> value = new ArrayList<>();
		if(e instanceof ExpInt) {
			value.add((LuaValue)visitExpInt((ExpInt)e, arg));
		}
		else if(e instanceof ExpName) {
			value.add((LuaValue)visitExpName((ExpName)e, arg));
		}else if(e instanceof ExpString) {
			value.add((LuaValue)visitExpString((ExpString)e,arg));
		}
		else if( e instanceof ExpTrue) {
			value.add((LuaValue)visitExpTrue((ExpTrue)e,arg));
		}else if(e instanceof ExpFalse) {
			value.add((LuaValue)visitExpFalse((ExpFalse)e,arg));
		}else if(e instanceof ExpBinary) {
			value.add((LuaValue)visitExpBin((ExpBinary)e,arg));
		}else if(e instanceof ExpFunctionCall) {
			value = (List<LuaValue>)visitExpFunctionCall((ExpFunctionCall)e,arg);
		}
		else if(e instanceof ExpTable) {
			value.add((LuaTable)visitExpTable((ExpTable)e,arg));
		}else if(e instanceof ExpTableLookup) {
			value.add((LuaValue)visitExpTableLookup((ExpTableLookup)e, arg));
		}else if(e instanceof ExpUnary) {
			value.add((LuaValue)visitExpTableLookup((ExpTableLookup)e, arg));
		}
		return value;
	}

	private LuaValue variableStore(Exp e, Object arg) throws Exception {
		LuaValue value = null;
		if(e instanceof ExpName) {
			value = new LuaString(((ExpName) e).name);
			boolean namefound = false;
			for(int i=0; i< ((LuaTable)arg).arraySize;i++) {
				if(value.equals(((LuaTable)arg).array[i])){
					namefound = true;
					break;
				}
			};
			if(!namefound) {
					((LuaTable)arg).putImplicit(value);
			}
		}
		else if(e instanceof ExpTableLookup) {
			value =  (expEval(e, arg).size()>0)?expEval(e, arg).get(0):LuaNil.nil;
		}
		return value;
	}
	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		LuaValue value = null;
		Exp table = expTableLookup.table;
		Exp key = expTableLookup.key;
		LuaTable tableValue =(LuaTable)( (expEval(table, arg).size()>0)?expEval(table, arg).get(0):LuaNil.nil);
		LuaValue keyValue = expEval(key, arg).get(0);
		value = tableValue.get(keyValue);
		return value;
	}

	@Override
	public Object visitExpFunctionCall(ExpFunctionCall expFunctionCall, Object arg) throws Exception {
		//throw new UnsupportedOperationException();
		List<LuaValue> value = null;
		ExpName f = (ExpName)expFunctionCall.f;
		List<Exp> functionArgs = expFunctionCall.args;
		List<LuaValue>argsValue = new ArrayList<>();
		for(Exp a: functionArgs) {
			LuaValue aval = (expEval(a, arg).size()>0)? expEval(a, arg).get(0):LuaNil.nil;
			argsValue.add(aval);
		}
		switch(f.name) {
		case "print": {
			value = ((JavaFunction)((LuaTable)arg).get("print")).call(argsValue);
		}break;
		case "println":{
			value = ((JavaFunction)((LuaTable)arg).get("println")).call(argsValue);
		}break;
		case "toNumber":{
			value = ((JavaFunction)((LuaTable)arg).get("toNumber")).call(argsValue);
		}break;
		}
		return value;
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) {
		LuaValue value = ((LuaTable)arg).get(expName.name);
		/*if(value instanceof LuaNil) {
		return new LuaString(expName.name);
		}*/
		return value;
	}



}
