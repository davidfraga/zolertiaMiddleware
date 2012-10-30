/**
 * Subscription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.ufpe.gprt.eventmanager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.ufpe.gprt.resources.ResourceManager;

//import eu.linksmart.eventmanager.Part;

public class Subscription implements java.io.Serializable {
	private java.lang.String topic;

	private java.lang.String endpoint;
	private int port;

	private br.ufpe.gprt.eventmanager.Part[] parts;

	private int numberOfRetries = 3;

	private java.util.Calendar dateTime;
	
	private EventCycle cycle;

	public Subscription() {
	}

	public Subscription(java.lang.String topic, java.lang.String endpoint,
			br.ufpe.gprt.eventmanager.Part[] parts, int numberOfRetries,
			java.util.Calendar dateTime) {
		this.topic = topic;
		String host[] = endpoint.split(":");
		this.endpoint = host[0];
		this.port = Integer.parseInt(host[1]);
		this.parts = parts;
		this.numberOfRetries = numberOfRetries;
		this.dateTime = dateTime;
	}

	/**
	 * Gets the topic value for this Subscription.
	 * 
	 * @return topic
	 */
	public java.lang.String getTopic() {
		return topic;
	}

	/**
	 * Sets the topic value for this Subscription.
	 * 
	 * @param topic
	 */
	public void setTopic(java.lang.String topic) {
		this.topic = topic;
	}

	/**
	 * Gets the endpoint value for this Subscription.
	 * 
	 * @return endpoint
	 */
	public java.lang.String getEndpoint() {
		return endpoint;
	}

	/**
	 * Sets the endpoint value for this Subscription.
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(java.lang.String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Gets the parts value for this Subscription.
	 * 
	 * @return parts
	 */
	public br.ufpe.gprt.eventmanager.Part[] getParts() {
		return parts;
	}

	/**
	 * Sets the parts value for this Subscription.
	 * 
	 * @param parts
	 */
	public void setParts(br.ufpe.gprt.eventmanager.Part[] parts) {
		this.parts = parts;
	}

	public br.ufpe.gprt.eventmanager.Part getParts(int i) {
		return this.parts[i];
	}

	public void setParts(int i, br.ufpe.gprt.eventmanager.Part _value) {
		this.parts[i] = _value;
	}

	/**
	 * Gets the numberOfRetries value for this Subscription.
	 * 
	 * @return numberOfRetries
	 */
	public int getNumberOfRetries() {
		return numberOfRetries;
	}

	/**
	 * Sets the numberOfRetries value for this Subscription.
	 * 
	 * @param numberOfRetries
	 */
	public void setNumberOfRetries(int numberOfRetries) {
		this.numberOfRetries = numberOfRetries;
	}

	/**
	 * Gets the dateTime value for this Subscription.
	 * 
	 * @return dateTime
	 */
	public java.util.Calendar getDateTime() {
		return dateTime;
	}

	/**
	 * Sets the dateTime value for this Subscription.
	 * 
	 * @param dateTime
	 */
	public void setDateTime(java.util.Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public void sendData() {
		Socket socket;
		for (int i = 0; i < numberOfRetries; i++) {
			// open socket and stream
			try {
				socket = new Socket(endpoint, port);
				DataOutputStream dataOutputStream = new DataOutputStream(
						socket.getOutputStream());

				// write reports
				String data = ResourceManager.getInstance().extractReport(this.parts);
				data += this.dateTime.getTimeInMillis();
				dataOutputStream.writeBytes(data);
				dataOutputStream.write("\n".getBytes());
				dataOutputStream.flush();
				// close socket and stream
				dataOutputStream.close();
				socket.close();
				System.out.println("Subscription: DATA SENT");
				break;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

}
