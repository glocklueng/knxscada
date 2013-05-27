package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.ControllerDialog.OnControllerDialogApproveListener;
import pl.marek.knx.CustomTabBar.OnTabLongClickListener;
import pl.marek.knx.LayerDialog.OnLayerDialogApproveListener;
import pl.marek.knx.SideBarItem;
import pl.marek.knx.PopupMenuDialog.PopupMenuItemListener;
import pl.marek.knx.SideBarView.SideBarListener;
import pl.marek.knx.SideBarView.SideBarMode;
import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.controls.Controller;
import pl.marek.knx.controls.ControllerType;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.DrawableUtils;
import pl.marek.knx.utils.MessageDialog;
import pl.marek.knx.web.WebServerSettings;
import pl.marek.knx.web.WebServerState;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProjectActivity extends FragmentActivity implements SideBarListener, 
																 PopupMenuItemListener, 
																 OnTabLongClickListener, 
																 OnControllerDialogApproveListener, 
																 OnItemLongClickListener,
																 OnLayerDialogApproveListener{
	
	private static final int NEW_LAYER_ITEM_ID = -1;
	private static final int POPUP_MENU_ITEM_LAYER_EDIT = 100;
	private static final int POPUP_MENU_ITEM_LAYER_DELETE = 101;
	private static final int POPUP_MENU_ITEM_SUBLAYER_EDIT = 102;
	private static final int POPUP_MENU_ITEM_SUBLAYER_DELETE = 103;
	private static final int POPUP_MENU_ITEM_ELEMENT_EDIT = 104;
	private static final int POPUP_MENU_ITEM_ELEMENT_DELETE = 105;
	
	private Project project;
	private Layer currentLayer;
	private SubLayer currentSubLayer;
	
	private Layer editedLayer;
	private SubLayer editedSubLayer;
	private Element editedElement;
	private Controller editedController;
	
	private DatabaseManager dbManager;
	private SideBarView layersSideBarView;
	private GestureDetector layersGestureDetector;
	private SideBarView controllersSideBarView;
	
	private LayerDialog<?> layerDialog;
	private ControllerDialog controllerDialog;
	private PopupMenuDialog layerPopupMenu;
	private PopupMenuDialog elementPopupMenu;
	
	private ActionBar actionBar;
	private CustomTabBar tabBar;
    private ViewPager subLayerViewPager;
    private SubLayerPagerAdapter subLayerPagerAdapter;
    
	private class SubLayerPageChangeListener extends ViewPager.SimpleOnPageChangeListener{
		
		@Override
        public void onPageSelected(int position) {
            tabBar.setCurrentTab(position);
            currentSubLayer = currentLayer.getSubLayers().get(position);
        }
	}
    
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
		
		setControllersSideBar();
		setLayersSideBar();
		establishKNXConnection();
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.project_menu, menu);
	    return true;
	}
	
	private void establishKNXConnection(){
		MainApplication mainApp = (MainApplication)getApplication();
		ConnectionState connState = mainApp.getKNXConnectionState();
		if(!connState.equals(ConnectionState.CONNECTED)){
			showKNXConnectionStartDialog();
		}
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
		if(subLayer.isMainSubLayer()){
			String tabName = "";
			if(currentLayer != null){
				if(currentLayer.isMainLayer()){
					tabName = project.getName();
				}else{
					tabName = currentLayer.getName();
				}
			}
			tabBar.addTab(tabName, R.drawable.logo);
		}else{
			tabBar.addTab(subLayer.getName(), getResourceIdByName(subLayer.getIcon()));
		}
		Bundle bundle = new Bundle();
		bundle.putParcelable(SubLayer.SUBLAYER, subLayer);
		bundle.putParcelable(Project.PROJECT, project);
		bundle.putParcelable(Layer.LAYER, currentLayer);
		subLayerPagerAdapter.addPage(Fragment.instantiate(this, SubLayerFragment.class.getName(), bundle));
		if(subLayerPagerAdapter.getCount() == 1){
			currentSubLayer = subLayer;
		}
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
			items.add(createLayerSideBarItem(layer));
		}
		return items;
	}
	
	private SideBarItem createLayerSideBarItem(Layer layer){
		SideBarItem item = new SideBarItem();
	    item.setId(layer.getId());
	    if(layer.isMainLayer()){
	    	item.setName(project.getName());
	    	item.setIcon(R.drawable.logo);
	    }else{
	    	item.setName(layer.getName());
	    	item.setIcon(getResourceIdByName(layer.getIcon()));
	    }
	    return item;
	}
	
	private void setControllersSideBar(){

	    controllersSideBarView = (SideBarView) findViewById(R.id.right_side_navigation_view);
	    controllersSideBarView.setSideBarListener(this);
	    controllersSideBarView.setMode(SideBarMode.RIGHT);
	    
	    ArrayList<SideBarItem> controllers = new ArrayList<SideBarItem>();
	    	    
	    for(ControllerType type: ControllerType.values()){
	    	controllers.add(createControllerSideBarItem(type, type.ordinal()));
	    }
	    
	    controllersSideBarView.setItems(controllers);
	    
	}
	
	private SideBarItem createControllerSideBarItem(ControllerType controllerType, int id){
		SideBarItem item = new SideBarItem();
	    item.setId(id);
	    item.setName(controllerType.getTitle(this));
	    item.setIcon(controllerType.getIcon());
	    return item;
	}
		
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {		
		Controller c = (Controller)view;
		showControllerPopupMenu(c);
		return false;
	}
	
	public void onSideBarItemClick(SideBarView view, SideBarItem item) {
		if(view.equals(layersSideBarView)){
			if(item.getId() == NEW_LAYER_ITEM_ID){
				showNewLayerDialog();
			} else{
				clearSubLayers();
				currentLayer = dbManager.getLayerByIdWithDependencies(item.getId());
				ArrayList<SubLayer> subLayers = currentLayer.getSubLayers();
				if(subLayers.size() > 0){
					currentSubLayer = subLayers.get(0);	
				}else{
					currentSubLayer = null;
				}
				for(SubLayer s:subLayers){
					addSubLayer(s);
				}
			}
		} else if(view.equals(controllersSideBarView)){
			ControllerType controllerType = ControllerType.values()[item.getId()];
			showControllerDialog(controllerType, null);
		}
	}
	
	public void onSideBarItemLongClick(SideBarView view, SideBarItem item) {
		if(view.equals(layersSideBarView)){
			if(item.getId() != NEW_LAYER_ITEM_ID){
				Layer layer = dbManager.getLayerById(item.getId());
				if(!layer.isMainLayer()){
					editedLayer = layer;
					showLayerPopupMenu(layer);
				}
			}
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(layersGestureDetector.onTouchEvent(event))
			return true;
		else
			return super.dispatchTouchEvent(event);
	}
	
	public void onTabLongClick(int position) {
		if(currentLayer != null){
			SubLayer sublayer = currentLayer.getSubLayers().get(position);
			if(!sublayer.isMainSubLayer()){
				editedSubLayer = sublayer;
				showLayerPopupMenu(editedSubLayer);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	if(controllersSideBarView.isShown())
	    		controllersSideBarView.hide();
	        layersSideBarView.toggle();
	        break;
	    case R.id.project_ab_features:
	    	if(currentLayer != null && currentSubLayer != null){
		    	if(layersSideBarView.isShown()){
		    		layersSideBarView.hide();
		    	}
		    	controllersSideBarView.toggle();
	    	}else{
	    		new MessageDialog(this).showDialog(
	    				getString(R.string.choose_sublayer_msg_title), 
	    				getString(R.string.choose_sublayer_msg), 
	    				getResources().getDrawable(android.R.drawable.ic_dialog_alert));
	    	}
	    	break;
	    case R.id.project_add_sublayer_menu_item:
	    	if(currentLayer != null){
	    		showNewSubLayerDialog();
	    	}else{
	    		new MessageDialog(this).showDialog(
	    				getString(R.string.choose_layer_msg_title), 
	    				getString(R.string.choose_layer_msg), 
	    				getResources().getDrawable(android.R.drawable.ic_dialog_alert));
	    	}
	    	break;
	    case R.id.project_open_webapp_menu_item:
	    	
	    	MainApplication mainApp = (MainApplication)getApplication();
			WebServerState wSState = mainApp.getWebServerState();
			
			if(wSState.equals(WebServerState.STARTED)){
		    	openWebApp();
			}else{
				showWebAppStartDialog();
			}
	    	
	    	break;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	    return true;
	}

	private void showKNXConnectionStartDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.connection_not_started_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.connection_not_started_message));
		builder.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				startService(new Intent(getApplicationContext(), KNXConnectionService.class));
			}
		});
		builder.setNegativeButton(getString(android.R.string.no), new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.create().show();
	}
	
	private void showWebAppStartDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.webapp_server_not_started_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.webapp_server_not_started_message));
		builder.setPositiveButton(getString(android.R.string.yes), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				startService(new Intent(getApplicationContext(), WebServerService.class));
			}
		});
		builder.setNegativeButton(getString(android.R.string.no), new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.create().show();
	}
	
	private void openWebApp(){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(createProjectUrl()));
		startActivity(i);
	}
	
