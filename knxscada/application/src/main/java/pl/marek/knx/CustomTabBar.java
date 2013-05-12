package pl.marek.knx;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

public class CustomTabBar implements TabHost.OnTabChangeListener{
	
	private Activity rootActivity;
	private TabHost tabHost;
    private ViewPager viewPager;
    private ArrayList<TabSpec> tabs;
    
    private OnTabLongClickListener onTabLongClickListener;
    
	public CustomTabBar(Activity rootActivity){
		this.rootActivity = rootActivity;
		initialiseTabHost();
		tabs = new ArrayList<TabSpec>();
	}

	private void initialiseTabHost() {
		tabHost = (TabHost) rootActivity.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
  
	}
	
	public void addTab(String label, int iconRes) {
		TabSpec tabSpec = tabHost.newTabSpec(label);
		tabSpec.setContent(new TabFactory(rootActivity));
		tabSpec.setIndicator(createIndicator(label, iconRes));
		tabHost.addTab(tabSpec);
		tabs.add(tabSpec);
		int tabId = tabHost.getTabWidget().getTabCount() - 1;
		tabHost.getTabWidget().getChildAt(tabId).setOnLongClickListener(new TabLongClickListener(tabSpec));
		tabHost.getTabWidget().getChildAt(tabId).setOnClickListener(new TabClickListener(tabSpec));
	}
	
	private int getTabPosition(TabSpec tabSpec){
		return tabs.indexOf(tabSpec);
	}

	
	public boolean updateTab(int index, String title, int iconRes) {
		TabWidget widget = tabHost.getTabWidget();
		if (index < 0 || index >= widget.getTabCount())
			return false;
		RelativeLayout tabView = (RelativeLayout) widget.getChildTabViewAt(index);

		TextView tabTitle = (TextView) tabView.getChildAt(1);
		tabTitle.setText(title);
		
		ImageView icon = (ImageView) tabView.getChildAt(0);
		if(iconRes != -1){
			icon.setImageResource(iconRes);
		}
		return true;
	}
	
	private View createIndicator(String label, int iconRes){
		View tabIndicator = LayoutInflater.from(rootActivity).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
		
		TextView title = (TextView) tabIndicator.findViewById(android.R.id.title);
		title.setText(label);
		
		if(iconRes != -1){
			ImageView icon = (ImageView) tabIndicator.findViewById(android.R.id.icon);
			icon.setImageResource(iconRes);
		}
		return tabIndicator;
	}
	
	public void setViewPager(ViewPager viewPager){
		this.viewPager = viewPager;
	}
	
	public void setOnTabLongClickListener(OnTabLongClickListener listener){
		this.onTabLongClickListener = listener;
	}
	
	public void clearTabs(){
		tabHost.clearAllTabs();
		tabs.clear();
	}
	
	public int getCurrentTab(){
		return tabHost.getCurrentTab();
	}
	
	public void setCurrentTab(int position){
		tabHost.setCurrentTab(position);
	}
	
	public void setPreviousTab(){
		int position = getCurrentTab() - 1;
		if(position >= 0){
			setCurrentTab(position);
		}
	}
	
	public void setNextTab(){
		int position = getCurrentTab() + 1;
		if(position < tabHost.getChildCount() - 1){
			setCurrentTab(position);
		}
	}
	
	public void removeTab(int position){
		setPreviousTab();
		tabHost.getTabWidget().removeView(tabHost.getTabWidget().getChildTabViewAt(position));
		tabs.remove(position);
	}

	public void onTabChanged(String tag) {
		if(viewPager != null){
			viewPager.setCurrentItem(getCurrentTab());
		}
    }
	
	public interface OnTabLongClickListener{
		public void onTabLongClick(int position);
	}
	
	private class TabLongClickListener implements OnLongClickListener{

		private TabSpec tab;
		
		public TabLongClickListener(TabSpec tab){
			this.tab = tab;
		}
		
		public boolean onLongClick(View v) {
			if(onTabLongClickListener != null){
				onTabLongClickListener.onTabLongClick(getTabPosition(tab));
				return true;
			}
			return false;
		}
	}
	
	private class TabClickListener implements OnClickListener{

		private TabSpec tab;
		
		public TabClickListener(TabSpec tab){
			this.tab = tab;
		}
		
		public void onClick(View v) {
			setCurrentTab(getTabPosition(tab));
		}
		
		
	}
	
	public class TabFactory implements TabContentFactory {

		private final Context mContext;

	    public TabFactory(Context context) {
	        mContext = context;
	    }

	    public View createTabContent(String tag) {
	        View v = new View(mContext);
	        v.setMinimumWidth(0);
	        v.setMinimumHeight(0);
	        return v;
	    }
	}
}
