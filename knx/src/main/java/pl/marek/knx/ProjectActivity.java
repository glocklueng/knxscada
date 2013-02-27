package pl.marek.knx;

import pl.marek.knx.SideBarItem;
import pl.marek.knx.SideBarView.SideBarListener;
import pl.marek.knx.SideBarView.SideBarMode;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class ProjectActivity extends Activity implements SideBarListener{
	
	private Project project;
	private DatabaseManager dbManager;
	private SideBarView leftSideBarView;
	private SideBarView rightSideBarView;
	private GestureDetector leftGestureDetector;
	private GestureDetector rightGestureDetector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		
		dbManager = new DatabaseManagerImpl(this);
		
		Project intentProject = getIntent().getParcelableExtra(Project.PROJECT);
		project = dbManager.getProjectByIdWithDependencies(intentProject.getId());
		
		setActionBar();
		setSideBar();
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
	
	private void setSideBar(){
	    leftSideBarView = (SideBarView) findViewById(R.id.left_side_navigation_view);
	    leftSideBarView.setSideBarListener(this);
	    leftSideBarView.setMode(SideBarMode.LEFT);
	    
	    for(int i=1;i< 100; i++){
	    	SideBarItem item = new SideBarItem();
		    item.setId(i);
		    item.setName(String.format("Item %d", i));
	    	leftSideBarView.addItem(item);
	    }
	    
	    rightSideBarView = (SideBarView) findViewById(R.id.right_side_navigation_view);
	    rightSideBarView.setSideBarListener(this);
	    rightSideBarView.setMode(SideBarMode.RIGHT);
	    
	    for(int i=1;i< 100; i++){
	    	SideBarItem item = new SideBarItem();
		    item.setId(i);
		    item.setName(String.format("Feature %d", i));
	    	rightSideBarView.addItem(item);
	    }
	    
	    leftGestureDetector = new GestureDetector(this, leftSideBarView);
	    rightGestureDetector = new GestureDetector(this, rightSideBarView);
	}
	
	@Override
	public void onSideBarItemClick(SideBarItem item) {
		Log.d("12345", String.format("%s", item.getName()));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		leftGestureDetector.onTouchEvent(event);
		rightGestureDetector.onTouchEvent(event);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	if(rightSideBarView.isShown())
	    		rightSideBarView.hide();
	        leftSideBarView.toggle();
	        break;
	    case R.id.project_ab_features:
	    	if(leftSideBarView.isShown())
	    		leftSideBarView.hide();
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

}
