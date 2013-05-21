package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/imagebutton.html")
public class ImageButton extends CompoundButton{

	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer imageButtonContainer;
	private StaticImage onImage;
	private StaticImage offImage;
	
	private Model<String> onImageModel;
	private Model<String> offImageModel;
	
	public ImageButton(String id) {
		super(id);
		setOutputMarkupId(true);
		load();
	}

	private void load(){
		removeAll();
		
		imageButtonContainer = new WebMarkupContainer("image-button"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				updateComponentTag(tag);
				super.onComponentTag(tag);
			}
		};
		onImageModel = new Model<String>("");
		offImageModel = new Model<String>("");
		
		
		onImage = new StaticImage("image-on", onImageModel);
		offImage = new StaticImage("image-off", offImageModel);
		imageButtonContainer.add(onImage);
		imageButtonContainer.add(offImage);
		add(imageButtonContainer);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
//		tag.put("style", "position: absolute; right: 3px;");
		super.onComponentTag(tag);
	}
	
	protected void setOnImage(String path){
		onImageModel.setObject(path);
	}
	
	protected void setOffImage(String path){
		offImageModel.setObject(path);
	}

	@Override
	public void refresh() {
		load();
	}
}