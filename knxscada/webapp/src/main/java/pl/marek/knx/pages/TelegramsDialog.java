package pl.marek.knx.pages;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("telegrams.html")
public class TelegramsDialog extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	public TelegramsDialog(String id, DBManager dbManager) {
		super(id, dbManager);
	}
	
	

}
