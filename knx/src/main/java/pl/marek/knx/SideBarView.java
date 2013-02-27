package pl.marek.knx;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SideBarView extends LinearLayout implements OnGestureListener{
	
	private static final int ACCEPT_SLIDE_MOVE = 150;
	private static final int ACCEPT_SLIDE_START_MARGIN = 100;
	
    private LinearLayout sidebarView;
    private ListView listView;
    private View outsideView;

    private SideBarListener listener;
    private ArrayList<SideBarItem> items;
    private SideBarMode mode = SideBarMode.LEFT;
    private BaseAdapter sideBarAdapter;
    
    public SideBarView(Context context) {
        super(context);
        load();
    }

    public SideBarView(Context context, AttributeSet attrs) {
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
        View container = inflater.inflate(mode.getResource(), this, true);
        sidebarView = (LinearLayout) container.findViewById(android.R.id.content);
        listView = (ListView) container.findViewById(android.R.id.list);
        outsideView = (View) container.findViewById(android.R.id.background);
        outsideView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onSideBarItemClick(items.get(position));
                }
                hide();
            }
        });
        items = new ArrayList<SideBarItem>();
        sideBarAdapter = new SideBarAdapter();
        listView.setAdapter(sideBarAdapter);
    }
    
    public void setSideBarListener(SideBarListener listener) {
        this.listener = listener;
    }
    
    public void setSideBarAdapter(BaseAdapter adapter){
    	sideBarAdapter = adapter;
    }
    
    public ArrayList<SideBarItem> getItems(){
    	return items;
    }
    
    public void setItems(ArrayList<SideBarItem> items){
    	this.items = items;
    	notifySetDataChanged();
    }
    
    public void addItem(SideBarItem item){
    	items.add(item);
    	notifySetDataChanged();
    }
    
    public void removeItem(SideBarItem item){
    	items.remove(item);
    	notifySetDataChanged();
    }
    
    public void setItem(SideBarItem item, int position){
    	items.set(position, item);
    	notifySetDataChanged();
    }
    
    private void notifySetDataChanged(){
    	if(sideBarAdapter != null){
    		sideBarAdapter.notifyDataSetChanged();
    	}
    }
    
    public void setMode(SideBarMode mode) {
        if (isShown()) {
            hide();
        }
        this.mode = mode;
        init();
    }
    
    public SideBarMode getMode() {
        return mode;
    }
        
    public void show() {
    	outsideView.setVisibility(View.VISIBLE);
        outsideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_fade_in));
        sidebarView.setVisibility(View.VISIBLE);
        sidebarView.startAnimation(mode.getInAnimation(getContext()));
    }
    
    public void hide() {
        outsideView.setVisibility(View.GONE);
        outsideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sidebar_fade_out));
        sidebarView.setVisibility(View.GONE);
        sidebarView.startAnimation(mode.getOutAnimation(getContext()));
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
    
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		
		if(mode.acceptSlideMove(getContext(), (int)e1.getX())){
			int move = (int)(e2.getX() - e1.getX());
			if(Math.abs(move) >= ACCEPT_SLIDE_MOVE){
				show();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
    
    
	public interface SideBarListener {
		public void onSideBarItemClick(SideBarItem item);
	}
    
    public static enum SideBarMode {
        LEFT {
			@Override
			public int getResource() {
				return R.layout.sidebar_left;
			}

			@Override
			public Animation getInAnimation(Context context) {
				return AnimationUtils.loadAnimation(context, R.anim.sidebar_in_from_left);
			}

			@Override
			public Animation getOutAnimation(Context context) {
				return AnimationUtils.loadAnimation(context, R.anim.sidebar_out_to_left);
			}

			@Override
			public boolean acceptSlideMove(Context context, int startPosition) {
				if(startPosition >= 0 && startPosition <= ACCEPT_SLIDE_START_MARGIN)
					return true;
				return false;
			}
		},
        
        RIGHT {
			@Override
			public int getResource() {
				return R.layout.sidebar_right;
			}

			@Override
			public Animation getInAnimation(Context context) {
				return AnimationUtils.loadAnimation(context, R.anim.sidebar_in_from_right);
			}

			@Override
			public Animation getOutAnimation(Context context) {
				return AnimationUtils.loadAnimation(context, R.anim.sidebar_out_to_right);
			}

			@Override
			public boolean acceptSlideMove(Context context, int startPosition) {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				int width = size.x;
				if(startPosition <= width && startPosition >= width-ACCEPT_SLIDE_START_MARGIN)
					return true;
				return false;
			}
		};
        
        public abstract int getResource();
        public abstract Animation getInAnimation(Context context);
        public abstract Animation getOutAnimation(Context context);
        public abstract boolean acceptSlideMove(Context context, int startPosition);
    };
    
    
    
    private class SideBarAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SideBarAdapter() {
            inflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	SideBarItem item = (SideBarItem)getItem(position);
        	
        	ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.sidebar_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.sidebar_item_text);
                holder.icon = (ImageView) convertView.findViewById(R.id.sidebar_item_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            holder.text.setText(item.getName());
            if (item.getIcon() != SideBarItem.DEFAULT_ICON_VALUE) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(item.getIcon());
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            return convertView;
        }
        
        private class ViewHolder {
            TextView text;
            ImageView icon;
        }
    }
}

class SideBarItem {

    public static final int DEFAULT_ICON_VALUE = -1;

    private int id;
    private String name;
    private int icon = DEFAULT_ICON_VALUE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
