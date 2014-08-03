package org.d3.util;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

public class Config {

	private static final String CONFIG_FILENAME = "global_config.xml";
	
	private static final String PATH = CONFIG_FILENAME;
	
	private static final String ROOT_NODE = "/globe/";
	
	private File file = null;
	
	private volatile long lastModifiedTime = 0;
	
	private Multimap<String, String> configMap;
	
	private static Config instance = null;
	
	private static File getConfigFile(){
		URL url = Config.class.getClassLoader().getResource("");
		File file = new File(url.getPath());
		if(file.exists()){
			File p = file.getParentFile();
			System.out.println(getPath(p));
			return null;
		}
		return null;
	}
	
	private static File getPath(File file){
		String[] files = file.list();
		String thePath = file.getPath();
		File temp = null;
		for(String name: files){
			temp = new File(thePath + File.separator + name);
			if(temp.isDirectory()){
				getPath(temp);
			}
			else{
				if(CONFIG_FILENAME.equals(name)){
					System.out.println("---" + temp);
					return temp;
				}
			}
		}
		return null;
	}
	
	public static void main(String...strings){
		File file = getConfigFile();
		System.out.println(file);
	}
	
	private Config(){
		
		file = getConfigFile();
		lastModifiedTime = file.lastModified();
		
		if(lastModifiedTime == 0){
			System.err.println(PATH + " file does not exist!");
		}
		
		configMap = LinkedListMultimap.create();
		
		initConfigMap();
	}
	
	private void initConfigMap(){
		
		configMap.clear();
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		
		try {
			doc = saxReader.read(file);
		} catch (DocumentException e) {
			System.err.println(PATH + " file does not exist!");
		}
		
		Element root = doc.getRootElement();
		parseElement(root);
	}
	
	@SuppressWarnings("unchecked")
	private void parseElement(Element root){
		List<Element> elements = root.elements();
		
		for(Element e: elements){
			if(e.hasMixedContent()){
				parseElement(e);
			}
			configMap.put(e.getPath(), e.getTextTrim());
		}
	}
	
	public static Config getInstance(){
		return instance;
	}
	
	final public Collection<String> getItem(String name){
		
		long newTime = file.lastModified();
		if(newTime > lastModifiedTime){
			synchronized (this) {
				if(newTime > lastModifiedTime){
					lastModifiedTime = newTime;
					initConfigMap();
				}
			}
		}
		return configMap.get(ROOT_NODE + name);
	}
	
}
