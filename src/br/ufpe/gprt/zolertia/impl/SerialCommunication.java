package br.ufpe.gprt.zolertia.impl;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.TooManyListenersException;


public class SerialCommunication implements SerialPortEventListener {

	// private static final Logger LOG = LoggerFactory
	// .getLogger(ArduinoAggregator.class.getName());

	SerialPort serialPort;

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */

	protected PrintWriter serialOutput;

	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;

	private String buffer = "";

	private Set<ZolertiaListener> listeners;

	public SerialCommunication(String portName) {

		if ("".equals(portName)) {
			throw new IllegalArgumentException("portName must be provided!");
		}

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			throw new IllegalArgumentException("Could not open ComPort "
					+ portName);
		}

		// open serial port, and use class name for the appName.
		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			// LOG.debug("Listening on port " + portName);
		} catch (PortInUseException e) {
			throw new IllegalArgumentException(
					"Serial Port in use. On Mac OS X, ", e);
		} catch (UnsupportedCommOperationException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		} catch (TooManyListenersException e) {
			throw new IllegalArgumentException(
					"Too many listeners for serial port "
							+ serialPort.getName(), e);
		}

		this.listeners = new HashSet<ZolertiaListener>();

	}

	public void stopSerialConnection() {
		flush();
		close();
	}

	public void serialWrite(String command) {
		/*
		 * byte[] b = command.getBytes(); try { output.write(b); output.flush();
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// System.console().writer().write(command);

		/*PrintWriter serialOutput = new PrintWriter(output);
		if (serialOutput != null) {
			serialOutput.print(command);
			serialOutput.flush();
		}*/
		
		try {
			output.write(command.getBytes());
			Thread.sleep(100);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void flush() {
		try {
			output.flush();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		System.out.println("SERIAL EVENT");
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			System.out.println("DATA AVALIABLE");
			
			try {
				//available = input.available();
				//byte chunk[] = new byte[available];
				//input.read(chunk, 0, available);
				// Displayed results are codepage dependent
				// = new String(chunk);


				String dataFormatted = "";
				final BufferedReader br = new BufferedReader(
						new InputStreamReader(input));
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println("Line: "+line);
					SensorData sd = SensorData.parseSensorData(line);
					dataFormatted = "";//+= sd.getNodeID() + " - Temperatura: "+ sd.getTemperature() +"\n";
					//System.out.println("sensor: "+dataFormatted);
					for (ZolertiaListener item : listeners) {
						/*
						 * float ans =
						 * Float.parseFloat(sensorResult.split(" = ")[1]);
						 * item.updateTemperature(ans);
						 * System.out.println("DATA="+ans);
						 */
						item.updateTemperature(dataFormatted);
					}
					br.close();
				}
				
				System.out.println("Dados dos sensores:\n"+dataFormatted);
				
			}
			

			 catch (IOException e) {
				// LOG.error("Cannot read from Serial Port.", e);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	private void publishSensorEvent(String key, String value) {
		if (key.startsWith("t")) {
			// float degreesCelsius = Float.parseFloat(value);
			for (ZolertiaListener listener : listeners) {
				listener.updateTemperature(value);
			}
		}
	}

	public void addListener(ZolertiaListener listener) {
		this.listeners.add(listener);
	}
}
