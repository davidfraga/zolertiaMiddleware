package br.ufpe.gprt.semantic;

import br.ufpe.gprt.semantic.PolicyManager.Enum_Action;
import br.ufpe.gprt.semantic.PolicyManager.Enum_Condition;
import br.ufpe.gprt.semantic.PolicyManager.Enum_DataType;

/**
 * Policy is the rule that satisfies a context.
 * Its structure is P = DataType,Condition,Value,Action
 * @author GPRT-BEMO
 *
 */
public class Policy {
	
	private PolicyManager.Enum_DataType s_dataTypeCode;
	private PolicyManager.Enum_Condition s_conditionCode;
	private int s_conditionParam;
	
		
	
	public PolicyManager.Enum_Condition getCondition (){
		return s_conditionCode;
	}
	
	public int getConditionParam (){
		return s_conditionParam;
	}
	
	
	
	public PolicyManager.Enum_DataType getDataType (){
		return s_dataTypeCode;
	}
	
	public Policy (Enum_DataType dataType, Enum_Condition conditionCode, int conditionParam){				
		s_dataTypeCode = dataType;
		s_conditionCode = conditionCode;
		s_conditionParam = conditionParam;
		
	}

}
