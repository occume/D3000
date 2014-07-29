package org.d3.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.D3Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectConvert {
	@Autowired
	private ObjectMapper jackson;
	
	private static ObjectConvert me;
	
	public static ObjectConvert Me(){
		if(me == null){
			me = (ObjectConvert) D3Context.getBean("objectConvert");
		}
		return me;
	}
	
	public String ojb2json(Object t){
		try {
			return jackson.writeValueAsString(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}