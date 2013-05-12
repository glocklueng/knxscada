package pl.marek.knx.pages;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.interfaces.DatabaseManager;

@HtmlFile("telegrams.html")
public class TelegramsDialog extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	public TelegramsDialog(String id, DatabaseManager dbManager) {
		super(id, dbManager);
	}
}
