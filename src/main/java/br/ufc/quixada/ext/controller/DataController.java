
package br.ufc.quixada.ext.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;

import br.ufc.quixada.ext.WekaUtil;
import br.ufc.quixada.ext.model.Data;

import org.apache.camel.Consume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.quixada.ext.repository.DataRepository;

@RestController
@RequestMapping("/data")
public class DataController {

	private DataRepository dataRepository;

	public DataController(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/tmodelo")
	public String getTModelo() {
		try {
            return WekaUtil.classifie(0, "/home/antoniorrm/2016total-semoutlier2.csv");
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/all")
	public List<Data> getAll() {
		List<Data> dados = this.dataRepository.findAll();
		return dados;
	}

	@GetMapping("/{id}")
	public Data getById(@PathVariable("id") String id) {
		return this.dataRepository.findOne(id);
	}

	@PutMapping
	public Data insert(@RequestBody Data data) {
		return this.dataRepository.insert(data);
	}

	@PostMapping
	public Data update(@RequestBody Data data) {
		return this.dataRepository.save(data);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id) {
		this.dataRepository.delete(id);
	}

	// Upload single file using Spring Controller
//	private final Logger log = LoggerFactory.getLogger(getClass());
	@PostMapping("/uploadFile/{id}")
//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
		String fileName= "";
		int type = Integer.parseInt(id);
		try {
			fileName = file.getOriginalFilename();
            String path = "/home/antoniorrm/" + fileName;
			byte[] bytes = file.getBytes();
			// Creating the directory to store file
			BufferedOutputStream stream =
					new BufferedOutputStream(
					        new FileOutputStream(
					                new File(path)));
			stream.write(bytes);
			stream.close();

			return WekaUtil.classifie(type, path);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	
	@GetMapping("/download")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamReader> download(String param){

	       	File file2Upload = new File("/home/antoniorrm/upquixe.csv");
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        headers.add("Pragma", "no-cache");
	        headers.add("Expires", "0");
	        
	    InputStreamReader resource = null;
		try {
			resource = new InputStreamReader(new FileInputStream(file2Upload));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ResponseEntity.ok()
	    		.headers(headers)
	            .contentLength(file2Upload.length())
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(resource);
	}
}
