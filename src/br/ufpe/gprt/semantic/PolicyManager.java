package br.ufpe.gprt.semantic;
import java.util.ArrayList;

public class PolicyManager {
	
	private static ArrayList<Policy> s_policyList;
		
	public static int addPolicy (Policy newPolicy ) {
		s_policyList.add(newPolicy);
		return (s_policyList.size() - 1 );
	}
	
	public static Policy getPolicyByIndex (int index) {
		return s_policyList.get(index);
	}
	
	public static boolean containPolicy (Policy policy) {
		return s_policyList.contains(policy);
	}
	
		
	public static int searchPolicy (Policy policy){
		return s_policyList.indexOf(policy);
	}
		
		
	public PolicyManager () {
		s_policyList = new ArrayList<Policy>(); 
		
	}
	
}
