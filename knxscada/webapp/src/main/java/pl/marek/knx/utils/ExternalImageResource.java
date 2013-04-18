package pl.marek.knx.utils;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;

public class ExternalImageResource implements IResource{

	private static final long serialVersionUID = 1L;
	private String imagePath;
    
    public ExternalImageResource(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public void respond(Attributes attributes) {
        File file = new File(imagePath);
        FileResourceStream fileResourceStream = new FileResourceStream(file);
        ResourceStreamResource resource = new ResourceStreamResource(fileResourceStream);
        resource.respond(attributes);
    }
}