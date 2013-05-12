package pl.marek.knx.utils;

import java.io.File;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import android.os.Environment;

public class FileUploadUtil {
	
	private FileUpload upload;
	private File uploadedFile;
	
	public FileUploadUtil(FileUpload upload){
		this.upload = upload;
	}
	
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
	
	public boolean uploadFile(){
		
		if (upload == null){
            return false;
        }
        else{
        	if(isExternalStorageWriteable()){
        		String fileName = upload.getClientFileName();

                try {
                	File externalStorage = getExternalStorageDirectory();
                	File parent = new File(externalStorage, getApplicationUploadDirectory());
                	if(!parent.exists()){
                		parent.mkdirs();
                	}
                	uploadedFile = new File(parent, fileName);
    				upload.writeTo(uploadedFile);
    			} catch (Exception e) {
    				return false;
    			}
        	}else{
        		return false;
        	}
        }
		return true;
	}
	
	private String getApplicationUploadDirectory(){
		return "knxscada";
	}
	
	public String getUploadedFilePath(){
		String path = "";
		if(uploadedFile != null){
			if(uploadedFile.exists()){
				path = uploadedFile.getAbsolutePath();
			}
		}
		return path;
	}

}
