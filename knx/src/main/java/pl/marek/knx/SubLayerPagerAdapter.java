package pl.marek.knx;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SubLayerPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> subLayers;

	public SubLayerPagerAdapter(FragmentManager fm) {
		super(fm);
		subLayers = new ArrayList<Fragment>();
	}
	
	public SubLayerPagerAdapter(FragmentManager fm, List<Fragment> subLayers) {
		super(fm);
		this.subLayers = subLayers;
	}

	@Override
	public Fragment getItem(int position) {
		return this.subLayers.get(position);
	}

	@Override
	public int getCount() {
		return this.subLayers.size();
	}
	
	public void addPage(Fragment subLayer){
		subLayers.add(subLayer);
		notifyDataSetChanged();
	}
	
	public void removePage(Fragment subLayer){
		subLayers.remove(subLayer);
		notifyDataSetChanged();
	}
	
	public void clear(){
		subLayers.clear();
		notifyDataSetChanged();
	}
}