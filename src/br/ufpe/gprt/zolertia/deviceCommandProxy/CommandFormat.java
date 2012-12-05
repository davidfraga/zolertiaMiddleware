package br.ufpe.gprt.zolertia.deviceCommandProxy;


import java.util.List;

import br.ufpe.gprt.semantic.ContextManager.ActionTypeRelatedToCondition;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.Policy;

public class CommandFormat {
	
	public enum CommandType{
		INSERT, DELETE
	}
	
	public static String getPeriodCommand(Enum_Action action){
		String result = "netcmd {";
		switch (action) {
		case SEND_PACKETS_LESS_FREQUENTLY:
			result +="period+}";
			break;
		case SEND_PACKETS_MORE_FREQUENTLY:
			result +="period-}";
			break;

		default:
			break;
		}
		
		return result;
	}
}
