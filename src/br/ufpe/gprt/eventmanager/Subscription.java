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
public class Subscription implements Runnable {
	public static final int PERIOD_DEFAULT = 5000;
	public static final int PERIOD_MIN = 3000;
	public static final int PERIOD_MAX = 10000;
	public static final int MAX_NUM_OF_RETRIES = 9;
	public static final int ITERATIONS_TO_REPLY = 3;
	private double lastMomentSent;

	private java.lang.String topic;

	private java.lang.String endpoint;
	private int port;

	private int period;

	private String data;

	private Status status;

	public Subscription(String topic, String endpoint) {
		this.topic = topic;
		String[] data = endpoint.split(":");
		this.endpoint = data[0];
		this.port = Integer.parseInt(data[1]);
		status = Status.STARTED;
		setPeriod(PERIOD_DEFAULT);
		lastMomentSent = 0;
	}

	public synchronized void setStatus(Status status) {
		this.status = status;
		System.out.println("SUB: status changed to " + this.status);
	}

	public Status getStatus() {
		return status;
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

	public synchronized void setPeriod(int period) {
		if (this.period != period) {
			this.period = period;
			System.out.println(endpoint + ":" + port + " set to period: "
					+ period);
		}
	}

	public class Sender implements Runnable {

		String msg;

		public Sender(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			System.out.println("SUB - " + endpoint + ":" + port + " -> " + msg);
			Socket socket;
			boolean sent = false;
			for (int i = 0; i < MAX_NUM_OF_RETRIES / ITERATIONS_TO_REPLY; i++) {
				for (int j = 0; j < MAX_NUM_OF_RETRIES / ITERATIONS_TO_REPLY; j++) {
					// open socket and stream
					try {
						socket = new Socket(endpoint, port);
						DataOutputStream dataOutputStream = new DataOutputStream(
								socket.getOutputStream());

						dataOutputStream.writeUTF(msg);
						dataOutputStream.flush();
						dataOutputStream.close();
						socket.close();
						System.out.println("Subscription: DATA SENT to "
								+ endpoint + ":" + port);
						sent = true;
						lastMomentSent = System.currentTimeMillis() / 1000.0
								- lastMomentSent;
						setStatus(Status.STARTED);
						break;
					} catch (UnknownHostException e) {
						System.out.println("SUB:UHE " + e.getMessage());						
					} catch (IOException e) {
						System.out.println("SUB:IOE " + e.getMessage());						
					}
				}
				if (sent) break;
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!sent) setStatus(Status.DEAD);

		}

	}

	public synchronized void storeData(String msg) {
		this.data = msg;
	}

	@Override
	public void run() {
		while (status != Status.DEAD) {
			if (status == Status.STARTED) {
				if (data != null) {
					setStatus(Status.WAITING);
					Thread send = new Thread(new Sender(this.data));
					send.start();
					// data = null;
				}

			}

			try {
				Thread.sleep(this.period);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public enum Status {
		DEAD, STARTED, WAITING
	}

	public void setAction(boolean defaultMode) {
		if (defaultMode) {
			setStatus(Status.STARTED);
			setPeriod(PERIOD_DEFAULT);
		} else {
			setStatus(Status.WAITING);
		}
	}
}
