package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.CustomTabBar.OnTabLongClickListener;
import pl.marek.knx.IconPickerDialog.OnIconPickListener;
import pl.marek.knx.SideBarItem;
import pl.marek.knx.PopupMenuDialog.PopupMenuItemListener;
import pl.marek.knx.SideBarView.SideBarListener;
import pl.marek.knx.SideBarView.SideBarMode;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.DrawableUtils;
import pl.marek.knx.utils.MessageDialog;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ProjectActivity extends FragmentActivity implements SideBarListener, PopupMenuItemListener, OnTabLongClickListener{
	
	private static final int NEW_LAYER_ITEM_ID = 0;
	
	private Project project;
	private Layer currentLayer;
	private SubLayer currentSubLayer;
	
	private DatabaseManager dbManager;
	private SideBarView layersSideBarView;
	private GestureDetector layersGestureDetector;
	private SideBarView rightSideBarView;
	private GestureDetector rightGestureDetector;
	
	private LayerDialog<?> layerDialog;
	private PopupMenuDialog layerPopupMenu;
	private Layer editedLayer;
	private SubLayer editedSubLayer;
	
	private ActionBar actionBar;
	
	private CustomTabBar tabBar;
    private ViewPager subLayerViewPager;
    private SubLayerPagerAdapter subLayerPagerAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		
		dbManager = new DatabaseManagerImpl(this);
		
		Project intentProject = getIntent().getParcelableExtra(Project.PROJECT);
		project = dbManager.getProjectByIdWithDependencies(intentProject.getId());
		
		setActionBar();
		tabBar = new CustomTabBar(this);
		tabBar.setOnTabLongClickListener(this);
		
		initialiseSubLayersPaging();
		setSideBar();
		setLayersSideBar();

	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.project_menu, menu);
	    return true;
	}
	
	private void setActionBar(){
		actionBar = getActionBar();
		if(project != null){
			actionBar.setTitle(project.getName());
		}
		actionBar.setDisplayHomeAsUpEnabled(true);			
	}
	
	private void initialiseSubLayersPaging(){
        subLayerViewPager = (ViewPager) findViewById(R.id.viewpager);
        subLayerViewPager.setOnPageChangeListener(new SubLayerPageChangeListener());
		subLayerPagerAdapter = new SubLayerPagerAdapter(super.getSupportFragmentManager());
		subLayerViewPager.setAdapter(subLayerPagerAdapter);
		tabBar.setViewPager(subLayerViewPager);
		
		
	}
		
	private void addSubLayer(SubLayer subLayer){
		tabBar.addTab(subLayer.getName(), getResourceIdByName(subLayer.getIcon()));
		Bundle bundle = new Bundle();
		bundle.putParcelable(SubLayer.SUBLAYER, subLayer);
		subLayerPagerAdapter.addPage(Fragment.instantiate(this, SubLayerFragment.class.getName(), bundle));
	}
	
	private void clearSubLayers(){
		tabBar.clearTabs();
		subLayerPagerAdapter.clear();
		subLayerViewPager.setAdapter(subLayerPagerAdapter);
	}
	
	private void setLayersSideBar(){
	    layersSideBarView = (SideBarView) findViewById(R.id.left_side_navigation_view);
	    layersSideBarView.setSideBarListener(this);
	    layersSideBarView.setMode(SideBarMode.LEFT);
	    layersSideBarView.setItems(getLayersItems());
	    layersSideBarView.selectFirstItem();
	    layersGestureDetector = new GestureDetector(this, layersSideBarView);
	}
	
	private ArrayList<SideBarItem> getLayersItems(){
		ArrayList<SideBarItem> items = new ArrayList<SideBarItem>();
		SideBarItem addItem = new SideBarItem();
	    addItem.setId(NEW_LAYER_ITEM_ID);
	    addItem.setName(getString(R.string.project_add_layer_item));
    	addItem.setIcon(R.drawable.new_item);
		items.add(addItem);
		
		for(Layer layer: project.getLayers()){
			items.add(createSideBarItem(layer));
		}
		return items;
	}
	
	private SideBarItem createSideBarItem(Layer layer){
		SideBarItem item = new SideBarItem();
	    item.setId(layer.getId());
	    item.setName(layer.getName());
	    item.setIcon(getResourceIdByName(layer.getIcon()));
	    return item;
	}
	
	private void setSideBar(){

	    rightSideBarView = (SideBarView) findViewById(R.id.right_side_navigation_view);
	    rightSideBarView.setSideBarListener(this);
	    rightSideBarView.setMode(SideBarMode.RIGHT);
	    
	    for(int i=1;i< 100; i++){
	    	SideBarItem item = new SideBarItem();
		    item.setId(i);
		    item.setName(String.format("Feature %d", i));
	    	rightSideBarView.addItem(item);
	    }
	    
	    rightGestureDetector = new GestureDetector(this, rightSideBarView);
	}
	
	@Override
	public void onSideBarItemClick(SideBarView view, SideBarItem item) {
		if(view.equals(layersSideBarView)){
			if(item.getId() == NEW_LAYER_ITEM_ID){
				showNewLayerDialog();
			} else{
				clearSubLayers();
				currentLayer = dbManager.getLayerByIdWithDependencies(item.getId());
				ArrayList<SubLayer> subLayers = currentLayer.getSubLayers();
				for(SubLayer s:subLayers){
					addSubLayer(s);
				}
			}
		}
	}
	
	@Override
	public void onSideBarItemLongClick(SideBarView view, SideBarItem item) {
		if(view.equals(layersSideBarView)){
			if(item.getId() != NEW_LAYER_ITEM_ID){
				Layer layer = dbManager.getLayerById(item.getId());
				editedLayer = layer;
				showLayerPopupMenu(layer);
			}
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean layersSideBarConsumed = layersGestureDetector.onTouchEvent(event);
		boolean rightSideBarConsumed = rightGestureDetector.onTouchEvent(event);
		if(layersSideBarConsumed || rightSideBarConsumed)
			return true;
		else
			return super.dispatchTouchEvent(event);
	}
	
	@Override
	public void onTabLongClick(int tabId) {
		if(currentLayer != null){
			editedSubLayer = currentLayer.getSubLayers().get(tabId); 
			showLayerPopupMenu(editedSubLayer);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	if(rightSideBarView.isShown())
	    		rightSideBarView.hide();
	        layersSideBarView.toggle();
	        break;
	    case R.id.project_ab_features:
	    	if(layersSideBarView.isShown())
	    		layersSideBarView.hide();
	    	rightSideBarView.toggle();
	    	break;
	    case R.id.project_add_sublayer_menu_item:
	    	showNewSubLayerDialog();
	    	break;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dbManager != null && !dbManager.isOpen()){
			dbManager.open();
		}
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		if(dbManager != null && dbManager.isOpen())
			dbManager.close();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(layerDialog != null){
			if(layerDialog.isShowing()){
				outState.putString(LayerDialog.NAME, layerDialog.getName());
				outState.putString(LayerDialog.DESCRIPTION, layerDialog.getDescription());
				outState.putString(LayerDialog.ICON, layerDialog.getIconName());
				outState.putString(LayerDialog.CLASS_NAME, layerDialog.getClassName());
				if(layerDialog.getLayer() != null){
					if(layerDialog.getLayer() instanceof SubLayer){
						outState.putParcelable(SubLayer.SUBLAYER, layerDialog.getLayer());
					}else{
						outState.putParcelable(Layer.LAYER, layerDialog.getLayer());
					}
				}
				
				layerDialog.dismiss();
			}
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		if(state != null){
			String name = state.getString(LayerDialog.NAME);
			String description = state.getString(LayerDialog.DESCRIPTION);
			String icon = state.getString(LayerDialog.ICON);
			String className = state.getString(LayerDialog.CLASS_NAME);
					
			Layer dialogLayer = state.getParcelable(Layer.LAYER);
			SubLayer dialogSubLayer = state.getParcelable(SubLayer.SUBLAYER);
			
			if(className != null){
				if(className.equals(SubLayer.class.getName())){
					if(dialogSubLayer != null){
						showEditSubLayerDialog(dialogSubLayer);
					}else{
						showNewSubLayerDialog();
					}
				}else{
					if(dialogLayer != null){
						showEditLayerDialog(dialogLayer);
					}else{
						showNewLayerDialog();
					}
					
				}
				setLayerDialogParameters(name, description, icon);
			}
		}
	}
	
	private void showNewLayerDialog(){
		layerDialog = new LayerDialog<Layer>(this, Layer.class);
		layerDialog.show();
		layerDialog.setTitle(getString(R.string.dialog_new_layer_title));
	}
	
	private void showEditLayerDialog(Layer layer){
		layerDialog = new LayerDialog<Layer>(this, Layer.class, layer);
		layerDialog.show();
		layerDialog.setTitle(getString(R.string.dialog_edit_layer_title));
	}
	
	private void showNewSubLayerDialog(){
		layerDialog = new LayerDialog<SubLayer>(this, SubLayer.class);
		layerDialog.show();
		layerDialog.setTitle(getString(R.string.dialog_new_sublayer_title));
	}
	
	private void showEditSubLayerDialog(SubLayer layer){
		layerDialog = new LayerDialog<SubLayer>(this, SubLayer.class, layer);
		layerDialog.show();
		layerDialog.setTitle(getString(R.string.dialog_edit_sublayer_title));
	}
	
	private void setLayerDialogParameters(String name, String description, String icon){
		if(layerDialog != null){
			if(name != null)
				layerDialog.setName(name);
			if(description != null)
				layerDialog.setDescription(description);
			if(icon != null)
				layerDialog.setIcon(icon);
		}
	}
	
	
	public void showLayerPopupMenu(Layer layer){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem;
		PopupMenuItem deleteItem; 
		if(layer instanceof SubLayer){
			editItem = new PopupMenuItem(getString(R.string.sublayer_popup_menu_item_edit), getResources().getDrawable(R.drawable.edit_icon));
			deleteItem = new PopupMenuItem(getString(R.string.sublayer_popup_menu_item_delete), getResources().getDrawable(R.drawable.trash_icon));
		} else{
			editItem = new PopupMenuItem(getString(R.string.layer_popup_menu_item_edit), getResources().getDrawable(R.drawable.edit_icon));
			deleteItem = new PopupMenuItem(getString(R.string.layer_popup_menu_item_delete), getResources().getDrawable(R.drawable.trash_icon));
		}
		items.add(editItem);
		items.add(deleteItem);
		
		layerPopupMenu = new PopupMenuDialog(this,layer.getName(), items);
		layerPopupMenu.setPopupMenuItemListener(this);
		layerPopupMenu.show();
	}
	
	@Override
	public void onPopupMenuItemClick(int position, PopupMenuItem item) {
		if(item.getName().equals(getString(R.string.layer_popup_menu_item_edit))){
			showEditLayerDialog(editedLayer);
		}else if(item.getName().equals(getString(R.string.layer_popup_menu_item_delete))){
			showDeleteConfirmation(editedLayer);
		}else if(item.getName().equals(getString(R.string.sublayer_popup_menu_item_edit))){
			showEditSubLayerDialog(editedSubLayer);
		}else if(item.getName().equals(getString(R.string.sublayer_popup_menu_item_delete))){
			showDeleteConfirmation(editedSubLayer);
		}
	}
	
	public void showDeleteConfirmation(final Layer layer){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.layer_delete_confirmation_title));
		builder.setIcon(getResources().getDrawable(R.drawable.trash_icon));
		if(layer instanceof SubLayer){
			builder.setMessage(getString(R.string.sublayer_delete_confirmation_text, layer.getName()));
		}else{
			builder.setMessage(getString(R.string.layer_delete_confirmation_text, layer.getName()));	
		}
		builder.setPositiveButton(getString(R.string.yes), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(layer instanceof SubLayer){
					removeSubLayer((SubLayer)layer);
				}else{
					removeLayer(layer);	
				}
			}
		});
		builder.setNegativeButton(getString(R.string.no), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				editedLayer = null;
			}
		});
		builder.create().show();
	}
		
	public boolean addLayer(String name, String description, String iconName){
		if(!layerConditionCheck())
			return false;
		
		Layer layer = new Layer(project.getId(), name, description, iconName);
		dbManager.addLayer(layer);
		project.getLayers().add(layer);
		
		SideBarItem item = new SideBarItem();
		item.setName(name);
		item.setId(layer.getId());
		item.setIcon(getResourceIdByName(iconName));
		layersSideBarView.addItem(item);
		
		return true;
	}
	
	public boolean editLayer(Layer layer){
		if(!layerConditionCheck())
			return false;
		dbManager.updateLayer(layer);
		int index = findSideBarLayerIndex(layer);
		layersSideBarView.setItem(createSideBarItem(layer), index);
		editedLayer = null;
		return true;
	}
	
	public boolean removeLayer(Layer layer){
		dbManager.removeLayer(layer);
		int index = findSideBarLayerIndex(layer);
		layersSideBarView.removeItem(index);
		editedLayer = null;
		return false;
	}
	
	public boolean addSubLayer(String name, String description, String iconName){
		if(!layerConditionCheck())
			return false;
		
		SubLayer layer = new SubLayer(project.getId(), currentLayer.getId(), name, description, iconName);
		dbManager.addSubLayer(layer);
		currentLayer.getSubLayers().add(layer);

		addSubLayer(layer);
		
		return true;
	}
	
	public boolean editSubLayer(SubLayer layer){
		if(!layerConditionCheck())
			return false;
		int position = currentLayer.getSubLayers().indexOf(editedSubLayer);
		
		dbManager.updateSubLayer(layer);
		editedSubLayer = null;
		
		tabBar.updateTab(position, layer.getName(),getResourceIdByName(layer.getIcon()));
		Bundle bundle = new Bundle();
		bundle.putParcelable(SubLayer.SUBLAYER, layer);
		subLayerPagerAdapter.updatePage(position, Fragment.instantiate(this, SubLayerFragment.class.getName(), bundle));
		
		return true;
	}
	
	public boolean removeSubLayer(SubLayer layer){
		int position = currentLayer.getSubLayers().indexOf(layer);
		dbManager.removeSubLayer(layer);
		editedSubLayer = null;
		
		tabBar.removeTab(position);
		subLayerPagerAdapter.removePage(subLayerPagerAdapter.getItem(position));

		return false;
	}
	
	private boolean layerConditionCheck(){
		if(layerDialog.getName().isEmpty()){
			return false;
		}
		return true;
	}
	
	private int findSideBarLayerIndex(Layer layer){
		
		int id = layer.getId();
		for(SideBarItem item: layersSideBarView.getItems()){
			if(id == item.getId()){
				return layersSideBarView.getItems().indexOf(item);
			}
		}
		return -1;
	}
	
	private int getResourceIdByName(String name){
		return DrawableUtils.getResourceIdByName(this, name);
	}
		
	public class SubLayerPageChangeListener extends ViewPager.SimpleOnPageChangeListener{
		@Override
        public void onPageSelected(int position) {
            tabBar.setCurrentTab(position);
            currentSubLayer = currentLayer.getSubLayers().get(position);
        }
	}
	
	public class LayerDialog<T extends Layer> extends Dialog implements View.OnClickListener, OnIconPickListener{
		
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String ICON = "icon";
		public static final String CLASS_NAME = "class_name";
		
		
		private TextView titleView;
		private TextView nameView;
		private TextView descView;
		private TextView nameLabelView;
		private TextView descLabelView;
		private ImageButton iconViewButton;
		private Button applyButton;
		private Button cancelButton;
		
		private int iconRes = -1;
		
		private T layer;
		private Class<T> clazz;
		
		public LayerDialog(Context context, Class<T> clazz){
			super(context, R.style.dialogTheme);
			this.clazz = clazz;
		}

		public LayerDialog(Context context, Class<T> clazz,  T layer) {
			this(context, clazz);
			this.layer = layer;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layer_dialog);
	        
			setDialogSize();
			
			titleView = (TextView)findViewById(R.id.dialog_title_text);
			nameView = (TextView)findViewById(R.id.dialog_new_layer_name);
			descView = (TextView)findViewById(R.id.dialog_new_layer_description);			
			iconViewButton = (ImageButton) findViewById(R.id.dialog_new_layer_icon);
			nameLabelView = (TextView)findViewById(R.id.dialog_new_layer_name_label);
			descLabelView = (TextView)findViewById(R.id.dialog_new_layer_description_label);
			
			applyButton = (Button)findViewById(R.id.dialog_layer_add_button);
			cancelButton = (Button)findViewById(R.id.dialog_new_layer_cancel_button);
			
			applyButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
			iconViewButton.setOnClickListener(this);
			
			setInitialValues();
		}
		
		private void setInitialValues(){
			if(layer != null){
				applyButton.setText(getString(R.string.dialog_layer_edit_button));
				nameView.setText(layer.getName());
				descView.setText(layer.getDescription());
				setIcon(layer.getIcon());
			}else{
				if(clazz.equals(SubLayer.class)){
					applyButton.setText(getString(R.string.dialog_sublayer_add_button));
				}
			}
			if(clazz.equals(SubLayer.class)){
				nameView.setHint(getString(R.string.dialog_new_sublayer_name_hint));
				descView.setHint(getString(R.string.dialog_new_sublayer_description_hint));
				nameLabelView.setText(getString(R.string.dialog_new_sublayer_name));
				descLabelView.setText(getString(R.string.dialog_new_sublayer_description));
			}	
		}
		
		private void setDialogSize(){
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = (int)(metrics.widthPixels * 0.95f);
			getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
		}
		
		public String getName(){
			return nameView.getText().toString();
		}
		
		public void setName(String name){
			nameView.setText(name);
		}
		
		public String getDescription(){
			return descView.getText().toString();
		}
		
		public void setDescription(String description){
			descView.setText(description);
		}
		
		public String getIconName(){
			String name = "";
			try{
				if(iconRes > 0)
					name = getContext().getResources().getResourceEntryName(iconRes);
			} catch(NotFoundException ex){
				Log.d(getString(R.string.application_name), ex.getMessage());
			}
			return name;
		}
		
		public void setIcon(String name){
			iconRes = getResourceIdByName(name);
			if(iconRes > 0)
				iconViewButton.setImageResource(iconRes);
		}
		
		public void setTitle(String title){
			titleView.setText(title);
		}
		
		public void setApplyButtonText(String text){
			applyButton.setText(text);
		}
		
		public void setLayer(T layer){
			this.layer = layer;
		}
		
		public T getLayer(){
			return layer;
		};
		
		public String getClassName(){
			return clazz.getName();
		}

		@Override
		public void onClick(View v) {
			boolean state = true;
			
			if(v.equals(applyButton)){
				if(layer == null){
					if(clazz.equals(SubLayer.class)){
						state = addSubLayer(getName(), getDescription(), getIconName());
					}else{
						state = addLayer(getName(), getDescription(), getIconName());
					}
				} else{
					layer.setProjectId(project.getId());
					layer.setName(getName());
					layer.setDescription(getDescription());
					layer.setIcon(getIconName());
					if(clazz.equals(SubLayer.class)){
						SubLayer subLayer = (SubLayer)layer;
						subLayer.setLayerId(currentLayer.getId());
						state = editSubLayer(subLayer);
					} else{
						state = editLayer(layer);	
					}
				}
				if(state){
					dismiss();
				} else{
					MessageDialog msgDialog = new MessageDialog(getContext());
					if(clazz.equals(SubLayer.class)){
						msgDialog.showDialog(getString(R.string.sublayer_name_empty_title), 
											 getString(R.string.sublayer_name_empty_text), 
											 getResources().getDrawable(android.R.drawable.ic_dialog_alert));
					} else{
						msgDialog.showDialog(getString(R.string.layer_name_empty_title), 
											 getString(R.string.layer_name_empty_text), 
											 getResources().getDrawable(android.R.drawable.ic_dialog_alert));
					}
				}
			} else if(v.equals(cancelButton)){
				cancel();
			} else if(v.equals(iconViewButton)){
				IconPickerDialog picker = new IconPickerDialog(getContext());
				if(clazz.equals(SubLayer.class)){
					picker.setIcons(DrawableUtils.getSubLayersIconsResources());
				}else{
					picker.setIcons(DrawableUtils.getLayersIconsResources());
				}
				picker.setOnIconPickListener(this);
				picker.show();
			}
		}

		@Override
		public void onIconPick(int iconRes) {
			iconViewButton.setImageResource(iconRes);
			this.iconRes = iconRes;
		}
	}
}
