package pl.marek.knx.pages;

import java.util.Set;
import javax.servlet.ServletContext;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;

import android.content.Context;

import pl.marek.knx.KNXWebApplication;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;

public abstract class BasePanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	private DatabaseManager dbManager;
	
	public BasePanel(String id, DatabaseManager dbManager) {
		super(id);
		this.dbManager = dbManager;
	}

	protected String getParameter(String key){
		String value = RequestCycle.get().getRequest().getRequestParameters().getParameterValue(key).toString();
		return value;
	}
	
	protected boolean isContainsParameter(String key){
		Set<String> params = RequestCycle.get().getRequest().getRequestParameters().getParameterNames();
		return params.contains(key);
	}
	
	protected boolean isContainsInParameterName(String key){
		Set<String> params = RequestCycle.get().getRequest().getRequestParameters().getParameterNames();
		for(String param: params){
			if(param.contains(key)){
				return true;
			}
		}
		return false;
	}
	
	protected DatabaseManager getDBManager(){
		return dbManager;
	}
	
	protected ServletContext getServletContext(){
		return WebApplication.get().getServletContext();
	}
	
	protected Context getAndroidContext(){
		KNXWebApplication app = (KNXWebApplication)getApplication();
		return app.getAndroidContext();
	}
	
	protected Index getIndexPage(){
		return (Index)getPage();
	}
	
	protected HeaderPanel getHeaderPanel(){
		return getIndexPage().getHeaderPanel();
	}
	
	protected SideBarPanel getSideBarPanel(){
		return getIndexPage().getSideBarPanel();
	}
	
	protected MainPanel getMainPanel(){
		return getIndexPage().getMainPanel();
	}
	
	protected FooterPanel getFooterPanel(){
		return getIndexPage().getFooterPanel();
	}
	
	protected ContentPanel getContentPanel(){
		return getMainPanel().getContentPanel();
	}
	
	protected SubLayersPanel getSubLayersPanel(){
		return getMainPanel().getSubLayersPanel();
	}
	
	protected DialogsPanel getDialogsPanel(){
		return getIndexPage().getDialogsPanel();
	}
	
	protected ProjectChooserPanel getProjectChooserPanel(){
		return getIndexPage().getProjectChooserPanel();
	}
	
	protected ElementsPanel getElementsPanel(){
		return getContentPanel().getElementsPanel();
	}
	
	protected SubLayerSettingsPanel getSubLayerSettingsPanel(){
		return getContentPanel().getSubLayerSettingsPanel();
	}
	
	protected Project getCurrentProject(){
		Project project = null;
		String pId = getParameter("project");
        if(pId != null){
        	try{
	            int projectId = Integer.parseInt(pId);
	            project = getDBManager().getProjectByIdWithDependencies(projectId);
        	}catch(NumberFormatException ex){}
        }
        return project;
	}
	
	protected Layer getCurrentLayer(){
		return getSideBarPanel().getCurrentLayer();
	}
	
	protected SubLayer getCurrentSubLayer(){
		return getSubLayersPanel().getCurrentSubLayer();
	}
}
