package pl.marek.knx.interfaces;

public interface WebServerController {
	
	public static final String START_WEBSERVER = "pl.marek.knx.start_webserver";
	public static final String STOP_WEBSERVER = "pl.marek.knx.stop_webserver";
	public static final String DEPLOY_WEBAPP = "pl.marek.knx.deploy_webapp";
	public static final String UNDEPLOY_WEBAPP = "pl.marek.knx.undeploy_webapp";

	public void start();
	public void stop();
	public void deployWebApp();
	public void undeployWebApp();
}
