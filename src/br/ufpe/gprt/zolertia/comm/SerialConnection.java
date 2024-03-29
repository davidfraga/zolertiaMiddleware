/*
 * Copyright (c) 2008, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * $Id: SerialConnection.java,v 1.1 2010/11/03 14:53:05 adamdunkels Exp $
 *
 * -----------------------------------------------------------------
 *
 * SerialConnection
 *
 * Authors : Joakim Eriksson, Niclas Finne
 * Created : 5 jul 2008
 * Updated : $Date: 2010/11/03 14:53:05 $
 *           $Revision: 1.1 $
 */

package br.ufpe.gprt.zolertia.comm;
import java.io.PrintWriter;

import br.ufpe.gprt.zolertia.impl.ZolertiaListener;

/**
 *
 */
public abstract class SerialConnection {

  protected final ZolertiaListener listener;

  protected boolean isSerialOutputSupported = true;

  protected String comPort;
  protected boolean isOpen;
  protected boolean isClosed = true;
  protected String lastError;

  protected PrintWriter serialOutput;

  protected SerialConnection(ZolertiaListener listener) {
    this.listener = listener;
  }

  public boolean isMultiplePortsSupported() {
    return false;
  }

  public void setSerialOutputSupported(boolean isSerialOutputSupported) {
    this.isSerialOutputSupported = isSerialOutputSupported;
  }

  public boolean isSerialOutputSupported() {
    return isSerialOutputSupported;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public abstract String getConnectionName();

  public String getComPort() {
    return comPort;
  }

  public void setComPort(String comPort) {
    this.comPort = comPort;
  }

  public String getLastError() {
    return lastError;
  }

  protected PrintWriter getSerialOutput() {
    return serialOutput;
  }

  protected void setSerialOutput(PrintWriter serialOutput) {
    this.serialOutput = serialOutput;
  }

  public void writeSerialData(String data) {
    PrintWriter serialOutput = this.serialOutput;
    if (serialOutput != null) {
      serialOutput.println(data);
      serialOutput.flush();
    }
  }

  public abstract void open(String comPort);

  public final void close() {
    isClosed = true;
    lastError = null;
    closeConnection();
  }

  protected final void closeConnection() {
    isOpen = false;
    if (serialOutput != null) {
      serialOutput.close();
      serialOutput = null;
    }
    doClose();
    //serialClosed();
  }

  protected abstract void doClose();

  protected final void serialData(String line) {
    listener.serialData(this, line);
  }

/*  protected final void serialOpened() {
    listener.serialOpened(this);
  }

  protected final void serialClosed() {
    listener.serialClosed(this);
  }*/

}
