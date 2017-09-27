package br.ufc.quixada.ext.model;


import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;


//@Document(collection = "data")
public class Data {
	
	@Id 
	private String id;
	
	private String data;
	
	public String getId() {
		return id;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String dados) {
		this.data = dados;
		
	} 
}
