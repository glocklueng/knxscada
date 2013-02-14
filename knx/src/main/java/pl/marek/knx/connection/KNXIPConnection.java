package pl.marek.knx.connection;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import android.util.Log;

import pl.marek.knx.interfaces.KNXConnection;
import pl.marek.knx.log.LogTags;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
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
		Log.d(LogTags.KNX_CONNECTION, String.format("%d %s %s %s %s %s",settings.getServiceMode(), settings.getLocalEndPoint(),settings.getRemoteEndPoint(),settings.isUseNAT(), settings.getMediumSettings(), settings));
		
		int serviceMode = settings.getServiceMode();
		InetSocketAddress local = settings.getLocalEndPoint();
		InetSocketAddress remote = settings.getRemoteEndPoint();
		boolean useNAT = settings.isUseNAT();
		KNXMediumSettings tpSet = settings.getMediumSettings();

		connection = new KNXNetworkLinkIP(
				serviceMode,
				local, 
				remote,
				useNAT, 
				tpSet);
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
		if(connection != null){
			if (connection.isOpen()) {
				connection.close();
			}
		}
		return isConnected();
	}

	@Override
	public boolean isConnected() {
		if(connection != null){
			if (connection.isOpen()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public KNXNetworkLink getConnection() {
		return connection;
	}
	
	private void createProcessCommunicator() throws KNXLinkClosedException {
		processCommunicator = new ProcessCommunicatorImpl(connection);
		processCommunicator.setResponseTimeout(settings.getTimeout());
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