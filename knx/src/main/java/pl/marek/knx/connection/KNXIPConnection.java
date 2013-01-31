package pl.marek.knx.connection;

import java.net.UnknownHostException;

import pl.marek.knx.interfaces.KNXConnection;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessListener;

public class KNXIPConnection implements KNXConnection {

	private KNXNetworkLink connection;
	private KNXConnectionSettings settings;
	private ProcessCommunicatorImpl processCommunicator;

	public KNXIPConnection(KNXConnectionSettings settings) {
		this.settings = settings;
	}

	@Override
	public boolean connect() throws KNXException, UnknownHostException {
		if (settings.isDiscoverWhileConnecting()) {
			discover();
		}
		return connectKNX();
	}

	private boolean connectKNX() throws KNXException, UnknownHostException {
		connection = new KNXNetworkLinkIP(
				settings.getServiceMode(),
				settings.getLocalEndPoint(), 
				settings.getRemoteEndPoint(),
				settings.isUseNAT(), 
				settings.getMediumSettings());
		createProcessCommunicator();
		return isConnected();
	}
	
	private void discover() throws UnknownHostException, KNXException{
		KNXDiscoverer discoverer = new KNXDiscoverer(settings);
		SearchResponse result = discoverer.discoverOne(settings.getDiscoverTimeout());
		if(result != null){
			String ip = result.getControlEndpoint().getAddress().getHostAddress();
			int port = result.getControlEndpoint().getPort();
			settings.setRemoteEndPoint(ip, port);
		}
	}

	@Override
	public boolean disconnect() {
		if (connection.isOpen()) {
			connection.close();
		}

		return isConnected();
	}

	@Override
	public boolean isConnected() {
		if (connection.isOpen()) {
			return true;
		}
		return false;
	}

	@Override
	public KNXNetworkLink getConnection() {
		return connection;
	}
	
	private void createProcessCommunicator() throws KNXLinkClosedException {
		processCommunicator = new ProcessCommunicatorImpl(connection);
	}

	@Override
	public ProcessCommunicator getProcessCommunicator() {
		return processCommunicator;
	}

	@Override
	public void addProcessListener(ProcessListener processListener) {
		((ProcessCommunicatorImpl) processCommunicator)
				.addProcessListener(processListener);
	}

	@Override
	public void addLinkListener(NetworkLinkListener linkListener) {
		connection.addLinkListener(linkListener);
	}
}