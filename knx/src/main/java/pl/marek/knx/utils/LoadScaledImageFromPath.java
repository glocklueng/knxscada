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
	
	private int sizeX;
	private int sizeY;
	
	public LoadScaledImageFromPath(ImageView imageView, ProgressBar progressBar){
		this(imageView, progressBar, 100, 100);
	}
	
	public LoadScaledImageFromPath(ImageView imageView, ProgressBar progressBar, int sizeX, int sizeY){
		this.imageView = imageView;
		this.progressBar = progressBar;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
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
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap ,sizeX, sizeY, true);

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