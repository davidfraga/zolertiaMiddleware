package br.ufpe.gprt.semantic;

import br.ufpe.gprt.semantic.PolicyManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Condition;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;

public class Policy {
	
	private Enum_DataType s_dataTypeCode;
	private Enum_Condition s_conditionCode;
	private int s_conditionParam;
	private Enum_Action s_actionCode;
		
	
	public Enum_Condition getCondition (){
		return s_conditionCode;
	}
	
	public int getConditionParam (){
		return s_conditionParam;
	}
	
	public Enum_Action getAction () {
		return s_actionCode;
	}
	
	public Enum_DataType getDataType (){
		return s_dataTypeCode;
	}
	
	public Policy (Enum_DataType dataType, Enum_Condition conditionCode, int conditionParam, Enum_Action actionCode){				
		s_dataTypeCode = dataType;
		s_conditionCode = conditionCode;
		s_conditionParam = conditionParam;
		s_actionCode = actionCode;
	}

}
