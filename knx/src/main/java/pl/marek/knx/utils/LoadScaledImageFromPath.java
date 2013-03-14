package pl.marek.knx.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadScaledImageFromPath extends AsyncTask<String, Void, Bitmap>{
	
	private ImageView imageView;
	private ProgressBar progressBar;
	
	public LoadScaledImageFromPath(ImageView imageView, ProgressBar progressBar){
		this.imageView = imageView;
		this.progressBar = progressBar;
	}
	
	@Override
	protected void onPreExecute() {
		imageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		
		String path = params[0];
		
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap ,150, 150, true);

		return scaledBitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		progressBar.setVisibility(View.GONE);
		if(result != null && imageView != null){
			imageView.setImageBitmap(result);
			imageView.setVisibility(View.VISIBLE);
		}
	}
}