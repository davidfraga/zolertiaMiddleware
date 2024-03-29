/*
 * In case of German law being applicable to this license agreement, the following warranty and liability terms shall apply:
 *
 * 1. Licensor shall be liable for any damages caused by wilful intent or malicious concealment of defects.
 * 2. Licensor's liability for gross negligence is limited to foreseeable, contractually typical damages.
 * 3. Licensor shall not be liable for damages caused by slight negligence, except in cases 
 *    of violation of essential contractual obligations (cardinal obligations). Licensee's claims for 
 *    such damages shall be statute barred within 12 months subsequent to the delivery of the software.
 * 4. As the Software is licensed on a royalty free basis, any liability of the Licensor for indirect damages 
 *    and consequential damages - except in cases of intent - is excluded.
 *
 * This limitation of liability shall also apply if this license agreement shall be subject to law 
 * stipulating liability clauses corresponding to German law.
 */
package br.ufpe.gprt.eventmanager;

import java.io.Serializable;

import br.ufpe.gprt.eventmanager.EventListener;


public class EventCycle implements Serializable {

	private static final long serialVersionUID = 1306176023187533355L;
	private String name;
	private EventListener startEvent;
	private EventListener stopEvent;
	private String policy;
	private long timeout;

	public String getName() {
		return name;
	}

	public EventListener getStartEvent() {
		return startEvent;
	}

	public EventListener getStopEvent() {
		return stopEvent;
	}
	
	public String getPolicy() {
		return policy;
	}

	public long getTimeout() {
		return timeout;
	}

	public EventCycle(String name, EventListener startEvent, EventListener stopEvent, String policy, long timeout) {
		this.name = name;
		this.startEvent = startEvent;
		this.stopEvent = stopEvent;
		this.timeout = timeout;
		this.policy = policy;
	}

}
