package pl.marek.knx;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SubLayerPagerAdapter extends FragmentStatePagerAdapter {
	
	private FragmentManager fragmentManager;
	private List<Fragment> subLayers;

	public SubLayerPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentManager = fm;
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
	
    @Override
    public int getItemPosition(Object object){
    	return POSITION_NONE;
    }
	
	public void addPage(Fragment subLayer){
		fragmentManager.beginTransaction().attach(subLayer).commit();
		subLayers.add(subLayer);
		notifyDataSetChanged();
	}
	
	public void updatePage(int index, Fragment fragment){
		subLayers.set(index, fragment);
		notifyDataSetChanged();
	}
	
	public void removePage(Fragment subLayer){
		fragmentManager.beginTransaction().remove(subLayer).commit();
		subLayers.remove(subLayer);
		notifyDataSetChanged();
	}
	
	public void clear(){
		for(Fragment f: subLayers){
			fragmentManager.beginTransaction().remove(f).commit();
		}
		subLayers.clear();
		notifyDataSetChanged();
	}
}