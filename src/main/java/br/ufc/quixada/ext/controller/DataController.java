
package br.ufc.quixada.ext.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufc.quixada.ext.model.Data;
import br.ufc.quixada.ext.repository.DataRepository;

@RestController
@RequestMapping("/data")
public class DataController {

	private DataRepository dataRepository;

	public DataController(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		// TODO Auto-generated constructor stub
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
}
