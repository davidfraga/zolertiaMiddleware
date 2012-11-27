package br.ufpe.gprt.zolertia.deviceCommandProxy;


import java.util.List;

import br.ufpe.gprt.semantic.ContextManager.ActionTypeRelatedToCondition;
import br.ufpe.gprt.semantic.ContextManager.Enum_Action;
import br.ufpe.gprt.semantic.Policy;

public class CommandFormat {

	private static final String TEMPERATURE_CODE = "T";

	public static String getTemperatureCommand(CommandType type, List<Policy> policyList,
			Enum_Action action,
			ActionTypeRelatedToCondition actionTypeRelatedToCondition) {
		String result = type.name().toLowerCase()+" ";
		
		for (Policy policy : policyList) {
			result += TEMPERATURE_CODE;
			switch (policy.getCondition()) {
			case EQUALS:
				result += "=" + policy.getConditionParam();
				break;
			case GREATER_THAN:
				result += ">" + policy.getConditionParam();
				break;
			case LESS_THAN:
				result += "<" + policy.getConditionParam();
				break;
			default:
				break;
			}

			result += " && ";
		}

		result = result.substring(0, result.length() - 3);

		result += action.ordinal() + " " + actionTypeRelatedToCondition.name();

		return result;
	}
	
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
