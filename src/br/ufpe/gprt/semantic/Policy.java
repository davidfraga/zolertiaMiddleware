package br.ufpe.gprt.semantic;
public class Policy {
	
	private static int s_dataTypeCode;
	private static int s_conditionCode;
	private static int s_conditionParam;
	private static int s_actionCode;
	private static int s_dataString;
	
	
	
	public static int getConditionCode (){
		return s_conditionCode;
	}
	
	public static int getConditionParam (){
		return s_conditionParam;
	}
	
	public static int getActionCode () {
		return s_actionCode;
	}
	
	public static int getDataType (){
		return s_dataTypeCode;
	}
	
	public Policy (int dataType, int conditionCode, int conditionParam, int actionCode){
		
				
		s_dataTypeCode = dataType;
		s_conditionCode = conditionCode;
		s_conditionParam = conditionParam;
		s_actionCode = actionCode;
	}

}
