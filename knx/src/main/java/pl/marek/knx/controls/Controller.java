package pl.marek.knx.controls;

import pl.marek.knx.database.Element;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class Controller extends LinearLayout{
	
	private LinearLayout mView;
	private int layoutId;
	
	protected Element element;
	
	private Controller(Context context){
		super(context);
	}
	
	public Controller(Context context, Element element, int layoutId) {
		this(context);
		this.layoutId = layoutId;
		this.element = element;
		initialize();
	}
	
	public void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layoutId, this, true);
		mView = (LinearLayout)getChildAt(0);
	}
	
	public void setBackgroundColor(int color){
		LayerDrawable layerDrawable = (LayerDrawable)mView.getBackground();
		GradientDrawable backgroundDrawable = (GradientDrawable)layerDrawable.getDrawable(0);
		backgroundDrawable.setColor(color);
	}
	
	public LinearLayout getMainView(){
		return mView;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	

}
