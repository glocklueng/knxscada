package pl.marek.knx.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import org.json.simple.JSONObject;

import pl.marek.knx.pages.AjaxBehavior;
import pl.marek.knx.utils.JSONUtil;

public abstract class CompoundButton extends Panel{

	private static final long serialVersionUID = 1L;
	
	protected OnChangeListener onChangeListener;
	protected OnSwitchClickEvent switchClickEvent;
	protected boolean checked;
	
	public CompoundButton(String id) {
		super(id);
		checked = false;
		switchClickEvent = new OnSwitchClickEvent();
		add(switchClickEvent);

	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean state) {
		this.checked = state;
		if(onChangeListener != null){
			onChangeListener.onChange(isChecked());
		}
	}
	
	protected String getStateText(){
		String text = "";
		if(isChecked()){
			text = getString("on");
		}else{
			text = getString("off");
		}
		return text;
	}

	public String getCallbackUrl() {
		return switchClickEvent.getCallbackUrl().toString();
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public interface OnChangeListener{
		public void onChange(boolean state);
	}
	
	protected class OnSwitchClickEvent extends AjaxBehavior{

		private static final long serialVersionUID = 1L;
		
		@Override
		protected void respond(AjaxRequestTarget target) {
			
			String msg = getRequestMessage();
	
			JSONObject obj = JSONUtil.convertStringToObject(msg);
			
			String state = (String)obj.get("state");
			if("on".equals(state)){
				setChecked(true);
			}else{
				setChecked(false);
			}
		}
	}
	
	protected void updateComponentTag(ComponentTag tag){
		String currClass = tag.getAttribute("class");
		if(isChecked()){
			tag.put("class", String.format("%s compound-button-on", currClass));
		}else{
			tag.put("class", String.format("%s compound-button-off", currClass));
		}
		tag.put("callback", getCallbackUrl());
	}
	
	public abstract void refresh();

}
