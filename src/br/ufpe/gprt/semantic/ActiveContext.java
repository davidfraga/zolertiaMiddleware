package br.ufpe.gprt.semantic;

import java.util.ArrayList;

import br.ufpe.gprt.eventmanager.Subscription;

public class ActiveContext {
	
	private Context s_context;
	private ArrayList<String> s_interestedSubscribers;
	
	public Context getContext(){
		return s_context;
	}
	
	public ActiveContext(Context context){
		s_context = context;
		s_interestedSubscribers = new ArrayList<String>();
	}
		
	
	public ArrayList<Policy> getPolicies (){
		return  s_context.getPolicies();
	}
	
	public ArrayList<String> getInterestedSubscribers (){
		return s_interestedSubscribers;
	}
	
	public void addInterestedSubscriber(String endpoint){
		s_interestedSubscribers.add(endpoint);
	}
	
	public boolean containSubscriber (String endpoint){
		return s_interestedSubscribers.contains(endpoint);
	}
	
	public void removeSubscriber (String subscriber){
		s_interestedSubscribers.remove(subscriber);
	}

	public String getTopic() {
		return s_context.getTopic();
	}
	
	
}
