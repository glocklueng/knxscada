package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.ControllerDialog.OnControllerDialogApproveListener;
import pl.marek.knx.CustomTabBar.OnTabLongClickListener;
import pl.marek.knx.LayerDialog.OnLayerDialogApproveListener;
import pl.marek.knx.SideBarItem;
import pl.marek.knx.PopupMenuDialog.PopupMenuItemListener;
import pl.marek.knx.SideBarView.SideBarListener;
import pl.marek.knx.SideBarView.SideBarMode;
import pl.marek.knx.controls.Controller;
import pl.marek.knx.controls.OnOffSwitch;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.DrawableUtils;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	
	private static final int NEW_LAYER_ITEM_ID = 0;
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
		

	}
	
	@Override
	protected void onStart() {
		super.onStart();

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
	
	private void setControllersSideBar(){

	    controllersSideBarView = (SideBarView) findViewById(R.id.right_side_navigation_view);
	    controllersSideBarView.setSideBarListener(this);
	    controllersSideBarView.setMode(SideBarMode.RIGHT);
	    
	    for(int i=1;i< 10; i++){
	    	SideBarItem item = new SideBarItem();
		    item.setId(i);
		    item.setName(String.format("Feature %d", i));
		    item.setIcon(R.drawable.room_sofa_icon);
	    	controllersSideBarView.addItem(item);
	    }
	    
	}
		
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
		Controller c = (Controller)view;
		showControllerPopupMenu(c);
		return false;
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
			OnOffSwitch s = new OnOffSwitch(this, null);
			showControllerDialog(s, null);
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
		if(layersGestureDetector.onTouchEvent(event))
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
	    	if(controllersSideBarView.isShown())
	    		controllersSideBarView.hide();
	        layersSideBarView.toggle();
	        break;
	    case R.id.project_ab_features:
	    	if(layersSideBarView.isShown())
	    		layersSideBarView.hide();
	    	controllersSideBarView.toggle();
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
	
	
	public void showLayerPopupMenu(Layer layer){
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
	
	public void showControllerPopupMenu(Controller controller){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem;
		PopupMenuItem deleteItem; 
		editItem = new PopupMenuItem(POPUP_MENU_ITEM_ELEMENT_EDIT, getString(R.string.element_popup_menu_item_edit), R.drawable.edit_icon);
		deleteItem = new PopupMenuItem(POPUP_MENU_ITEM_ELEMENT_DELETE, getString(R.string.element_popup_menu_item_delete), R.drawable.trash_icon);
		items.add(editItem);
		items.add(deleteItem);
		
		editedController = controller;
		editedElement = controller.getElement();
		elementPopupMenu = new PopupMenuDialog(this, editedElement.getName(), items);
		elementPopupMenu.setPopupMenuItemListener(this);
		elementPopupMenu.show();
		elementPopupMenu.setIcon(controller.getIcon());
		
	}
	
	@Override
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
				showControllerDialog(editedController, editedElement);
				break;
			case POPUP_MENU_ITEM_ELEMENT_DELETE:
				removeElement(editedElement);
				break;
		}
	}
	
	private void showControllerDialog(Controller controller, Element element){
		if(element != null){
			controllerDialog = new ControllerDialog(this, true, element);
		} else{
			controllerDialog = new ControllerDialog(this, element);
		}
		controllerDialog.show();
		controllerDialog.setTitle(controller.getName());
		controllerDialog.setTitleIcon(controller.getIcon());
		controllerDialog.setType(controller.getType());
		controllerDialog.setOnControllerDialogApproveListener(this);
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
			
	@Override
	public void onLayerDialogAddAction(Layer layer, Class<?> clazz) {
		if(clazz.getName().equals(Layer.class.getName())){
			layer.setProjectId(project.getId());
			dbManager.addLayer(layer);
			project.getLayers().add(layer);
			layersSideBarView.addItem(createSideBarItem(layer));
		} else if(clazz.getName().equals(SubLayer.class.getName())){
			SubLayer subLayer = (SubLayer)layer;
			subLayer.setProjectId(project.getId());
			subLayer.setLayerId(currentLayer.getId());
			dbManager.addSubLayer(subLayer);
			currentLayer.getSubLayers().add(subLayer);
			addSubLayer(subLayer);
		}
	}

	@Override
	public void onLayerDialogEditAction(Layer layer, Class<?> clazz) {
		if(clazz.getName().equals(Layer.class.getName())){
			dbManager.updateLayer(layer);
			int index = findSideBarLayerIndex(layer);
			layersSideBarView.setItem(createSideBarItem(layer), index);
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
	
	public boolean removeLayer(Layer layer){
		dbManager.removeLayer(layer);
		int index = findSideBarLayerIndex(layer);
		layersSideBarView.removeItem(index);
		editedLayer = null;
		return false;
	}
		
	public boolean removeSubLayer(SubLayer layer){
		int position = currentLayer.getSubLayers().indexOf(layer);
		dbManager.removeSubLayer(layer);
		editedSubLayer = null;
		
		tabBar.removeTab(position);
		subLayerPagerAdapter.removePage(subLayerPagerAdapter.getItem(position));

		return false;
	}
	
	@Override
	public void onControllerDialogAddAction(Element element) {		
		element.setProjectId(project.getId());
		element.setLayerId(currentLayer.getId());
		element.setSubLayerId(currentSubLayer.getId());
		dbManager.addElement(element);
		addElementToSubLayerFragment(element);
	}
	
	private void addElementToSubLayerFragment(Element element){
		ElementAdapter adapter = getElementAdapter();
		adapter.add(element);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onControllerDialogEditAction(Element element) {
		dbManager.updateElement(element);
		
		ElementAdapter adapter = getElementAdapter();
		adapter.notifyDataSetChanged();
		
		editedElement = null;
		editedController = null;
	}
	
	public void removeElement(Element element){
		dbManager.removeElement(element);
		
		ElementAdapter adapter = getElementAdapter();
		adapter.remove(element);
		adapter.notifyDataSetChanged();
		
		editedElement = null;
		editedController = null;
	}
	
	private ElementAdapter getElementAdapter(){
		int position = currentLayer.getSubLayers().indexOf(currentSubLayer);
		SubLayerFragment fragment = (SubLayerFragment)subLayerPagerAdapter.getItem(position);
		ElementAdapter adapter = (ElementAdapter)fragment.getListAdapter();
		return adapter;
	}
}
