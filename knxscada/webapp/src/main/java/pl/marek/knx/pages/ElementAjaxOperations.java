package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.json.simple.JSONObject;

import pl.marek.knx.database.Element;
import pl.marek.knx.utils.JSONUtil;

public class ElementAjaxOperations extends AjaxBehavior{

	private static final long serialVersionUID = 1L;
	
	private BasePanel panel;
	
	public ElementAjaxOperations(BasePanel panel){
		this.panel = panel;
	}

	@Override
	protected void respond(AjaxRequestTarget target) {
		
		String msg = getRequestMessage();
		JSONObject obj = JSONUtil.convertStringToObject(msg);
		
		int id = JSONUtil.convertJSONValueToInt(obj, "id");
		String type = (String)obj.get("type");
		Element element = panel.getDBManager().getElementById(id);
		
		if(element != null){
			if("update".equals(type)){
				int x = JSONUtil.convertJSONValueToInt(obj, "x");
				int y = JSONUtil.convertJSONValueToInt(obj, "y");
				element.setX(x);
				element.setY(y);
			}else if("remove".equals(type)){
				element.setNotVisualisationElement();
			}
			panel.getDBManager().updateElement(element);
		}
	}
}