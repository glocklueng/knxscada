package pl.marek.knx.connection;

import java.net.UnknownHostException;
import pl.marek.knx.connection.KNXConnectionSettings;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.Discoverer;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;

public class KNXDiscoverer extends Discoverer{
	
    
	public KNXDiscoverer(KNXConnectionSettings settings) throws UnknownHostException, KNXException{	
		super(settings.getLocalEndPoint().getAddress(), settings.getLocalEndPoint().getPort(), settings.isUseNAT());
	}
	
	public SearchResponse[] discover(int timeout) throws KNXException{
		clearSearchResponses();
		startSearch(timeout, true);
		return getSearchResponses();
	}
	
	public SearchResponse discoverOne(int timeout) throws KNXException{
		startSearch(timeout);
		while(isSearching()){
			SearchResponse[] responses = getSearchResponses();
			if(responses.length > 0){
				stopSearch();
			}
		}
		if(getSearchResponses() != null && getSearchResponses().length > 0){
			return getSearchResponses()[0];
		}
		return null;
	}
	
	public void startSearch(int timeout) throws KNXException{
		clearSearchResponses();
		startSearch(timeout, false);
	}
}
