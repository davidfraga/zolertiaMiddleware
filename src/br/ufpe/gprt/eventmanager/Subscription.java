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
import java.util.Timer;

/**
 * Subscription - this class store the information from the sensors and from the
 * client
 * 
 * @author GPRT-BEMO
 * 
 */
public class Subscription implements Runnable{
	public static final int PERIOD_DEFAULT = 5000;
	public static final int PERIOD_MIN = 3000;
	public static final int PERIOD_MAX = 10000;

	private java.lang.String topic;

	private java.lang.String endpoint;
	private int port;

	private int numberOfRetries = 3;

	private int period;

	private String data;

	private boolean started;

	public Subscription(String topic, String endpoint) {
		this.topic = topic;
		String[] data = endpoint.split(":");
		this.endpoint = data[0];
		this.port = Integer.parseInt(data[1]);
		started = true;
		setPeriod(PERIOD_DEFAULT);
	}

	public synchronized void setStatus(boolean start) {
		started = start;
		setPeriod(PERIOD_DEFAULT);
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
		return endpoint + ":" + port;
	}

	/**
	 * Sets the endpoint value for this Subscription.
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(java.lang.String endpoint) {
		String host[] = endpoint.split(":");
		this.endpoint = host[0];
		this.port = Integer.parseInt(host[1]);
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
	
	public synchronized void setPeriod(int period){
		if (this.period != period) {
			this.period = period;
			System.out.println(endpoint + ":" + port + " set to period: "+period);
		}
	}

	public class Sender implements Runnable {
		String msg;

		public Sender(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			System.out.println("SUB - "+endpoint + ":" + port + " -> " + msg);
			Socket socket;
			for (int i = 0; i < numberOfRetries; i++) {
				// open socket and stream
				try {
					socket = new Socket(endpoint, port);
					DataOutputStream dataOutputStream = new DataOutputStream(
							socket.getOutputStream());

					dataOutputStream.writeUTF(msg);
					dataOutputStream.flush();
					dataOutputStream.close();
					socket.close();
					System.out.println("Subscription: DATA SENT to " + endpoint
							+ ":" + port);

					break;
				} catch (UnknownHostException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}

		}

	}

	public synchronized void storeData(String msg) {
		this.data = msg;
	}

	@Override
	public void run() {
		while (started) {
			if (data != null) {
				Thread send = new Thread(new Sender(this.data));
				send.start();
				//data = null;
			}
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
