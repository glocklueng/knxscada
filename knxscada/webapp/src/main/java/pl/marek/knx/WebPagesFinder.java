package pl.marek.knx;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;

import pl.marek.knx.annotations.HtmlFile;

public class WebPagesFinder implements IResourceFinder{
	
	public static final String DEFAULT_PAGES_PATH = "/pages/";
	
	private ServletContext servletContext;
	private String pagesPath;
	
	public WebPagesFinder(ServletContext servletContext){
		this.servletContext = servletContext;
		this.pagesPath = DEFAULT_PAGES_PATH;
	}

	@Override
	public IResourceStream find(Class<?> clazz, String path) {
		
		String extension = path.substring(path.lastIndexOf('.') + 1);
		for(String fileName: createFileNames(clazz)){
			String location = String.format("%s%s.%s", pagesPath,fileName,extension);
			URL url;
			try{
				url = servletContext.getResource(location);
				if (url != null){
					return new UrlResourceStream(url);
				}
			}
			catch (MalformedURLException e){
				throw new WicketRuntimeException(e);
			}
		}
		return null;
	}
	
	private ArrayList<String> createFileNames(Class<?> clazz){
		
		ArrayList<String> files = new ArrayList<String>();
		String name = Strings.lastPathComponent(clazz.getName(), '.');
		String annotatedName = getAnnotatedFileName(clazz);
		if(annotatedName != null){
			files.add(annotatedName);
		}
		files.add(name);
		files.add(name.toLowerCase());
		
		String firstLetter = String.valueOf(name.charAt(0)).toLowerCase();
		String otherLetters = name.substring(1);

		files.add(firstLetter.concat(otherLetters));
		files.add(name.replace("Page", "").toLowerCase());
		files.add(name.replace("Panel", "").toLowerCase());
		
		return files;
	}
	
	private String getAnnotatedFileName(Class<?> clazz){
		HtmlFile htmlFile = clazz.getAnnotation(HtmlFile.class);
		String name = null;
		if(htmlFile != null){
			name = htmlFile.value();
			if(name != null){
				name = name.replace(".html", "");
			}
		}
		return name;
	}
}