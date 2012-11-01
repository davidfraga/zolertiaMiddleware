/**
 * EventManagerPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
/**
 * Interface that provide the service integrated with the linksmart networkmanager
 * It'll to be accessible at client side
 * @Autor David Fraga
 */
package br.ufpe.gprt.eventmanager;

public interface EventManagerPort extends java.rmi.Remote {
	public static final String DESCRIPTION = "zolertia event communication";
    public static final String SID = "ZolertiasService";
	
    public boolean subscribe(java.lang.String topic, java.lang.String endpoint) throws java.rmi.RemoteException;
    public boolean unsubscribe(java.lang.String topic, java.lang.String endpoint) throws java.rmi.RemoteException;
    public String poll(String topic) throws java.rmi.RemoteException;
}
