package pl.marek.knx.pages;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.interfaces.DatabaseManager;

import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.utils.DateConversion;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("telegrams.html")
public class TelegramsDialog extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private DataView<Telegram> dataView;
	private TelegramDetailsDialog detailsDialog;
	private TelegramFiltersForm filtersForm;
	private WebMarkupContainer telegramsTable;
	
	public TelegramsDialog(String id, DatabaseManager dbManager) {
		super(id, dbManager);
		setOutputMarkupId(true);
		loadComponents();
	}
	
	private void loadComponents(){
		
		telegramsTable = new WebMarkupContainer("telegrams-table");
		telegramsTable.setOutputMarkupId(true);
		
		dataView = new DataView<Telegram>("telegram-row", new TelegramDataProvider()){
			private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<Telegram> item){			            	
                final Telegram telegram = item.getModelObject();
                item.add(new Label("telegram-time", DateConversion.getDateTimeString(telegram.getTime())));
                item.add(new Label("telegram-source", telegram.getSourceAddress()));
                item.add(new Label("telegram-destination", telegram.getDestinationAddress()));
                item.add(new Label("telegram-data", telegram.getData()));
                item.add(new Label("telegram-type", telegram.getType()));
                
                String icon = getIconByPriority(telegram.getPriority());
                String priority = "";
                if(icon.isEmpty()){
                	priority = telegram.getPriority();
                }else{
                	item.add(new Label("telegram-priority-text", priority));
                	item.add(new StaticImage("telegram-priority-img", new Model<String>(icon)));
                }
                item.add(new TelegramClickBehavior(telegram));
            }
        };
        dataView.setOutputMarkupId(true);
        dataView.setItemsPerPage(5);
        telegramsTable.add(dataView);
        add(telegramsTable);
        
		filtersForm = new TelegramFiltersForm("telegrams-filters-form");
        add(filtersForm);
        
		detailsDialog = new TelegramDetailsDialog("telegram-details");
		add(detailsDialog);
	}
	
	private String getIconByPriority(String priority){
		String icon = "";
		
		if("low".equals(priority)){
			icon = "images/telegram_priority_low.png";
		} else if("normal".equals(priority)){
			icon = "images/telegram_priority_normal.png";
		} else if("system".equals(priority)){
			icon = "images/telegram_priority_system.png";
		} else if("urgent".equals(priority)){
			icon = "images/telegram_priority_urgent.png";
		}
		
		return icon;
	}
	
	private class TelegramClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;
		private Telegram telegram;
		
		public TelegramClickBehavior(Telegram telegram) {
			super("click");
			this.telegram = telegram;
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			
			detailsDialog.setTelegram(telegram);
			target.add(detailsDialog);
			target.appendJavaScript("showTelegramDetails();");
			
		}
		
	}
	
	private class TelegramFiltersForm extends Form<Void>{

		private static final long serialVersionUID = 1L;
		
		private TextField<String> sourceAddressField;
		private TextField<String> destinationAddressField;
		private TextField<String> fromDateField;
		private TextField<String> toDateField;
		
		private CheckBox lowPriorityField;
		private CheckBox normalPriorityField;
		private CheckBox urgentPriorityField;
		private CheckBox systemPriorityField;
		
		private CheckBox readTypeField;
		private CheckBox writeTypeField;
		private CheckBox responseTypeField;
		
		private TextField<Integer> numOfTelegramsOnPageField;
		
		private Filters filters;
		
		private AjaxPagingNavigator navigator = new AjaxPagingNavigator("navigator", dataView){

			private static final long serialVersionUID = 1L;

			@Override
        	protected void onAjaxEvent(AjaxRequestTarget target) {
        		target.appendJavaScript("initTelegramFilters();");
        		super.onAjaxEvent(target);
        	}
        };
		
		public TelegramFiltersForm(String id) {
			super(id);
			filters = new Filters();
			setDefaultModel(new CompoundPropertyModel<Filters>(filters));
			
			loadComponents();
		}
		
		private void loadComponents(){
			
			sourceAddressField = new TextField<String>("sourceAddress");
			destinationAddressField = new TextField<String>("destinationAddress");
			fromDateField = new TextField<String>("fromDate");
			toDateField = new TextField<String>("toDate");
			
			lowPriorityField = new CheckBox("lowPriority");
			normalPriorityField = new CheckBox("normalPriority");
			urgentPriorityField = new CheckBox("urgentPriority");
			systemPriorityField = new CheckBox("systemPriority");
			
			
			readTypeField = new CheckBox("readType");
			writeTypeField = new CheckBox("writeType");
			responseTypeField = new CheckBox("responseType");
			
			numOfTelegramsOnPageField = new TextField<Integer>("numOfTelegramsOnPage");
			
			add(sourceAddressField);
			add(destinationAddressField);
			add(fromDateField);
			add(toDateField);
			add(lowPriorityField);
			add(normalPriorityField);
			add(urgentPriorityField);
			add(systemPriorityField);
			add(readTypeField);
			add(writeTypeField);
			add(responseTypeField);
			add(numOfTelegramsOnPageField);
			
			navigator.setOutputMarkupId(true);
			add(navigator);
			
			add(new AjaxButton("telegrams-filters-submit-button") {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {					
					dataView.setItemsPerPage(filters.getNumOfTelegramsOnPage());
					target.add(telegramsTable);
					target.add(navigator);
				}
			});
		}
		
		public Filters getFilters(){
			return filters;
		}
	}
	
	private class TelegramDataProvider implements IDataProvider<Telegram>{

		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {}

		@Override
		public Iterator<? extends Telegram> iterator(long first, long count) {
			String limit = String.format("%d,%d", first, count);
			return getTelegramList(limit).iterator();
		}

		@Override
		public long size() {
			int size = getTelegramList(null).size();
			return size;
		}

		@Override
		public IModel<Telegram> model(Telegram object) {
			return new DetachableTelegramModel(object);
		}
		
		private List<Telegram> getTelegramList(String limit){
			String source = "";
			String destination = "";
			String priority = "";
			String type = "";
			Date from = null;
			Date to = null;
			
			if(filtersForm != null){
				Filters filters = filtersForm.getFilters();
				if(filters != null){
					source = filters.getSourceAddress();
					destination = filters.getDestinationAddress();
					priority = filters.getPriority();
					type = filters.getType();
					from = filters.getFrom();
					to = filters.getTo();
				}
			}
			
			return getDBManager().getTelegrams(source, destination, priority, type, from, to, limit);
		}
	}
	
	private class DetachableTelegramModel extends LoadableDetachableModel<Telegram>{

		private static final long serialVersionUID = 1L;

		private final long id;

	    public DetachableTelegramModel(Telegram t){
	        this(t.getId());
	    }

	    public DetachableTelegramModel(long id){
	        this.id = id;
	    }

	    @Override
	    public int hashCode(){
	        return Long.valueOf(id).hashCode();
	    }

	    @Override
	    public boolean equals(final Object obj){
	        if (obj == this){
	            return true;
	        }
	        else if (obj == null){
	            return false;
	        }
	        else if (obj instanceof DetachableTelegramModel){
	            DetachableTelegramModel other = (DetachableTelegramModel)obj;
	            return other.id == id;
	        }
	        return false;
	    }

	    @Override
	    protected Telegram load(){
	        return getDBManager().getTelegramById((int)id);
	    }
	}
	
	public class Filters{
		
		private String sourceAddress;
		private String destinationAddress;
		private String fromDate;
		private String toDate;
		private boolean lowPriority;
		private boolean normalPriority;
		private boolean urgentPriority;
		private boolean systemPriority;
		private boolean readType;
		private boolean writeType;
		private boolean responseType;
		private int numOfTelegramsOnPage;
		
		public Filters(){
			sourceAddress = "";
			destinationAddress = "";
			fromDate = "";
			toDate = "";
			lowPriority = true;
			normalPriority = true;
			urgentPriority = true;
			systemPriority = true;
			readType = true;
			writeType = true;
			responseType = true;
			numOfTelegramsOnPage = 5;
		}
		
		public String getSourceAddress() {
			return sourceAddress;
		}
		public void setSourceAddress(String sourceAddress) {
			this.sourceAddress = sourceAddress;
		}
		public String getDestinationAddress() {
			return destinationAddress;
		}
		public void setDestinationAddress(String destinationAddress) {
			this.destinationAddress = destinationAddress;
		}
		public String getFromDate() {
			if(fromDate != null && !fromDate.isEmpty()){
				fromDate = fromDate + " 00:00:00";
			}
			return fromDate;
		}
		public Date getFrom(){
			if(getFromDate() == null || getFromDate().isEmpty()){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(0);
				return cal.getTime();
			}
			return DateConversion.getDateFromString(getFromDate());
		}
		
		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}
		public String getToDate() {
			if(toDate != null && !toDate.isEmpty()){
				toDate = toDate + " 00:00:00";
			}
			return toDate;
		}
		public Date getTo(){
			if(getToDate() == null || getToDate().isEmpty()){
				return Calendar.getInstance().getTime();
			}
			return DateConversion.getDateFromString(getToDate());
		}
		
		public void setToDate(String toDate) {
			this.toDate = toDate;
		}
		public boolean isLowPriority() {
			return lowPriority;
		}
		public void setLowPriority(boolean lowPriority) {
			this.lowPriority = lowPriority;
		}
		public boolean isNormalPriority() {
			return normalPriority;
		}
		public void setNormalPriority(boolean normalPriority) {
			this.normalPriority = normalPriority;
		}
		public boolean isUrgentPriority() {
			return urgentPriority;
		}
		public void setUrgentPriority(boolean urgentPriority) {
			this.urgentPriority = urgentPriority;
		}
		public boolean isSystemPriority() {
			return systemPriority;
		}
		public void setSystemPriority(boolean systemlowPriority) {
			this.systemPriority = systemlowPriority;
		}
		
		public String getPriority(){
			String priority = "";
			boolean isFirst = true;
			if(isLowPriority()){
				priority = "low";
				isFirst = false;
			}
			if(isNormalPriority()){
				if(!isFirst){
					priority = priority + ",";
				}
				priority = priority + "normal";
				isFirst = false;
			}
			if(isUrgentPriority()){
				if(!isFirst){
					priority = priority + ",";
				}
				priority = priority + "urgent";
				isFirst = false;
			}
			if(isSystemPriority()){
				if(!isFirst){
					priority = priority + ",";
				}
				priority = priority + "system";
				isFirst = false;
			}
			
			return priority;
		}
		
		public boolean isReadType() {
			return readType;
		}
		public void setReadType(boolean readType) {
			this.readType = readType;
		}
		public boolean isWriteType() {
			return writeType;
		}
		public void setWriteType(boolean writeType) {
			this.writeType = writeType;
		}
		public boolean isResponseType() {
			return responseType;
		}
		public void setResponseType(boolean responseType) {
			this.responseType = responseType;
		}
		public String getType(){
			String type = "";
			boolean isFirst = true;
			if(isReadType()){
				type = "Read";
				isFirst = false;
			}
			if(isWriteType()){
				if(!isFirst){
					type = type + ",";
				}
				type = type + "Write";
				isFirst = false;
			}
			if(isResponseType()){
				if(!isFirst){
					type = type + ",";
				}
				type = type + "Response";
				isFirst = false;
			}
			
			return type;
		}
		
		public int getNumOfTelegramsOnPage() {
			return numOfTelegramsOnPage;
		}
		public void setNumOfTelegramsOnPage(int numOfTelegramsOnPage) {
			this.numOfTelegramsOnPage = numOfTelegramsOnPage;
		}
	}
}