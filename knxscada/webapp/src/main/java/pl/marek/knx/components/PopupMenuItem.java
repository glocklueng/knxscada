package pl.marek.knx.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.pages.BasePanel;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/popupmenuitem.html")
public class PopupMenuItem extends BasePanel{

	private static final long serialVersionUID = 1L;

	private Label nameLabel;
	private StaticImage icon;
	
	private Model<String> nameModel;
	private Model<String> iconModel;
	
	public PopupMenuItem(String id, Model<String> name, Model<String> icon) {
		super(id);
		this.nameModel = name;
		this.iconModel = icon;
		loadComponents();
	}
	
	private void loadComponents(){
		icon = new StaticImage("popup-menu-item-icon", iconModel);
		nameLabel = new Label("popup-menu-item-name", nameModel);
		
		add(icon);
		add(nameLabel);
	}
}
