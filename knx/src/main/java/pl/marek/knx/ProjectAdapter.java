package pl.marek.knx;

import java.util.List;

import pl.marek.knx.database.Project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ProjectAdapter extends ArrayAdapter<Project>{
	
	private LayoutInflater inflater;
	private Context context;
	private Project animatedProject;
	private ListView listView;
	
	public ProjectAdapter(Context context, ListView listView, List<Project> projects) {
		super(context, android.R.layout.simple_list_item_single_choice, projects);
		this.context = context;
		this.listView = listView;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Project project = getItem(position);
		
		View view = inflater.inflate(R.layout.project_item, null);
		TextView nameView = null;
		TextView descriptionView = null;
		ImageView imageView = null;
		
		ViewHolder holder = (ViewHolder)view.getTag();
		if(holder != null){
			nameView = holder.getName();
			descriptionView = holder.getDescription();
			imageView = holder.getImage();
		} else{
			nameView = (TextView)view.findViewById(R.id.project_item_name);
			descriptionView = (TextView)view.findViewById(R.id.project_item_description);
			imageView = (ImageView)view.findViewById(R.id.project_item_image);
			holder = new ViewHolder(nameView, descriptionView, imageView);
			view.setTag(holder);
		}
		
		nameView.setText(project.getName());
		descriptionView.setText(project.getDescription());
		
		//TODO Wymyślić skąd brać obrazek do pokazania na liście
		if(position == 1){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.background));
		}
		
		if(imageView.getDrawable() == null){
			RelativeLayout v = (RelativeLayout)view.findViewById(R.id.project_item_text_views);
			v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		if(project.equals(animatedProject)){
			showAddAnimation(view);
			animatedProject = null;
		}
		
		return view;
	}
	
	public void setAnimatedProject(Project project){
		animatedProject = project;
	}
	
	public void delete(Project project){
		int position = getPosition(project);
		View view = listView.getChildAt(position);
		showDeleteAnimation(view,project);
	}
	
	private void showAddAnimation(View view){
		view.startAnimation(getAnimation(android.R.anim.slide_in_left));
	}
	
	private void showDeleteAnimation(View view, final Project project){
		Animation animation = getAnimation(android.R.anim.slide_out_right);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				remove(project);
				notifyDataSetChanged();
			}
		});
		view.startAnimation(animation);
	}
	
	private Animation getAnimation(int animationId){
		Animation animation  = AnimationUtils.loadAnimation(context, animationId);
		animation.setDuration(500);
		return animation;
	}
	
	private class ViewHolder{
		private TextView name;
		private TextView description;
		private ImageView image;
		
		public ViewHolder(TextView name, TextView description, ImageView image){
			this.name = name;
			this.description = description;
			this.image = image;
		}
		
		public TextView getName() {
			return name;
		}

		public TextView getDescription() {
			return description;
		}

		public ImageView getImage() {
			return image;
		}
	}
}
