package br.ufpe.gprt.semantic;

import java.util.ArrayList;

import br.ufpe.gprt.eventmanager.Subscription;

public class ActiveContext {
	
	private Context s_context;
	private ArrayList<Subscription> s_interestedSubscribers;
	private boolean isSelfManager = false;
	
	public boolean isSelfManager() {
		return isSelfManager;
	}

	public void setSelfManager(boolean isSelfManager) {
		this.isSelfManager = isSelfManager;
	}

	public Context getContext(){
		return s_context;
	}
	
	public ActiveContext(Context context){
		s_context = context;
		s_interestedSubscribers = new ArrayList<Subscription>();
	}
		
	
	public ArrayList<Policy> getPolicies (){
		return  s_context.getPolicies();
	}
	
	public ArrayList<Subscription> getInterestedSubscribers (){
		return s_interestedSubscribers;
	}
	
	public void addInterestedSubscriber(Subscription endpoint){
		s_interestedSubscribers.add(endpoint);
	}
	
	public boolean containSubscriber(Subscription endpoint){
		return s_interestedSubscribers.contains(endpoint);
	}
	
	public void removeSubscriber (String endpoint){
		for (Subscription item : s_interestedSubscribers) {
			if (item.getEndpoint().equals(endpoint)) s_interestedSubscribers.remove(item);
		}
		
	}

	public String getTopic() {
		return s_context.getTopic();
	}
	
	
}
