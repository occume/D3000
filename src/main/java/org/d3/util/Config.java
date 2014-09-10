package org.d3.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

public class Config {

	private static final String CONFIG_FILENAME = "global_config.xml";
	
	private static final String PATH = CONFIG_FILENAME;
	
	private static final String ROOT_NODE = "/globe/";
	
	private static File file = null;
	
	private static InputStream ins = null;
	
	private static Resource res = null;
	
	private volatile long lastModifiedTime = 0;
	
	private Multimap<String, String> configMap;
	
	private static Config instance = new Config();
	
	private static void getConfigFile(){
//		URL url = Config.class.getClassLoader().getResource("");
//		System.out.println("url = " + url);
//		if(url == null){
//			url = Config.class.getProtectionDomain().getCodeSource().getLocation();
//			url = Config.class.getResource("/" + CONFIG_FILENAME);
//			file = new File(url.getFile());
//			System.out.println(file.lastModified());
			res = new ClassPathResource("/" + CONFIG_FILENAME);
			try {
				System.out.println(res.lastModified());
//				file = r.getFile();
				ins = res.getInputStream();
//				r.c
//				System.out.println(file.lastModified());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return;
//		}
//		File file = new File(url.getPath());
//		if(file.exists()){
////			File p = file.getParentFile();
//			getPath(file);
//		}
	}
	
	private static void getPath(File config_dir){
		String[] files = config_dir.list();
		String thePath = config_dir.getPath();
		File temp = null;
		for(String name: files){
			temp = new File(thePath + File.separator + name);
			if(temp.isDirectory()){
				getPath(temp);
			}
			else{
				if(CONFIG_FILENAME.equals(name)){
					file = temp;
				}
			}
		}
	}
	
	public static void main(String...strings){
		getConfigFile();
	}
	
	private Config(){
		
		getConfigFile();
		try {
			lastModifiedTime = res.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			doc = saxReader.read(ins);
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
		
		long newTime = -1;
		try {
			newTime = res.lastModified();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
