package br.ufpe.gprt.semantic;
import java.util.ArrayList;

public class ContextManager {

	private static PolicyManager s_policyManager;

	public enum Enum_Condition {
		GREATER_THAN, LESS_THAN, EQUALS
	}

	public enum Enum_Action {
		SEND_PACKETS_LESS_FREQUENTLY, SEND_PACKETS_MORE_FREQUENTLY, REBOOT
	}

	public enum Enum_DataType {

		TEMPERATURE, BATERY_INFORMATION, RSSI, ATUAL_CHANNEL, HOP_COUNT

	}
	//--Search desired policy in database--//
	public static int selectPolicy(Enum_DataType interest, Enum_Condition condition,
			int param, Enum_Action action) {

		int interestCode = 0;
		int conditionCode = 0;
		int actionCode = 0;
		int index = 0;

		//Translate the components of desired policy in codes, to search in database--//

		if (interest == Enum_DataType.TEMPERATURE) {
			interestCode = 0;
		}

		if (interest == Enum_DataType.BATERY_INFORMATION) {
			interestCode = 1;
		}

		if (interest == Enum_DataType.RSSI) {
			interestCode = 2;
		}

		if (interest == Enum_DataType.ATUAL_CHANNEL) {
			interestCode = 3;
		}

		if (interest == Enum_DataType.HOP_COUNT) {
			interestCode = 4;
		}

		if (condition == Enum_Condition.GREATER_THAN) {
			conditionCode = 0;
		}

		if (condition == Enum_Condition.LESS_THAN) {
			conditionCode = 1;
		}

		if (condition == Enum_Condition.EQUALS) {
			conditionCode = 2;
		}
		
		if (action == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY) {
			actionCode = 0;
		}
		if (action == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY) {
			actionCode = 1;
		}
		if (action == Enum_Action.REBOOT) {
			actionCode = 2;
		}
		
		
		Policy currentPolicy = new Policy(interestCode, conditionCode, param, actionCode);
		
		/*--verifies that the policy already exists in the database. 
		If not, will be added. The index of it is returned*/

		if (s_policyManager.containPolicy(currentPolicy)) {
			index = s_policyManager.searchPolicy(currentPolicy);
		} else {
			s_policyManager.addPolicy(currentPolicy);
			index = s_policyManager.searchPolicy(currentPolicy);
		}

		return index;
	}

	public static Policy getPolicyByIndex(int index) {

		return s_policyManager.getPolicyByIndex(index);
	}

	public ContextManager() {

		s_policyManager = new PolicyManager();
	}

}
