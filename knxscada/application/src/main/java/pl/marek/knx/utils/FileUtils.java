package pl.marek.knx.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	
	public static boolean isExternalStorageWriteable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isExternalStorageReadable(){
		if(isExternalStorageWriteable()){
			return true;
		}
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}
	
	public static File getExternalStorageDirectory(){
		return Environment.getExternalStorageDirectory();
	}
		
	public static void copy(AssetManager assets, String sourcePath, String destinationPath) throws IOException{			
		String contents[] = assets.list(sourcePath);
		File destination = new File(destinationPath,sourcePath);
		if(contents == null || contents.length == 0){
			InputStream src = assets.open(sourcePath);
			if(!destination.getParentFile().exists())
				destination.getParentFile().mkdirs();
			copy(src, destination);
		} else{
			if(!destination.exists()){
				destination.mkdirs();
			}
			for (String entry : contents) {
				copy(assets, sourcePath + java.io.File.separator + entry, destinationPath);
			}
		}
	}
		
	public static void copy(InputStream src, File dst) throws IOException{
		OutputStream out = new FileOutputStream(dst);       
		byte[] buffer = new byte[1024];
        int read;
        if(src != null && out != null){
	        while ((read = src.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        src.close();
        	out.close();
        }
	}
	
	public static void delete(String path){
		Log.d("DELETE FILE", path);
		File file = new File(path);
		if(file.isDirectory()){
			String[] childs = file.list();
			if(childs != null){
				for(String child : childs){
					delete(new File(path,child).getAbsolutePath());
				}
			}
		}
		file.delete();
	}
}