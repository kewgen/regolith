package com.geargames.regolith;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.*;

public class XmlTransmitter {
	
	private static XmlTransmitter instance = new XmlTransmitter();
	
	private Map<Package, JAXBContext> contexts = new HashMap<Package, JAXBContext>();

	private XmlTransmitter(){}
	
	private <T> JAXBContext getContext(Class<T> tclass) throws Exception {
		JAXBContext context;
		Package pack = tclass.getPackage();
		if(contexts.containsKey(pack)){
			context = contexts.get(pack);
		}else{
			synchronized (this){
                if(!contexts.containsKey(pack)){
                    context = JAXBContext.newInstance(pack.getName());
			        contexts.put(pack, context);
                }else{
                    context = contexts.get(pack);
                }
            }
		}
		return context;
	} 



	public <T> T unmarshal(Class<T> tclass,  File xmlFile) throws Exception {
		Unmarshaller unmarshaller = getContext(tclass).createUnmarshaller();
		return (T)unmarshaller.unmarshal(xmlFile);
	}
	
	public <T> void marshal(T instance, File xmlFile) throws Exception {
		Marshaller marshaller = getContext(instance.getClass()).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(instance, xmlFile);
	}
	
	public <T> T unmarshal(Class<T> tclass, InputStream input) throws Exception {
		Unmarshaller unmarshaller = getContext(tclass).createUnmarshaller();
		return (T)unmarshaller.unmarshal(input);
	}
	
	public <T> void marshal(T instance, OutputStream output) throws Exception {
		Marshaller marshaller = getContext(instance.getClass()).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(instance, output);
	}

	public static XmlTransmitter getTransmitter(){
		return instance;
	}
}
