package br.ufpe.gprt.semantic;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.zolertia.device.SensorData;

public class PolicyManager {

	private Map<Policy, Boolean> actualPolicyList;

	public int addPolicy(Policy newPolicy, boolean isRunning) {
		actualPolicyList.put(newPolicy, false);
		return (actualPolicyList.size() - 1);
	}

	public Policy getPolicyByIndex(int index) {
		Policy[] policies = (Policy[]) actualPolicyList.keySet().toArray();
		return policies[index];
	}

	public boolean containPolicy(Policy policy) {
		return actualPolicyList.containsValue(policy);
	}

	public PolicyManager() {
		actualPolicyList = new HashMap<Policy, Boolean>();
	}

	public enum Enum_Condition {
		GREATER_THAN, LESS_THAN, EQUALS
	}

	public enum Enum_Action {
		SEND_PACKETS_LESS_FREQUENTLY, SEND_PACKETS_MORE_FREQUENTLY, REBOOT, STOP_POLICY
	}

	public enum Enum_DataType {

		TEMPERATURE, BATERY_INFORMATION, RSSI, ATUAL_CHANNEL, HOP_COUNT

	}

	public Policy createPolicy(Enum_DataType interest,
			Enum_Condition condition, int param, Enum_Action action) {

		Policy currentPolicy = new Policy(interest, condition, param, action);
		boolean isRunning = false;
		addPolicy(currentPolicy, isRunning);

		return currentPolicy;
	}

	private boolean actionIsCommand(Policy currentPolicy) {
		boolean result = true;

		// TODO not supported

		return result;
	}

	public boolean policyCheck(br.ufpe.gprt.semantic.Policy individualPolicy,
			SensorData currentSensorValue) {

		boolean policyEvaluation = true;
		double atualData = 0;

		if (individualPolicy.getDataType() == Enum_DataType.TEMPERATURE) {
			atualData = currentSensorValue.getTemperature();
		}
		if (individualPolicy.getDataType() == Enum_DataType.BATERY_INFORMATION) {
			atualData = currentSensorValue.getBatteryIndicator();
		}
		if (individualPolicy.getDataType() == Enum_DataType.RSSI) {
			atualData = currentSensorValue.getRssi();
		}

		/*
		 * if (individualPolicy.getDataType() == 3) { atualData =
		 * currentSensorValue.getAtualChannel(); }
		 */

		if (individualPolicy.getDataType() == Enum_DataType.HOP_COUNT) {
			atualData = currentSensorValue.getHops();
		}

		if (individualPolicy.getCondition() == Enum_Condition.GREATER_THAN)
			policyEvaluation = (atualData > individualPolicy
					.getConditionParam());

		if (individualPolicy.getCondition() == Enum_Condition.LESS_THAN)
			policyEvaluation = (atualData < individualPolicy
					.getConditionParam());

		if (individualPolicy.getCondition() == Enum_Condition.EQUALS)
			policyEvaluation = (atualData == individualPolicy
					.getConditionParam());

		System.out.println("Action taken:");

		if (policyEvaluation) {

			if (individualPolicy.getAction() == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY) {
				System.out.println("Send packets less frequently");
			}
			if (individualPolicy.getAction() == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY) {
				System.out.println("Send packets more frequently");
			}
			if (individualPolicy.getAction() == Enum_Action.REBOOT) {
				System.out.println("Reboot");
			}

		} else {
			System.out.println("Nothing");
		}

		return policyEvaluation;

	}

	public void policyStart(Policy policy) {

		if (actionIsCommand(policy)
				&& !(Boolean) this.actualPolicyList.get(policy)) {
			ResourceManager.getInstance().getZolertiaData()
					.createCommand(policy);
			boolean isRunning = true;
			actualPolicyList.remove(policy);
			addPolicy(policy, isRunning);
		}
	}

	public void policyStop(Policy policy) {

		if (actionIsCommand(policy)
				&& !(Boolean) this.actualPolicyList.get(policy)) {
			ResourceManager.getInstance().getZolertiaData()
					.stopCommand(policy);
			boolean isRunning = false;
			actualPolicyList.remove(policy);
			addPolicy(policy, isRunning);
		}
	}

}
