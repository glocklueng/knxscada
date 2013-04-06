package pl.marek.knx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import pl.marek.knx.PopupMenuItem;
import pl.marek.knx.PopupMenuDialog.PopupMenuItemListener;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.preferences.SettingsActivity;
import pl.marek.knx.utils.LoadScaledImageFromPath;
import pl.marek.knx.utils.MessageDialog;
import pl.marek.knx.utils.ProjectComparator;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProjectsActivity extends ListActivity implements OnItemLongClickListener, PopupMenuItemListener{
	
	public static final int SELECT_IMAGE = 1;
	public static final int POPUP_MENU_ITEM_EDIT = 100;
	public static final int POPUP_MENU_ITEM_DELETE = 101;
	public static final int POPUP_MENU_ITEM_CHOOSE_IMAGE = 102;
	public static final int POPUP_MENU_ITEM_REMOVE_IMAGE = 103;
	
	
	public static final String SHOW_PROJECT_POPUPMENU = "show+project_popup_menu";
	
	
	private ProjectAdapter projectAdapter;
	private DatabaseManager dbManager;
	private LinkedList<Project> projects;
	private ProjectDialog projectDialog;
	private PopupMenuDialog projectPopupMenu;
	private AlertDialog projectDeleteConfirmation;
	private Project editedProject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list);
		getActionBar().setHomeButtonEnabled(true);
		
		dbManager = new DatabaseManagerImpl(this);
		projects = new LinkedList<Project>(dbManager.getAllProjects());
		projectAdapter = new ProjectAdapter(this, getListView(), projects);
		setListAdapter(projectAdapter);
		getListView().setOnItemLongClickListener(this);
		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
		
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
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	   switch (item.getItemId()) 
	   {        
	      case android.R.id.home:            
	         Intent intent = new Intent(this, ProjectsActivity.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);
	         break;
	      case R.id.ab_telegram_item:
	    	  
	    	  startActivity(new Intent(this, TelegramActivity.class));
	    	  
	    	  break;
	      case R.id.ab_preferences_item:
	    	  
	    	  startActivity(new Intent(this, SettingsActivity.class));
	    	  
	    	  break;
	      case R.id.ab_new_project_item:
	    	  
	    	  showProjectDialog(null, null, null, null);
	    	  
	    	  
	    	  break;
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	   return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Project project = projectAdapter.getItem(position);
		Intent intent = new Intent(this, ProjectActivity.class);
		intent.putExtra(Project.PROJECT, project);
		startActivity(intent);
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
		Project project = projectAdapter.getItem(position);
		showProjectPopupMenu(project);
		return false;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(projectDialog != null){
			if(projectDialog.isShowing()){
				outState.putString(ProjectDialog.PROJECT_NAME, projectDialog.getName());
				outState.putString(ProjectDialog.PROJECT_DESCRIPTION, projectDialog.getDescription());
				outState.putString(ProjectDialog.PROJECT_IMAGE, projectDialog.getImagePath());
				outState.putParcelable(Project.PROJECT, projectDialog.getProject());
				projectDialog.dismiss();
			}
		}
		if(projectPopupMenu != null){
			if(projectPopupMenu.isShowing()){
				outState.putParcelable(Project.PROJECT, editedProject);
				outState.putBoolean(SHOW_PROJECT_POPUPMENU, true);
				projectPopupMenu.dismiss();
			}
		}
		
		if(projectDeleteConfirmation != null){
			projectDeleteConfirmation.dismiss();
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		if(state != null){
			String name = state.getString(ProjectDialog.PROJECT_NAME);
			String description = state.getString(ProjectDialog.PROJECT_DESCRIPTION);
			String imagePath = state.getString(ProjectDialog.PROJECT_IMAGE);
			Project dialogProject = state.getParcelable(Project.PROJECT);
			if(name != null){
				showProjectDialog(name, description, imagePath, dialogProject);
			}
			
			boolean showProjectPopupMenu = state.getBoolean(SHOW_PROJECT_POPUPMENU);
			if(showProjectPopupMenu){
				Project popupProject = state.getParcelable(Project.PROJECT);
				showProjectPopupMenu(popupProject);
			}
		}
	}
	
	public void showProjectDialog(String name, String description, String imagePath,  Project project){
		projectDialog = new ProjectDialog(this, project);
		projectDialog.show();
		if(name != null)
			projectDialog.setName(name);
		if(description != null)
			projectDialog.setDescription(description);
		if(description != null)
			projectDialog.setImage(imagePath);
	}
	
	public void showProjectPopupMenu(Project project){
		editedProject = project;
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem editItem = new PopupMenuItem(POPUP_MENU_ITEM_EDIT, getString(R.string.project_popup_menu_item_edit), R.drawable.edit_icon);
		PopupMenuItem deleteItem = new PopupMenuItem(POPUP_MENU_ITEM_DELETE, getString(R.string.project_popup_menu_item_delete), R.drawable.trash_icon);
		items.add(editItem);
		items.add(deleteItem);
		
		projectPopupMenu = new PopupMenuDialog(this,project.getName(), items);
		projectPopupMenu.setPopupMenuItemListener(this);
		projectPopupMenu.show();
		
	}
	
	public void showImagePopupMenu(){
		ArrayList<PopupMenuItem> items = new ArrayList<PopupMenuItem>();
		PopupMenuItem chooseItem = new PopupMenuItem(POPUP_MENU_ITEM_CHOOSE_IMAGE, getString(R.string.image_popup_menu_item_choose), R.drawable.new_item);
		PopupMenuItem removeItem = new PopupMenuItem(POPUP_MENU_ITEM_REMOVE_IMAGE, getString(R.string.image_popup_menu_item_remove), R.drawable.trash_icon);
		items.add(chooseItem);
		items.add(removeItem);
		
		projectPopupMenu = new PopupMenuDialog(this,getString(R.string.image_popup_menu_title), items);
		projectPopupMenu.setPopupMenuItemListener(this);
		projectPopupMenu.show();
	}
	
	@Override
	public void onPopupMenuItemClick(int position, PopupMenuItem item) {
		switch(item.getId()){
			case POPUP_MENU_ITEM_EDIT:
				showProjectDialog(null, null,null, editedProject);
				break;
			case POPUP_MENU_ITEM_DELETE:
				showDeleteConfirmation(editedProject);
				break;
			case POPUP_MENU_ITEM_CHOOSE_IMAGE:
				selectImage();
				break;
			case POPUP_MENU_ITEM_REMOVE_IMAGE:
				if(projectDialog != null && projectDialog.isShowing()){
					projectDialog.setImage(null);
				}
				break;
		}
	}
	
	public void showDeleteConfirmation(final Project project){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialogConfirmTheme);
		builder.setTitle(getString(R.string.project_delete_confirmation_title));
		builder.setIcon(getResources().getDrawable(R.drawable.trash_icon));
		builder.setMessage(getString(R.string.project_delete_confirmation_text, project.getName()));
		builder.setPositiveButton(getString(R.string.yes), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteProject(project);
			}
		});
		builder.setNegativeButton(getString(R.string.no), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				editedProject = null;
			}
		});
		projectDeleteConfirmation = builder.create();
		projectDeleteConfirmation.show();
	}
	
	public boolean addNewProject(){
		
		if(!projectConditionCheck())
			return false;
		
		Project project = new Project();
		project.setName(projectDialog.getName());
		project.setDescription(projectDialog.getDescription());
		project.setImage(projectDialog.getImagePath());
		
		//TODO Przemyśleć czy pole author ma sens, i ewentualnie skąd je pobrać
		project.setAuthor("");
		
		projects.add(project);
		Collections.sort(projects, new ProjectComparator());
		projectAdapter.setAnimatedProject(project);
		projectAdapter.notifyDataSetChanged();
		
		dbManager.addProject(project);
		return true;
	}
	
	public boolean editProject(Project project){
		if(!projectConditionCheck())
			return false;
		dbManager.updateProject(project);
		projects.set(findProjectIndex(project), project);
		projectAdapter.setAnimatedProject(project);
		projectAdapter.notifyDataSetChanged();
		editedProject = null;
		return true;
	}
	
	private boolean projectConditionCheck(){
		if(projectDialog.getName().isEmpty()){
			return false;
		}
		return true;
	}
	
	private int findProjectIndex(Project project){
		for(int i=0; i < projects.size(); i++){
			Project p = projects.get(i);
			if(p.getId() == project.getId()){
				return i; 
			}
		}
		return -1;
	}
	
	public void deleteProject(Project project){
		dbManager.removeProject(project);
		projectAdapter.delete(projectAdapter.getItem(findProjectIndex(project)));
		projects.remove(project);
		editedProject = null;
	}
	
	public void selectImage(){
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), SELECT_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_IMAGE) {
		        Uri selectedImageUri = data.getData();
		        String selectedImagePath = getImagePath(selectedImageUri);
		        Log.i("IMAGE", selectedImagePath);
		        if(projectDialog != null){
		        	projectDialog.show();
		        	projectDialog.setImage(selectedImagePath);
		        }
		    }
		}
	}
	
    public String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };        
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
		
	public class ProjectDialog extends BaseDialog implements View.OnClickListener{
		
		public static final String PROJECT_NAME = "project_name";
		public static final String PROJECT_DESCRIPTION = "project_description";
		public static final String PROJECT_IMAGE = "dialog_project_image";
		
		private TextView nameView;
		private TextView descView;
		private ImageView imageView;
		private TextView imageHint;
		private Button addButton;
		private Button cancelButton;
		private ProgressBar imageProgressBar;
		
		private Project project;
		private String imagePath;

		public ProjectDialog(Context context, Project project) {
			super(context, R.style.dialogTheme);
			this.project = project;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.project_dialog);
	        
			setDialogSize();
			
			TextView titleView = (TextView)findViewById(R.id.dialog_title_text);
			nameView = (TextView)findViewById(R.id.dialog_new_project_name);
			descView = (TextView)findViewById(R.id.dialog_new_project_description);	
			imageView = (ImageView)findViewById(R.id.dialog_new_project_image);
			imageHint = (TextView)findViewById(R.id.dialog_new_project_image_hint);
			imageProgressBar = (ProgressBar)findViewById(R.id.dialog_new_project_image_progressbar);
			imagePath = "";
			
			addButton = (Button)findViewById(R.id.dialog_project_add_button);
			cancelButton = (Button)findViewById(R.id.dialog_new_project_cancel_button);
			addButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
			imageView.setOnClickListener(this);
			imageHint.setOnClickListener(this);
			
			if(project != null){
				titleView.setText(getString(R.string.dialog_edit_project_title));
				addButton.setText(getString(R.string.dialog_project_edit_button));
				nameView.setText(project.getName());
				descView.setText(project.getDescription());
				setImage(project.getImage());
			}else{
				titleView.setText(getString(R.string.dialog_new_project_title));
			}
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
		
		public void setImage(String path){
			if(path != null && !path.equals("")){
				imageHint.setVisibility(View.GONE);
				new LoadScaledImageFromPath(imageView, imageProgressBar, 200, 200).execute(path);
				imagePath = path;
			} else{
				imageHint.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(null);
				imageView.setVisibility(View.GONE);
				imagePath = "";
			}
		}
		
		public String getImagePath(){
			return imagePath;
		}
		
		public Project getProject(){
			return project;
		};

		@Override
		public void onClick(View v) {
			boolean state = true;
			if(v.equals(addButton)){
				if(project == null){
					state = addNewProject();
				} else{
					project.setName(getName());
					project.setDescription(getDescription());
					project.setImage(getImagePath());
					state = editProject(project);
				}
				if(state){
					dismiss();
				} else{
					new MessageDialog(getContext()).showDialog(getString(R.string.project_name_empty_title), 
																getString(R.string.project_name_empty_text), 
																getResources().getDrawable(android.R.drawable.ic_dialog_alert));
					
				}
			} else if(v.equals(cancelButton)){
				cancel();
			} else if(v.equals(imageHint)){
				selectImage();
			} else if(v.equals(imageView)){
				showImagePopupMenu();
			}
		}
	}
}
