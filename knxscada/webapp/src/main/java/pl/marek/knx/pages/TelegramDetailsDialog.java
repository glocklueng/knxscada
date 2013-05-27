package pl.marek.knx.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.utils.DataRepresentation;
import pl.marek.knx.utils.DateConversion;

public class TelegramDetailsDialog extends WebMarkupContainer{

	private static final long serialVersionUID = 1L;
	
	private Telegram telegram;
	
	public TelegramDetailsDialog(String id) {
		super(id);
		setOutputMarkupId(true);
		loadComponents();
	}
	
	public void setTelegram(Telegram telegram){
		this.telegram = telegram;
		refresh();
	}
	
	private void loadComponents(){
		
		Label id = new Label("telegram-id", Model.of(""));
		Label time = new Label("telegram-time", Model.of(""));
		Label priority = new Label("telegram-priority", Model.of(""));
		Label srcAddr = new Label("telegram-source-address", Model.of(""));
		Label dstAddr = new Label("telegram-destination-address", Model.of(""));
		Label hopcount = new Label("telegram-hopcount", Model.of(""));
		Label type = new Label("telegram-type", Model.of(""));
		Label msgcode = new Label("telegram-msgcode", Model.of(""));
		Label rawdata = new Label("telegram-rawdata", Model.of(""));
		Label data = new Label("telegram-data", Model.of(""));
		Label rawframe = new Label("telegram-rawframe", Model.of(""));
		Label framelength = new Label("telegram-framelength", Model.of(""));
		Label dptid = new Label("telegram-dptid", Model.of(""));
		Label ack = new Label("telegram-ack", Model.of(""));
		Label confirmation = new Label("telegram-confirmation", Model.of(""));
		Label repeated = new Label("telegram-repeated", Model.of(""));
		
		if(telegram != null){
			id.setDefaultModelObject(String.valueOf(telegram.getId()));
			time.setDefaultModelObject(DateConversion.getDateTimeString(telegram.getTime()));
			priority.setDefaultModelObject(telegram.getPriority());
			srcAddr.setDefaultModelObject(telegram.getSourceAddress());
			dstAddr.setDefaultModelObject(telegram.getDestinationAddress());
			hopcount.setDefaultModelObject(String.valueOf(telegram.getHopcount()));
			type.setDefaultModelObject(telegram.getType());
			msgcode.setDefaultModelObject(telegram.getMsgCode());
			rawdata.setDefaultModelObject(DataRepresentation.byteArrayToHexString(telegram.getRawdata()));
			data.setDefaultModelObject(telegram.getData());
			rawframe.setDefaultModelObject(DataRepresentation.byteArrayToHexString(telegram.getRawframe()));
			framelength.setDefaultModelObject(String.valueOf(telegram.getFrameLength()));
			dptid.setDefaultModelObject(telegram.getDptId());
			ack.setDefaultModelObject(String.valueOf(telegram.getFlags().isAck()));
			confirmation.setDefaultModelObject(String.valueOf(telegram.getFlags().isConfirmation()));
			repeated.setDefaultModelObject(String.valueOf(telegram.getFlags().isRepeated()));
		}

		add(id);
		add(time);
		add(priority);
		add(srcAddr);
		add(dstAddr);
		add(hopcount);
		add(type);
		add(msgcode);
		add(rawdata);
		add(data);
		add(rawframe);
		add(framelength);
		add(dptid);
		add(ack);
		add(confirmation);
		add(repeated);
	}
	
	public void refresh(){
		removeAll();
		loadComponents();
	}
}
