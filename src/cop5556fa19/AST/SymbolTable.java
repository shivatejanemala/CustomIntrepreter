package cop5556fa19.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

	Map<Integer,ArrayList<StatLabel>> map = new HashMap<>();

	public void put(StatLabel statLabel, int blockNo) {
		// TODO Auto-generated method stub
		ArrayList<StatLabel> statList = new ArrayList<>();
		statLabel.index=blockNo;
		//map.put(statLabel, new Integer(blockNo));
		if(map.get(blockNo)!=null) {
			statList = map.get(blockNo);
		}
		else {
			statList = new ArrayList<>();
		}
		statList.add(statLabel);
		map.put(blockNo, statList);
	}

	public StatLabel get(StatGoto statGoto, int blockNo) {
		// TODO Auto-generated method stub
		StatLabel returnLabel = null;
		for(int i=blockNo; i>=0; i--) {
			ArrayList<StatLabel>labelList = map.get(i);
			if(labelList !=null) {
				if(iteratingStatList(labelList,statGoto)!=null) {
					returnLabel = iteratingStatList(labelList,statGoto);
					break;
				}
			}
			
		}
		return returnLabel;
	}

	private StatLabel iteratingStatList(ArrayList<StatLabel> labelList, StatGoto statGoto) {
		// TODO Auto-generated method stub
		for(int i=0; i<labelList.size();i++) {
			StatLabel label = labelList.get(0);
			if(label.label.equals(statGoto.name)) {
				//statGoto.label= label;
				return label;
			}
		}
		return null;
	}
	
	
}
