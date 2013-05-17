package pl.marek.knx;

import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.web.WebServerSettings;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebAppActivity extends Activity{
	
	private Project project;
	private DatabaseManager dbManager;
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp);
	
		dbManager = new DatabaseManagerImpl(this);
		
		Project intentProject = getIntent().getParcelableExtra(Project.PROJECT);
		project = dbManager.getProjectByIdWithDependencies(intentProject.getId());
		
		webView = (WebView)findViewById(R.id.webview);
		
		webView.loadUrl(createProjectUrl());
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebAppClient());
		
	}
	
	private String createProjectUrl(){
		String url = "";
		WebServerSettings settings  = new WebServerSettings(this);
		
		url = String.format("http://localhost:%d/?project=%d", settings.getPort(), project.getId());
		
		return url;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dbManager != null && !dbManager.isOpen()){
			dbManager.open();
		}
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		if(dbManager != null && dbManager.isOpen()){
			dbManager.close();
		}
	}
	
	private class WebAppClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			webView.loadUrl(url);
			return true;
		}
	}
}
