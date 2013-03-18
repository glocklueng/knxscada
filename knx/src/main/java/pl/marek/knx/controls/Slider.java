package pl.marek.knx.controls;

import pl.marek.knx.R;
import pl.marek.knx.utils.ColorUtils;
import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Slider extends Controller implements OnSeekBarChangeListener{

	private TextView nameView;
	private TextView descriptionView;
	private SeekBar sliderView;
	
	private int startColor;
	private int stopColor;


	public Slider(Context context) {
		super(context, R.layout.control_slider);
		init();
	}
		
	public void init(){
		
		nameView = (TextView)getMainView().findViewById(android.R.id.title);
		descriptionView = (TextView)getMainView().findViewById(android.R.id.summary);
		sliderView = (SeekBar)getMainView().findViewById(R.id.seekbar);
		sliderView.setOnSeekBarChangeListener(this);
		
		startColor = getResources().getColor(R.color.control_slider_low_color);
		stopColor = getResources().getColor(R.color.control_slider_high_color);
		
		setBackgroundColorBySliderPosition(sliderView.getProgress());
	}

	public void setName(String name){
		nameView.setText(name);
	}
	
	public String getName(){
		return nameView.getText().toString();
	}
	
	public void setDescription(String description){
		descriptionView.setText(description);
	}
	
	public String getDescription(){
		return descriptionView.getText().toString();
	}

	
	private void setBackgroundColorBySliderPosition(int position){
		int color = ColorUtils.interpolate(startColor, stopColor, position/100.0f);
		setBackgroundColor(color);
	}
	
	public void setStartColor(int color){
		startColor = color;
	}
	
	public void setStopColor(int color){
		stopColor = color;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		//Log.i("SeekBar", String.format("S: %d P: %d B: %s", seekBar.getProgress(), progress, fromUser));

		setBackgroundColorBySliderPosition(progress);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		Log.i("SeekBar START", String.format("%d", seekBar.getProgress()));
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Log.i("SeekBar STOP", String.format("%d", seekBar.getProgress()));
		
	}


}
