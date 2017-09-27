//package br.ufc.quixada.ext.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import br.ufc.quixada.ext.model.Data;
//import br.ufc.quixada.ext.repository.DataRepository;
//
//
//@Service
//public class DataService {
//
//	@Autowired
//	DataRepository repository;
//
//	public ResponseEntity<String> save(Data data){
//		if(null==repository.findById(data.getId())) {
//			repository.insert(data);
//			return new ResponseEntity<String>(HttpStatus.OK);		
//		}else{
//			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);		
//		}
//	}
//
//	public ResponseEntity<String> delete(String id){
//		repository.delete(id);
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
//
//	public ResponseEntity<String> update(Data data){
//		if(null==repository.findById(data.getId())) {
//			repository.save(data);
//			return new ResponseEntity<String>( HttpStatus.OK);		
//		}else{
//			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);		
//		}
//	}
//
//	public ResponseEntity<Data> get(String id){
//		return new ResponseEntity<Data>(this.repository.findOne(id), HttpStatus.OK);
//	}
//
//	public ResponseEntity<List<Data>> get(){
//		return new ResponseEntity<List<Data>>(this.repository.findAll(), HttpStatus.OK);
//	}
//	
//}