//	private void openWebAppInWebView(){
//		Intent intent = new Intent(this, WebAppActivity.class);
//		intent.putExtra(Project.PROJECT, (Parcelable)project);
//		startActivity(intent);
//	}
	
	private String createProjectUrl(){
		String url = "";
		WebServerSettings settings  = new WebServerSettings(this);
		url = String.format("http://localhost:%d/?project=%d", settings.getPort(), project.getId());	
		return url;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dbManager != null && !dbManager.isOpen()){
			dbManager.open();
		}
		initialiseSubLayersPaging();
		layersSideBarView.selectFirstItem();
	}
		
	@Override
	protected void onPause() {
		super.onPause();
		if(dbManager != null && dbManager.isOpen())
			dbManager.close();
		clearSubLayers();
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
		
		if(controllerDialog != null){
			if(controllerDialog.isShowing()){
				controllerDialog.dismiss();
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
		layerDialog.setOnLayerDialogApproveListener(this);
		layerDialog.setTitle(getString(R.string.dialog_new_layer_title));
	}
	
	private void showEditLayerDialog(Layer layer){
		layerDialog = new LayerDialog<Layer>(this, Layer.class, layer);
		layerDialog.show();
		layerDialog.setOnLayerDialogApproveListener(this);
		layerDialog.setTitle(getString(R.string.dialog_edit_layer_title));
	}
	
	private void showNewSubLayerDialog(){
		layerDialog = new LayerDialog<SubLayer>(this, SubLayer.class);
		layerDialog.show();
		layerDialog.setOnLayerDialogApproveListener(this);
		layerDialog.setTitle(getString(R.string.dialog_new_sublayer_title));
	}
	
	private void showEditSubLayerDialog(SubLayer layer){
		layerDialog = new LayerDialog<SubLayer>(this, SubLayer.class, layer);
		layerDialog.show();
		layerDialog.setOnLayerDialogApproveListener(this);
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
	
	
	private void showLayerPopupMenu(Layer layer){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem;
		PopupMenuItem deleteItem; 
		if(layer instanceof SubLayer){
			editItem = new PopupMenuItem(POPUP_MENU_ITEM_SUBLAYER_EDIT, getString(R.string.sublayer_popup_menu_item_edit), R.drawable.edit_icon);
			deleteItem = new PopupMenuItem(POPUP_MENU_ITEM_SUBLAYER_DELETE, getString(R.string.sublayer_popup_menu_item_delete), R.drawable.trash_icon);
		} else{
			editItem = new PopupMenuItem(POPUP_MENU_ITEM_LAYER_EDIT, getString(R.string.layer_popup_menu_item_edit), R.drawable.edit_icon);
			deleteItem = new PopupMenuItem(POPUP_MENU_ITEM_LAYER_DELETE, getString(R.string.layer_popup_menu_item_delete), R.drawable.trash_icon);
		}
		items.add(editItem);
		items.add(deleteItem);
		
		layerPopupMenu = new PopupMenuDialog(this,layer.getName(), items);
		layerPopupMenu.setPopupMenuItemListener(this);
		layerPopupMenu.show();
	}
	
	private void showControllerPopupMenu(Controller controller){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem;
		PopupMenuItem deleteItem; 
		editItem = new PopupMenuItem(POPUP_MENU_ITEM_ELEMENT_EDIT, getString(R.string.element_popup_menu_item_edit), R.drawable.edit_icon);
		deleteItem = new PopupMenuItem(POPUP_MENU_ITEM_ELEMENT_DELETE, getString(R.string.element_popup_menu_item_delete), R.drawable.trash_icon);
		items.add(editItem);
		items.add(deleteItem);
		
		editedController = controller;
		ControllerType cType = controller.getType();
		
		editedElement = controller.getElement();
		elementPopupMenu = new PopupMenuDialog(this, editedElement.getName(), items);
		elementPopupMenu.setPopupMenuItemListener(this);
		elementPopupMenu.show();
		elementPopupMenu.setIcon(cType.getIcon());
		
	}
	
	public void onPopupMenuItemClick(int position, PopupMenuItem item) {
		switch(item.getId()){
			case POPUP_MENU_ITEM_LAYER_EDIT:
				showEditLayerDialog(editedLayer);
				break;
			case POPUP_MENU_ITEM_LAYER_DELETE:
				showDeleteConfirmation(editedLayer);
				break;
			case POPUP_MENU_ITEM_SUBLAYER_EDIT:
				showEditSubLayerDialog(editedSubLayer);
				break;
			case POPUP_MENU_ITEM_SUBLAYER_DELETE:
				showDeleteConfirmation(editedSubLayer);
				break;
			case POPUP_MENU_ITEM_ELEMENT_EDIT:
				showControllerDialog(editedController.getType(), editedElement);
				break;
			case POPUP_MENU_ITEM_ELEMENT_DELETE:
				removeController(editedController);
				break;
		}
	}
	
	private void showControllerDialog(ControllerType controllerType, Element element){
		if(element != null){
			controllerDialog = new ControllerDialog(this, element);
		} else{ 
			controllerDialog = new ControllerDialog(this, controllerType.getAddressTypes());
		}
				
		controllerDialog.show();
		controllerDialog.setTitle(controllerType.getTitle(this));
		controllerDialog.setTitleIcon(controllerType.getIcon());
		controllerDialog.setType(controllerType);
		controllerDialog.setOnControllerDialogApproveListener(this);
	}
	
	private void showDeleteConfirmation(final Layer layer){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.layer_delete_confirmation_title));
		builder.setIcon(getResources().getDrawable(R.drawable.trash_icon));
		if(layer instanceof SubLayer){
			builder.setMessage(getString(R.string.sublayer_delete_confirmation_text, layer.getName()));
		}else{
			builder.setMessage(getString(R.string.layer_delete_confirmation_text, layer.getName()));	
		}
		builder.setPositiveButton(getString(R.string.yes), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if(layer instanceof SubLayer){
					removeSubLayer((SubLayer)layer);
				}else{
					removeLayer(layer);	
				}
			}
		});
		builder.setNegativeButton(getString(R.string.no), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				editedLayer = null;
			}
		});
		builder.create().show();
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
			
	public void onLayerDialogAddAction(Layer layer, Class<?> clazz) {
		if(clazz.getName().equals(Layer.class.getName())){
			layer.setProjectId(project.getId());
			dbManager.addLayer(layer);
			project.getLayers().add(layer);
			layersSideBarView.addItem(createLayerSideBarItem(layer));
		} else if(clazz.getName().equals(SubLayer.class.getName())){
			SubLayer subLayer = (SubLayer)layer;
			subLayer.setBackgroundImage("");
			subLayer.setProjectId(project.getId());
			subLayer.setLayerId(currentLayer.getId());
			dbManager.addSubLayer(subLayer);
			currentLayer.getSubLayers().add(subLayer);
			addSubLayer(subLayer);
		}
	}

	public void onLayerDialogEditAction(Layer layer, Class<?> clazz) {
		if(clazz.getName().equals(Layer.class.getName())){
			dbManager.updateLayer(layer);
			int index = findSideBarLayerIndex(layer);
			layersSideBarView.setItem(createLayerSideBarItem(layer), index);
			editedLayer = null;
		} else if(clazz.getName().equals(SubLayer.class.getName())){
			SubLayer subLayer = (SubLayer)layer;
			int position = currentLayer.getSubLayers().indexOf(editedSubLayer);	
			dbManager.updateSubLayer(subLayer);
			editedSubLayer = null;
			tabBar.updateTab(position, subLayer.getName(),getResourceIdByName(subLayer.getIcon()));
			Bundle bundle = new Bundle();
			bundle.putParcelable(SubLayer.SUBLAYER, subLayer);
			subLayerPagerAdapter.updatePage(position, Fragment.instantiate(this, SubLayerFragment.class.getName(), bundle));
		}
	}
	
	private boolean removeLayer(Layer layer){
		dbManager.removeLayer(layer);
		int index = findSideBarLayerIndex(layer);
		layersSideBarView.removeItem(index);
		if(editedLayer.getId() == currentLayer.getId()){
			currentLayer = null;
		}
		editedLayer = null;
		return false;
	}
		
	private boolean removeSubLayer(SubLayer layer){
		int position = currentLayer.getSubLayers().indexOf(layer);
		dbManager.removeSubLayer(layer);
		if(editedSubLayer.getId() == currentSubLayer.getId()){
			currentSubLayer = null;
		}
		editedSubLayer = null;
		
		currentLayer.getSubLayers().remove(position);
		tabBar.removeTab(position);
		subLayerPagerAdapter.removePage(subLayerPagerAdapter.getItem(position));

		return false;
	}
	
	public void onControllerDialogAddAction(Element element) {		
		element.setProjectId(project.getId());
		element.setLayerId(currentLayer.getId());
		element.setSubLayerId(currentSubLayer.getId());
		dbManager.addElement(element);
		addElementToSubLayerFragment(element);
	}
	
	private void addElementToSubLayerFragment(Element element){
		ControllerAdapter adapter = getControllerAdapter();
		ControllerType elementType = ControllerType.valueOf(element.getType());
		adapter.add(elementType.createView(this, element));
		adapter.notifyDataSetChanged();
	}
	
	public void onControllerDialogEditAction(Element element) {
		dbManager.updateElement(element);
		
		ControllerAdapter adapter = getControllerAdapter();
		editedController.setElement(element);
		adapter.notifyDataSetChanged();
		
		editedElement = null;
		editedController = null;
	}
	
	private void removeController(Controller controller){
		dbManager.removeElement(controller.getElement());
		
		ControllerAdapter adapter = getControllerAdapter();
		controller.onPause();
		adapter.remove(controller);
		adapter.notifyDataSetChanged();
		
		editedElement = null;
		editedController = null;
	}
	
	private ControllerAdapter getControllerAdapter(){
		int position = currentLayer.getSubLayers().indexOf(currentSubLayer);
		SubLayerFragment fragment = (SubLayerFragment)subLayerPagerAdapter.getItem(position);
		ControllerAdapter adapter = (ControllerAdapter)fragment.getListAdapter();
		return adapter;
	}
}
