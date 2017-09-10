
package br.ufc.quixada.ext.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import br.ufc.quixada.ext.TestWeka;
import br.ufc.quixada.ext.model.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			 return TestWeka.classifie();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	private final Logger log = LoggerFactory.getLogger(getClass());
	@PostMapping("/uploadFile")
//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody void uploadFileHandler(@RequestParam("file") MultipartFile file) {
		String name = "";

		try {
			name = file.getOriginalFilename();
			System.out.println(name);
			byte[] bytes = file.getBytes();
			// Creating the directory to store file
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("/home/antoniorrm/"+name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			log.info("afasdfadf" + name);
		}
	}

}
