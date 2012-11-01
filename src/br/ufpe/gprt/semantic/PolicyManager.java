package br.ufpe.gprt.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpe.gprt.eventmanager.Part;
import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.zolertia.device.SensorData;

/**
 * Manages the policies.
 * The actualPolicyList provide information about policies states.
 * If the policy exists, and its value realted is FALSE, it was only selected (state='waiting')
 * If the value related is TRUE, its state is 'running'
 * If it doesn't exists, its state is 'dead' or 'stopped' 
 * @author GPRT-BEMO
 *
 */
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

	/**
	 * Define if the action is a command that has to be sent to zolertia network or just processed in application side
	 * @param currentPolicy
	 * @return
	 */
	private boolean actionIsCommand(Policy currentPolicy) {
		boolean result = true;

		// TODO not supported yet

		return result;
	}

	/**
	 * Check if the sensor data matches with the policy
	 * @param individualPolicy
	 * @param currentSensorValue
	 * @return confirmation
	 */
	// TODO Verify if this method is in the correct place and its need
	public Part policyEvaluation(br.ufpe.gprt.semantic.Policy individualPolicy,
			SensorData currentSensorValue, boolean withAction) {
		Part result = new Part();
		boolean policyEvaluated = true;
		double atualData = 0;
		
		if (individualPolicy.getDataType() == Enum_DataType.TEMPERATURE) {
			atualData = currentSensorValue.getTemperature();
			result.setKey(Enum_DataType.TEMPERATURE.name());			
		}
		if (individualPolicy.getDataType() == Enum_DataType.BATERY_INFORMATION) {
			atualData = currentSensorValue.getBatteryIndicator();
			result.setKey(Enum_DataType.BATERY_INFORMATION.name());
		}
		if (individualPolicy.getDataType() == Enum_DataType.RSSI) {
			atualData = currentSensorValue.getRssi();
			result.setKey(Enum_DataType.RSSI.name());
		}

		if (individualPolicy.getDataType() == Enum_DataType.HOP_COUNT) {
			atualData = currentSensorValue.getHops();
			result.setKey(Enum_DataType.HOP_COUNT.name());
		}

		if (individualPolicy.getCondition() == Enum_Condition.GREATER_THAN){
			policyEvaluated = (atualData > individualPolicy
					.getConditionParam());
		}

		if (individualPolicy.getCondition() == Enum_Condition.LESS_THAN) {
			policyEvaluated = (atualData < individualPolicy
					.getConditionParam());
		}

		if (individualPolicy.getCondition() == Enum_Condition.EQUALS) {
			policyEvaluated = (atualData == individualPolicy
					.getConditionParam());
		}
		System.out.println("Action taken:");

		if (policyEvaluated) {
			if (withAction) activatePolicyAction(individualPolicy);
			else {
				result.setValue(atualData+"");
				return result;
			}

		} else {
			System.out.println("Nothing");
		}

		return null;

	}

	private void activatePolicyAction(Policy individualPolicy) {
		if (individualPolicy.getAction() == Enum_Action.SEND_PACKETS_LESS_FREQUENTLY) {
			System.out.println("Send packets less frequently");
		}
		if (individualPolicy.getAction() == Enum_Action.SEND_PACKETS_MORE_FREQUENTLY) {
			System.out.println("Send packets more frequently");
		}
		if (individualPolicy.getAction() == Enum_Action.REBOOT) {
			System.out.println("Reboot");
		}
	}

	/**
	 * Change policy state to "running"
	 * @param policy
	 */
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

	/**
	 * Change the policy state to "waiting"
	 * @param policy
	 */
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
