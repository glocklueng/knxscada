package pl.marek.knx.utils;

import java.util.ArrayList;

import pl.marek.knx.R;

import android.content.Context;

public class DrawableUtils {
	
	public static int getResourceIdByName(Context context, String name){
		int iconRes = -1;
		if(!"".equals(name) && name != null){
			iconRes = context.getResources().getIdentifier(name, "drawable", "pl.marek.knx");
		}
		return iconRes;
	}
	
	public static ArrayList<Integer> getLayersIconsResources(){
		ArrayList<Integer> icons = new ArrayList<Integer>();
	    
		icons.add(R.drawable.logo);
		icons.add(R.drawable.floor_m1);
		icons.add(R.drawable.floor_0);
		icons.add(R.drawable.floor_1);
		icons.add(R.drawable.floor_2);
		icons.add(R.drawable.floor_3);
		icons.add(R.drawable.floor_4);
		icons.add(R.drawable.floor_5);
		
	    return icons;
	}
	
	public static ArrayList<Integer> getSubLayersIconsResources(){
		ArrayList<Integer> icons = new ArrayList<Integer>();
		
	    icons.add(R.drawable.logo);
	    icons.add(R.drawable.room_kitchen_icon);
	    icons.add(R.drawable.room_bedroom_icon);
	    icons.add(R.drawable.room_bathroom_icon);
	    icons.add(R.drawable.room_toilet_icon);
	    icons.add(R.drawable.room_sofa_icon);
	    icons.add(R.drawable.room_tv_icon);
	    icons.add(R.drawable.room_doors_icon);	    
	    icons.add(R.drawable.room_corridor_icon);
	    icons.add(R.drawable.room_stairs_icon);
	    icons.add(R.drawable.room_room_icon);

		return icons;
	}

}
