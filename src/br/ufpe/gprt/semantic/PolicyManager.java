package br.ufpe.gprt.semantic;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.gprt.resources.ResourceManager;
import br.ufpe.gprt.zolertia.device.SensorData;
import br.ufpe.gprt.zolertia.deviceCommandProxy.CommandConnection;
import br.ufpe.gprt.zolertia.deviceCommandProxy.CommandFormat;

/**
 * Manages the policies. The actualPolicyList provide information about policies
 * states. If the policy exists, and its value realted is FALSE, it was only
 * selected (state='waiting') If the value related is TRUE, its state is
 * 'running' If it doesn't exists, its state is 'dead' or 'stopped'
 * 
 * @author GPRT-BEMO
 * 
 */
public class PolicyManager {

	private HashMap<Policy, Boolean> actualPolicyList;

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


	public enum Enum_DataType {

		TEMPERATURE, BATERY_INFORMATION, RSSI, ATUAL_CHANNEL, HOP_COUNT

	}

	public Policy createPolicy(Enum_DataType interest,Enum_Condition condition, int param) {
		Policy currentPolicy = new Policy(interest, condition, param);
		boolean isRunning = false;
		addPolicy(currentPolicy, isRunning);

		return currentPolicy;
	}

	/**
	 * Define if the action is a command that has to be sent to zolertia network
	 * or just processed in application side
	 * 
	 * @param currentPolicy
	 * @return
	 */
	private boolean actionIsCommand(Policy currentPolicy) {
		boolean result = false;

		// TODO not supported yet

		return result;
	}

	/**
	 * Check if the sensor data matches with the policy
	 * 
	 * @param individualPolicy
	 * @param currentSensorValue
	 * @return confirmation
	 */
	// TODO Verify if this method is in the correct place and its need
	
	
	public Map<Enum_DataType, Double> policyEvaluation(br.ufpe.gprt.semantic.Policy individualPolicy,
	SensorData currentSensorValue, boolean withAction) {
		Map<Enum_DataType, Double> policyEvaluated = new HashMap<PolicyManager.Enum_DataType, Double>();
		double atualData = 0;
		
		if (individualPolicy.getDataType() == Enum_DataType.TEMPERATURE) {
			atualData = currentSensorValue.getTemperature();
			System.out.println("PM: temperature "+atualData+"ºC");			
		}
		if (individualPolicy.getDataType() == Enum_DataType.BATERY_INFORMATION) {
			atualData = currentSensorValue.getBatteryIndicator();
		}
		if (individualPolicy.getDataType() == Enum_DataType.RSSI) {
			atualData = currentSensorValue.getRssi();
		}
		
		if (individualPolicy.getDataType() == Enum_DataType.HOP_COUNT) {
			atualData = currentSensorValue.getHops();
		}
		
		if (individualPolicy.getCondition() == Enum_Condition.GREATER_THAN) {
			if (atualData > individualPolicy.getConditionParam()){
				policyEvaluated.put(individualPolicy.getDataType(), atualData);
			}
		}
		
		if (individualPolicy.getCondition() == Enum_Condition.LESS_THAN) {
			if (atualData < individualPolicy.getConditionParam()){
				policyEvaluated.put(individualPolicy.getDataType(), atualData);
			}
		}
		
		if (individualPolicy.getCondition() == Enum_Condition.EQUALS) {
			if (atualData == individualPolicy
					.getConditionParam()){
				policyEvaluated.put(individualPolicy.getDataType(), atualData);
			}
		}
		
		return policyEvaluated;

	}

	
/*
	*//**
	 * Change policy state to "running"
	 * 
	 * @param policy
	 *//*
	public void policyStart(Policy policy) {

		if (actionIsCommand(policy)
				&& !(Boolean) this.actualPolicyList.get(policy)) {
			ResourceManager.getInstance().getZolertiaData().sendZolertiaCommand(CommandFormat.getTemperatureCommand(policy.getCondition(), policy.getConditionParam(), action));
			boolean isRunning = true;
			actualPolicyList.remove(policy);
			addPolicy(policy, isRunning);
		}
	}

	*//**
	 * Change the policy state to "waiting"
	 * 
	 * @param policy
	 *//*
	public void policyStop(Policy policy) {

		if (actionIsCommand(policy)
				&& !(Boolean) this.actualPolicyList.get(policy)) {
			ResourceManager.getInstance().getZolertiaData().stopCommand(policy);
			boolean isRunning = false;
			actualPolicyList.remove(policy);
			addPolicy(policy, isRunning);
		}
	}*/

}
