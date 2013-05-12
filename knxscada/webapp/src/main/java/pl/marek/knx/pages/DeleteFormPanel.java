package pl.marek.knx.pages;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.json.simple.JSONObject;

import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;

@HtmlFile("delete_form_panel.html")
public class DeleteFormPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Object object;

	private Label message;
	private DeleteBehavior deleteBehavior;
	
	public DeleteFormPanel(String id, DatabaseManager dbManager, Object object) {
		super(id, dbManager);
		this.object = object;
		setOutputMarkupId(true);
		loadComponents();
	}
	
	private void loadComponents(){
		
		deleteBehavior = new DeleteBehavior();
		
		message = new Label("delete-message", new Model<String>(getMessage())){

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("callback", deleteBehavior.getCallbackUrl());
				super.onComponentTag(tag);
			}
		};
		
		add(deleteBehavior);
		add(message);
	}
	
	private String getMessage(){
		String message = "";
		
		if(object instanceof Project){
			Project project = (Project)object;
			String msg = getString("delete-project-confirmation-message");
			message = String.format(msg, project.getName());
		} else if(object instanceof SubLayer){
			SubLayer subLayer = (SubLayer)object;
			String msg = getString("delete-sublayer-confirmation-message");
			message = String.format(msg, subLayer.getName());
		} else if(object instanceof Layer){
			Layer layer = (Layer)object;
			String msg = getString("delete-layer-confirmation-message");
			message = String.format(msg, layer.getName());
		} else if(object instanceof Element){
			Element element = (Element)object;
			String msg = getString("delete-element-confirmation-message");
			message = String.format(msg, element.getName());
		}
		
		return message;
	}
		
	private class DeleteBehavior extends AbstractDefaultAjaxBehavior{

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected void respond(AjaxRequestTarget target) {

			RequestCycle requestCycle = RequestCycle.get();
			JSONObject obj = new JSONObject();
			
			if(object instanceof Project){
				Project project = (Project)object;
			
				ProjectChooserPanel panel = getProjectChooserPanel();
				String projectId = panel.getProjectItemIdByProject(project);
				
				obj.put("type", "project");
				obj.put("remove", projectId);
				
				Project currProject = getCurrentProject();
				if(currProject != null){
					if(currProject.getId() == project.getId()){
						obj.put("select", "other");
					}else{
						obj.put("select", "");
					}
				}else{
					obj.put("select", "");
				}	
				getDBManager().removeProject(project);
			
			}else if(object instanceof SubLayer){
				SubLayer subLayer = (SubLayer)object;
								
				getDBManager().removeSubLayer(subLayer);
				
				SubLayersPanel panel = getSubLayersPanel();
				String subLayerId = panel.getSubLayerItemIdBySubLayer(subLayer);
				String other = panel.getOtherSubLayerItemIdByRemovedSubLayer(subLayer);

				obj.put("type", "sublayer");
				obj.put("remove", subLayerId);
				obj.put("select", other);
				
			}else if(object instanceof Layer){
				Layer layer = (Layer)object;
				getDBManager().removeLayer(layer);
				
				SideBarPanel panel = getSideBarPanel();
				String layerId = panel.getLayerItemIdByLayer(layer);
				String other = panel.getOtherLayerItemIdByRemovedLayer(layer);
				
				obj.put("type", "layer");
				obj.put("remove", layerId);
				obj.put("select", other);

			} else if(object instanceof Element){
				Element element = (Element)object;
				getDBManager().removeElement(element);
				
				ElementsPanel panel = getElementsPanel();
				String layerId = panel.getElementItemIdByElement(element);
				
				obj.put("type", "element");
				obj.put("remove", layerId);
			}
			
			requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler(obj.toJSONString()));
		}	
	}
}
