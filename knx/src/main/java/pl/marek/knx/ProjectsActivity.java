package pl.marek.knx;

import java.util.Collections;
import java.util.LinkedList;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.preferences.SettingsActivity;
import pl.marek.knx.utils.MessageDialog;
import pl.marek.knx.utils.ProjectComparator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class ProjectsActivity extends ListActivity implements OnItemLongClickListener{
	
	private ProjectAdapter projectAdapter;
	private DatabaseManager dbManager;
	private LinkedList<Project> projects;
	private ProjectDialog projectDialog;
	private ProjectPopupMenu projectPopupMenu;
	private AlertDialog projectDeleteConfirmation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setHomeButtonEnabled(true);
		
		dbManager = new DatabaseManagerImpl(this);
		projects = new LinkedList<Project>(dbManager.getAllProjects());
		projectAdapter = new ProjectAdapter(this, projects);
		setListAdapter(projectAdapter);
		getListView().setOnItemLongClickListener(this);
		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
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
	    	  
	    	  showProjectDialog(null, null, null);
	    	  
	    	  
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
				outState.putParcelable(ProjectDialog.PROJECT_OBJECT, projectDialog.getProject());
				projectDialog.dismiss();
			}
		}
		if(projectPopupMenu != null){
			if(projectPopupMenu.isShowing()){
				outState.putParcelable(ProjectPopupMenu.PROJECT_OBJECT, projectPopupMenu.getProject());
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
			Project dialogProject = state.getParcelable(ProjectDialog.PROJECT_OBJECT);
			if(name != null){
				showProjectDialog(name, description, dialogProject);
			}
			
			Project popupProject = state.getParcelable(ProjectPopupMenu.PROJECT_OBJECT);
			if(popupProject != null){
				showProjectPopupMenu(popupProject);
			}
		}
	}
	
	public void showProjectDialog(String name, String description, Project project){
		projectDialog = new ProjectDialog(this, project);
		projectDialog.show();
		if(name != null)
			projectDialog.setName(name);
		if(description != null)
			projectDialog.setDescription(description);
	}
	
	public void showProjectPopupMenu(Project project){
		projectPopupMenu = new ProjectPopupMenu(this, project);
		projectPopupMenu.show();
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
		
		//TODO Przemyśleć czy pole author ma sens, i ewentualnie skąd je pobrać
		project.setAuthor("");
		
		projects.add(project);
		Collections.sort(projects, new ProjectComparator());
		projectAdapter.notifyDataSetChanged();
		
		dbManager.addProject(project);
		return true;
	}
	
	public boolean editProject(Project project){
		if(!projectConditionCheck())
			return false;
		
		dbManager.updateProject(project);
		projects.set(findProjectIndex(project), project);
		projectAdapter.notifyDataSetChanged();
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
		projects.remove(project);
		projectAdapter.notifyDataSetChanged();
	}
		
	public class ProjectDialog extends Dialog implements View.OnClickListener{
		
		public static final String PROJECT_NAME = "project_name";
		public static final String PROJECT_DESCRIPTION = "project_description";
		public static final String PROJECT_OBJECT = "dialog_project_object";
		
		private TextView nameView;
		private TextView descView;
		private Button addButton;
		private Button cancelButton;
		private Project project;

		public ProjectDialog(Context context, Project project) {
			super(context, R.style.dialogTheme);
			this.project = project;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.project_dialog);
	        
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = (int)(metrics.widthPixels * 0.95f);
			getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
			
			TextView titleView = (TextView)findViewById(R.id.dialog_project_title);
			nameView = (TextView)findViewById(R.id.dialog_new_project_name);
			descView = (TextView)findViewById(R.id.dialog_new_project_description);			
			
			addButton = (Button)findViewById(R.id.dialog_project_add_button);
			cancelButton = (Button)findViewById(R.id.dialog_new_project_cancel_button);
			addButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
			
			if(project != null){
				titleView.setText(getString(R.string.dialog_edit_project_title));
				addButton.setText(getString(R.string.dialog_project_edit_button));
				nameView.setText(project.getName());
				descView.setText(project.getDescription());
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
					state = editProject(project);
				}
				if(state){
					dismiss();
				} else{
					new MessageDialog(getContext()).showDialog(getString(R.string.project_name_empty_title), 
																getString(R.string.project_name_empty_text), 
																getResources().getDrawable(android.R.drawable.ic_dialog_alert));
					
				}
			}else if(v.equals(cancelButton)){
				cancel();
			}
		}
	}
	
	public class ProjectPopupMenu extends Dialog implements OnItemClickListener{
		
		private static final String PROJECT_OBJECT = "popup_menu_project_object";
		
		private Project project;
		private PopupMenuListAdapter adapter;

		public ProjectPopupMenu(Context context, Project project) {
			super(context, R.style.dialogTheme);
			this.project = project;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.project_popup_menu);
			
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = (int)(metrics.widthPixels * 0.95f);
			getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
			
			TextView titleView = (TextView)findViewById(R.id.dialog_project_title);
			ListView listView = (ListView)findViewById(android.R.id.list);
			
			String[] items = new String[]{
					getString(R.string.project_popup_menu_item_edit),
					getString(R.string.project_popup_menu_item_delete)
			};
			
			titleView.setText(project.getName());
			adapter = new PopupMenuListAdapter(getContext(), items);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		
		public Project getProject(){
			return project;
		} 

		@Override
		public void onItemClick(AdapterView<?> v, View view, int position, long id) {
			String item = adapter.getItem(position);
			if(item.equals(getString(R.string.project_popup_menu_item_edit))){
				showProjectDialog(null, null, project);
			}else if(item.equals(getString(R.string.project_popup_menu_item_delete))){
				showDeleteConfirmation(project);
			}
			this.dismiss();
		}
	}
		
	public class PopupMenuListAdapter extends ArrayAdapter<String>{
		
		private LayoutInflater inflater;
		
		public PopupMenuListAdapter(Context context, String[] items) {
			super(context, android.R.layout.simple_list_item_single_choice, items);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			String itemName = getItem(position);
			
			View view = inflater.inflate(R.layout.project_popup_menu_item, parent, false);
			
			ImageView iconView = (ImageView)view.findViewById(R.id.project_popup_menu_item_icon);
			TextView nameView = (TextView)view.findViewById(R.id.project_popup_menu_item_name);
			
			nameView.setText(itemName);
			Drawable icon = getItemIcon(itemName);
			if(icon != null){
				iconView.setImageDrawable(icon);
			}
			return view;
		}
		
		private Drawable getItemIcon(String itemName){
			if(itemName.equals(getString(R.string.project_popup_menu_item_edit))){
				return getResources().getDrawable(R.drawable.edit_icon);
			}else if(itemName.equals(getString(R.string.project_popup_menu_item_delete))){
				return getResources().getDrawable(R.drawable.trash_icon);
			}
			return null;
		}
	}
}
