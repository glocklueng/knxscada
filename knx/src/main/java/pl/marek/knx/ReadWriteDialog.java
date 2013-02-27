package pl.marek.knx;

import java.util.ArrayList;
import java.util.List;

import pl.marek.knx.utils.CalimeroDatapoints;
import pl.marek.knx.utils.MessageDialog;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.exception.KNXFormatException;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;

public class ReadWriteDialog extends Dialog implements View.OnClickListener,
		OnItemSelectedListener {

	private TextView titleTextView;
	private ImageView titleImageView;
	private Button cancelButton;
	private Button readButton;
	private Button writeButton;
	private GroupAddressView groupAddressView;
	private Spinner dataTypeSpinner;
	private Spinner functionSpinner;
	private Spinner valueSpinner;
	private EditText valueText;
	private TextView valueUnit;
	private LinearLayout valueTextFields;
	
	private FunctionsAdapter functionsAdapter;
	private ArrayAdapter<String> valueAdapter;
	
	private ReadWriteListener listener;

	public ReadWriteDialog(Context context) {
		super(context, R.style.dialogTheme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.telegram_dialog_read_write);
		setDialogSize();
		loadDialogViews();
		loadAdapters();

		titleTextView.setText(R.string.dialog_rw_telegram_title);
		titleImageView.setImageResource(R.drawable.telegram_rw);
		readButton.setOnClickListener(this);
		writeButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	private void loadDialogViews() {
		titleTextView = (TextView) findViewById(R.id.dialog_title_text);
		titleImageView = (ImageView) findViewById(R.id.dialog_title_icon);
		cancelButton = (Button) findViewById(R.id.dialog_rw_telegram_cancel_button);
		readButton = (Button) findViewById(R.id.dialog_rw_telegram_read_button);
		writeButton = (Button) findViewById(R.id.dialog_rw_telegram_write_button);
		groupAddressView = (GroupAddressView) findViewById(R.id.dialog_rw_group_address);
		dataTypeSpinner = (Spinner) findViewById(R.id.dialog_rw_telegram_data_type_spinner);
		functionSpinner = (Spinner) findViewById(R.id.dialog_rw_telegram_function_spinner);
		valueSpinner = (Spinner) findViewById(R.id.dialog_rw_telegram_value_spinner);
		valueText = (EditText) findViewById(R.id.dialog_rw_telegram_value_text);
		valueUnit = (TextView) findViewById(R.id.dialog_rw_telegram_value_unit);
		valueTextFields = (LinearLayout) findViewById(R.id.dialog_rw_telegram_value_text_fields);
	}

	private void setDialogSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = (int) (metrics.widthPixels * 0.8f);
		getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	private void loadAdapters(){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getContext(), R.array.dialog_rw_data_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataTypeSpinner.setAdapter(adapter);
		dataTypeSpinner.setOnItemSelectedListener(this);
		
		functionsAdapter = new FunctionsAdapter(getContext(), new ArrayList<DPT>());
		functionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		functionSpinner.setAdapter(functionsAdapter);
		functionSpinner.setOnItemSelectedListener(this);
		
		valueAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<String>());
		valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		valueSpinner.setAdapter(valueAdapter);
		valueSpinner.setOnItemSelectedListener(this);
	}
	
	public void setListener(ReadWriteListener listener){
		this.listener = listener;
	}
	
	@Override
	public void onClick(View v) {
		
		DPT dpt = functionsAdapter.getItem((int)functionSpinner.getSelectedItemId());
		String address = groupAddressView.getGroupAddress();
				
		if (v.equals(cancelButton)) {
			cancel();
			return;
		} 
		
		if(!checkAddress(address)){
			showWrongAddressAlertDialog();
			return;
		}
		
		if (v.equals(readButton)) {
			
			listener.read(address, dpt);			
			
		} else if (v.equals(writeButton)) {
			String value = "";
			if(valueSpinner.isShown()){
				value = String.valueOf(valueSpinner.getSelectedItem());
			}else{
				value = String.valueOf(valueText.getText());
			}
			listener.write(address, dpt, value);
		}
		dismiss();
	}
	
	private boolean checkAddress(String address){
		try {
			@SuppressWarnings("unused")
			GroupAddress addr = new GroupAddress(address);
		} catch (KNXFormatException e) {
			return false;
		}
		return true;
	}
	
	private void showWrongAddressAlertDialog(){
		MessageDialog dialog = new MessageDialog(getContext());
		dialog.showDialog(getContext().getString(R.string.dialog_rw_telegram_wrong_address_title), 
						  getContext().getString(R.string.dialog_rw_telegram_wrong_address_msg), 
						  getContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent.equals(dataTypeSpinner)){
			ArrayList<DPT> dpts = CalimeroDatapoints.getFunctions(position);
			
			functionsAdapter.clear();
			functionsAdapter.addAll(dpts);
			functionsAdapter.notifyDataSetChanged();
			
			if(position == 0){
				valueTextFields.setVisibility(View.GONE);
				valueSpinner.setVisibility(View.VISIBLE);
			} else{
				valueTextFields.setVisibility(View.VISIBLE);
				valueSpinner.setVisibility(View.GONE);
			}
			
			
		} else if(parent.equals(functionSpinner)){
			DPT dpt = functionsAdapter.getItem(position);
			if(valueTextFields.isShown()){
				valueUnit.setText(dpt.getUnit());
				
			}else if(valueSpinner.isShown()){
				ArrayList<String> values = new ArrayList<String>();
				values.add(dpt.getLowerValue());
				values.add(dpt.getUpperValue());
				
				valueAdapter.clear();
				valueAdapter.addAll(values);
				valueAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	
	public interface ReadWriteListener{
		public void read(String address, DPT dpt);
		public void write(String address, DPT dpt, String value);
	}

	public class FunctionsAdapter extends ArrayAdapter<DPT> {

		public FunctionsAdapter(Context context, List<DPT> functions) {
			super(context, android.R.layout.simple_spinner_dropdown_item,functions);
		}

		public TextView getView(int position, View convertView, ViewGroup parent) {

			DPT dpt = getItem(position);
			TextView v = (TextView) super.getView(position, convertView, parent);
			v.setText(dpt.getDescription());
			return v;
		}

		public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
			DPT dpt = getItem(position);
			TextView v = (TextView) super.getView(position, convertView, parent);
			v.setText(dpt.getDescription());
			return v;
		}
	}
}
