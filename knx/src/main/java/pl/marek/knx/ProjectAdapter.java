package pl.marek.knx;

import java.util.List;

import pl.marek.knx.database.Project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ProjectAdapter extends ArrayAdapter<Project>{
	
	private LayoutInflater inflater;
	private Context context;
	
	public ProjectAdapter(Context context, List<Project> projects) {
		super(context, android.R.layout.simple_list_item_single_choice, projects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Project project = getItem(position);
		
		View view = inflater.inflate(R.layout.project_item, parent, false);
		TextView nameView = (TextView)view.findViewById(R.id.project_item_name);
		TextView descriptionView = (TextView)view.findViewById(R.id.project_item_description);
		ImageView imageView = (ImageView)view.findViewById(R.id.project_item_image);
		
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
		
		Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
	    view.startAnimation(animation);
		
		return view;
	}
}
