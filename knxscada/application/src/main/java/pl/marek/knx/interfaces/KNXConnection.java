package pl.marek.knx.interfaces;

import java.net.UnknownHostException;

import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessListener;

public interface KNXConnection {
	
	public boolean connect() throws KNXException, UnknownHostException;
	public boolean disconnect();
	public boolean isConnected();
	public KNXNetworkLink getConnection();
	public ProcessCommunicator getProcessCommunicator();
	public void addProcessListener(ProcessListener processListener);
	public void addLinkListener(NetworkLinkListener linkListener);

}