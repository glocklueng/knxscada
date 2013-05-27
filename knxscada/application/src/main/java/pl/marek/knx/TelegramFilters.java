package pl.marek.knx;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.marek.knx.utils.DateConversion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class TelegramFilters extends LinearLayout implements OnClickListener{
	
    private TableLayout sidebarView;
    private View outsideView;
    
    private IndividualAddressView sourceAddressView;
    private GroupAddressView destinationAddressView;
    private TextView fromDateView;
    private TextView toDateView;
    private CheckBox priorityLow;
    private CheckBox priorityNormal;
    private CheckBox priorityUrgent;
    private CheckBox prioritySystem;
    private CheckBox typeRead;
    private CheckBox typeWrite;
    private CheckBox typeResponse;
    private Button applyButton;
    
    private Date fromDate;
    private Date toDate;
	
    private TelegramFiltersListener listener;
    
	public TelegramFilters(Context context) {
		super(context);
		load();
	}
	
	public TelegramFilters(Context context, AttributeSet attrs) {
		super(context, attrs);
		load();
	}
	
	private void load() {
        if (isInEditMode()) {
            return;
        }
        init();
    }
    
    private void init() {
        removeAllViews();        
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View container = inflater.inflate(R.layout.telegram_filters, this, true);
        sidebarView = (TableLayout) container.findViewById(android.R.id.content);
        outsideView = (View) container.findViewById(android.R.id.background);
        outsideView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                hide();
            }
        });
        
        sourceAddressView = (IndividualAddressView)container.findViewById(R.id.telegram_filter_source_address_view);
        destinationAddressView = (GroupAddressView)container.findViewById(R.id.telegram_filter_destination_address_view);
        fromDateView = (TextView)container.findViewById(R.id.telegram_filter_from_date_view);
        toDateView = (TextView)container.findViewById(R.id.telegram_filter_to_date_view);
        priorityLow = (CheckBox)container.findViewById(R.id.telegram_filter_priority_low);
        priorityNormal = (CheckBox)container.findViewById(R.id.telegram_filter_priority_normal);
        priorityUrgent = (CheckBox)container.findViewById(R.id.telegram_filter_priority_urgent);
        prioritySystem = (CheckBox)container.findViewById(R.id.telegram_filter_priority_system);
        
        typeRead = (CheckBox)container.findViewById(R.id.telegram_filter_type_read);
        typeWrite = (CheckBox)container.findViewById(R.id.telegram_filter_type_write);
        typeResponse = (CheckBox)container.findViewById(R.id.telegram_filter_type_response);
        
        applyButton = (Button)container.findViewById(R.id.telegram_filter_apply);
        
        fromDateView.setOnClickListener(this);
        toDateView.setOnClickListener(this);
        applyButton.setOnClickListener(this);
    }

	public void setListener(TelegramFiltersListener listener) {
		this.listener = listener;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
		fromDateView.setText(DateConversion.getDateString(fromDate));
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
		toDateView.setText(DateConversion.getDateString(toDate));
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void show() {
    	outsideView.setVisibility(View.VISIBLE);
        outsideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_fade_in));
        sidebarView.setVisibility(View.VISIBLE);
        sidebarView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_in_from_left));
    }
    
    public void hide() {
        outsideView.setVisibility(View.GONE);
        outsideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_fade_out));
        sidebarView.setVisibility(View.GONE);
        sidebarView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_out_to_left));
    }
    
    public void toggle() {
        if (isShown()) {
            hide();
        } else {
            show();
        }
    }
    
    @Override
    public boolean isShown() {
        return sidebarView.isShown();
    }

	public void onClick(View v) {
		if(v.equals(fromDateView) || v.equals(toDateView)){
			showDatePickerDialog(v);
		}else if(v.equals(applyButton)){
			if(listener != null){
				listener.onTelegramFiltersApply(getSourceAddress(), getDestinationAddress(), fromDate, toDate, getPriorities(), getTypes());
			}
			hide();
		}
	}
	
	private String getSourceAddress(){
		if(sourceAddressView.isCorrectIndividualAddress()){
			return sourceAddressView.getIndividualAddress();
		}else{
			return "";
		}
	}
	
	private String getDestinationAddress(){
		if(destinationAddressView.isCorrectGroupAddress()){
			return destinationAddressView.getGroupAddress();
		}else{
			return "";
		}
	}
	
	private Map<String, Boolean> getPriorities(){
		Map<String, Boolean> priorities = new HashMap<String, Boolean>();
		priorities.put("low", priorityLow.isChecked());
		priorities.put("normal", priorityNormal.isChecked());
		priorities.put("urgent", priorityUrgent.isChecked());
		priorities.put("system", prioritySystem.isChecked());
		
		return priorities;
	}
	
	private Map<String, Boolean> getTypes(){
		Map<String, Boolean> types = new HashMap<String, Boolean>();
		types.put("Read", typeRead.isChecked());
		types.put("Write", typeWrite.isChecked());
		types.put("Response", typeResponse.isChecked());
		
		return types;
	}
	
    public void showDatePickerDialog(View v) {
	    DatePickerFragment newFragment = new DatePickerFragment();
	    newFragment.setDateView(v);
	    newFragment.setFilters(this);
	    FragmentManager fm = ((Activity)getContext()).getFragmentManager();
	    newFragment.show(fm, "datePicker");
	}

	public TextView getFromDateView() {
		return fromDateView;
	}

	public TextView getToDateView() {
		return toDateView;
	}

	public static class DatePickerFragment extends DialogFragment implements OnDateSetListener{
		
		private View dateView;
		private TelegramFilters filters;
		
		public void setDateView(View dateView) {
			this.dateView = dateView;
		}

		public void setFilters(TelegramFilters filters) {
			this.filters = filters;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			if(dateView.equals(filters.getFromDateView())){
				if(filters.getFromDate() != null){
					c.setTime(filters.getFromDate());
				}
			}else if(dateView.equals(filters.getToDateView())){
				if(filters.getToDate() != null){
					c.setTime(filters.getToDate());
				}
			}
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(year, month, day);
			if(dateView.equals(filters.getFromDateView())){
				filters.setFromDate(cal.getTime());
			}else if(dateView.equals(filters.getToDateView())){
				filters.setToDate(cal.getTime());
			}
		}
	}
	
	public interface TelegramFiltersListener{
		public void onTelegramFiltersApply(String srcAddr, String dstAddr, Date from, Date to, Map<String, Boolean> priorities, Map<String, Boolean> types);
	}

}
