package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.SideBarItem;
import pl.marek.knx.PopupMenuDialog.PopupMenuItemListener;
import pl.marek.knx.SideBarView.SideBarListener;
import pl.marek.knx.SideBarView.SideBarMode;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.MessageDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ProjectActivity extends Activity implements SideBarListener, PopupMenuItemListener{
	
	private Project project;
	private DatabaseManager dbManager;
	private SideBarView layersSideBarView;
	private GestureDetector layersGestureDetector;
	private SideBarView rightSideBarView;
	private GestureDetector rightGestureDetector;
	
	private LayerDialog layerDialog;
	private PopupMenuDialog layerPopupMenu;
	private Layer editedLayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		
		dbManager = new DatabaseManagerImpl(this);
		
		Project intentProject = getIntent().getParcelableExtra(Project.PROJECT);
		project = dbManager.getProjectByIdWithDependencies(intentProject.getId());
		
		setActionBar();
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
		if(project != null){
			getActionBar().setTitle(project.getName());
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	    addItem.setId(0);
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
			if(item.getId() == 0){
				showLayerDialog(null, null, null);
			} else{
				//TODO Dodać przełączanie pięter po kliknięciu
			}
		}
	}
	
	@Override
	public void onSideBarItemLongClick(SideBarView view, SideBarItem item) {
		if(view.equals(layersSideBarView)){
			if(item.getId() != 0){
				Layer layer = dbManager.getLayerById(item.getId());
				editedLayer = layer;
				showLayerPopupMenu(layer);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		layersGestureDetector.onTouchEvent(event);
		rightGestureDetector.onTouchEvent(event);
		return true;
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
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dbManager != null && !dbManager.isOpen())
			dbManager.open();
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
				outState.putString(LayerDialog.LAYER_NAME, layerDialog.getName());
				outState.putString(LayerDialog.LAYER_DESCRIPTION, layerDialog.getDescription());
				outState.putParcelable(LayerDialog.LAYER_OBJECT, layerDialog.getLayer());
				layerDialog.dismiss();
			}
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		if(state != null){
			String name = state.getString(LayerDialog.LAYER_NAME);
			String description = state.getString(LayerDialog.LAYER_DESCRIPTION);
			Layer dialogLayer = state.getParcelable(LayerDialog.LAYER_OBJECT);
			if(name != null){
				showLayerDialog(name, description, dialogLayer);
			}
		}
	}
	
	private void showLayerDialog(String name, String description, Layer layer){
		layerDialog = new LayerDialog(this, layer);
		layerDialog.show();
		if(name != null)
			layerDialog.setName(name);
		if(description != null)
			layerDialog.setDescription(description);
		
	}
	
	public void showLayerPopupMenu(Layer layer){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem = new PopupMenuItem(getString(R.string.layer_popup_menu_item_edit), getResources().getDrawable(R.drawable.edit_icon));
		PopupMenuItem deleteItem = new PopupMenuItem(getString(R.string.layer_popup_menu_item_delete), getResources().getDrawable(R.drawable.trash_icon));
		items.add(editItem);
		items.add(deleteItem);
		
		layerPopupMenu = new PopupMenuDialog(this,layer.getName(), items);
		layerPopupMenu.setPopupMenuItemListener(this);
		layerPopupMenu.show();
	}
	
	@Override
	public void onPopupMenuItemClick(int position, PopupMenuItem item) {
		if(item.getName().equals(getString(R.string.layer_popup_menu_item_edit))){
			showLayerDialog(null, null, editedLayer);
		}else if(item.getName().equals(getString(R.string.layer_popup_menu_item_delete))){
			showDeleteConfirmation(editedLayer);
		}
	}
	
	public void showDeleteConfirmation(final Layer layer){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.layer_delete_confirmation_title));
		builder.setIcon(getResources().getDrawable(R.drawable.trash_icon));
		builder.setMessage(getString(R.string.layer_delete_confirmation_text, layer.getName()));
		builder.setPositiveButton(getString(R.string.yes), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeLayer(layer);
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
		
	public boolean addLayer(String name, String description){
		if(!layerConditionCheck())
			return false;
		
		Layer layer = new Layer(project.getId(), name, description, "");
		dbManager.addLayer(layer);
		project.getLayers().add(layer);
		
		SideBarItem item = new SideBarItem();
		item.setName(name);
		item.setId(layer.getId());
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
	
	
	public class LayerDialog extends Dialog implements View.OnClickListener{
		
		public static final String LAYER_NAME = "layer_name";
		public static final String LAYER_DESCRIPTION = "layer_description";
		public static final String LAYER_OBJECT = "dialog_layer_object";
		
		private TextView nameView;
		private TextView descView;
		private Button addButton;
		private Button cancelButton;
		private Layer layer;

		public LayerDialog(Context context,  Layer layer) {
			super(context, R.style.dialogTheme);
			this.layer = layer;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layer_dialog);
	        
			setDialogSize();
			
			TextView titleView = (TextView)findViewById(R.id.dialog_title_text);
			nameView = (TextView)findViewById(R.id.dialog_new_layer_name);
			descView = (TextView)findViewById(R.id.dialog_new_layer_description);			
			
			addButton = (Button)findViewById(R.id.dialog_layer_add_button);
			cancelButton = (Button)findViewById(R.id.dialog_new_layer_cancel_button);
			addButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
			
			if(layer != null){
				titleView.setText(getString(R.string.dialog_edit_layer_title));
				addButton.setText(getString(R.string.dialog_layer_edit_button));
				nameView.setText(layer.getName());
				descView.setText(layer.getDescription());
			}else{
				titleView.setText(getString(R.string.dialog_new_layer_title));
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
		
		public String getDescription(){
			return descView.getText().toString();
		}
		
		public void setName(String name){
			nameView.setText(name);
		}
		
		public void setDescription(String description){
			descView.setText(description);
		}
		
		public Layer getLayer(){
			return layer;
		};

		@Override
		public void onClick(View v) {
			boolean state = true;
			if(v.equals(addButton)){
				if(layer == null){
					state = addLayer(getName(), getDescription());
				} else{
					layer.setProjectId(project.getId());
					layer.setName(getName());
					layer.setDescription(getDescription());
					state = editLayer(layer);
				}
				if(state){
					dismiss();
				} else{
					new MessageDialog(getContext()).showDialog(getString(R.string.layer_name_empty_title), 
																getString(R.string.layer_name_empty_text), 
																getResources().getDrawable(android.R.drawable.ic_dialog_alert));
					
				}
			}else if(v.equals(cancelButton)){
				cancel();
			}
		}
	}
}
