package pl.marek.knx;

import pl.marek.knx.database.Project;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class ProjectActivity extends Activity implements OnGestureListener{
	
	private Project project;
	
	private GestureDetector detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		
		project = getIntent().getParcelableExtra(Project.PROJECT);
		detector = new GestureDetector(this, this);
		
		Log.i("Project name: ", project.getName());
	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
			
		return detector.onTouchEvent(event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(project != null){
			getActionBar().setTitle(project.getName());
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		//Log.d("down", "down");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		
		int accept_move = 100;
		
		float move = e2.getX()-e1.getX();
		
		if(Math.abs(move)>= accept_move){
			if(move > 0){
				Log.d("MOVE","right");
			} else{
				Log.d("MOVE", "left");
			}
			
			
			return true;
		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}


}
