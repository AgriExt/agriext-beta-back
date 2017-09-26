
package br.ufc.quixada.ext.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.quixada.ext.model.Data;
import br.ufc.quixada.ext.service.DataService;
import br.ufc.quixada.ext.util.WekaUtil;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class DataController {

	@Autowired
	private DataService dataservice;

	@GetMapping("/all")
	public ResponseEntity<List<Data>> getAll() {
		return dataservice.get();
	}	

	@GetMapping("/{id}")
	public ResponseEntity<Data> getById(@PathVariable("id") String id) {
		return dataservice.get(id);
	}

	@PutMapping
	public ResponseEntity<String> insert(@RequestBody Data data) {
		return dataservice.save(data);

	}

	@PostMapping
	public ResponseEntity<String> update(@RequestBody Data data) {
		return dataservice.update(data);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id) {
		this.dataservice.delete(id);
	}

	@GetMapping("/getEt0JJ")
	public String getEt0JJ(@PathVariable("id") String id) {
		String et0 = "";
		try {
//			 et0 = WekaUtil.jensenHaysen(rad_solar_total, temp_ar_media);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			et0 = e.getMessage();
		}

		return et0;
	}

	@PostMapping("/uploadModel")
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("model") MultipartFile model, @RequestParam("kc")  double kc) {
		String fileName = "";
		String modelName = "";
		File file_exists = null;
		File model_existis= null;
		
		try {
			fileName = file.getOriginalFilename();
			modelName = model.getOriginalFilename();
			String pathFile = "/home/antoniorrm/" + fileName;
			String pathModel = "/home/antoniorrm/" + modelName;
			System.out.println(fileName + " " + kc);
			byte[] bytes = file.getBytes();
			file_exists = new File(pathFile);
			model_existis= new File(pathFile);
			if (!file_exists.exists()) {
				System.out.println("teste");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(pathFile)));
				stream.write(bytes);
				stream.close();
			}
			if (!model_existis.exists()) {
				System.out.println("teste");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(pathModel)));
				stream.write(bytes);
				stream.close();
			}
			return WekaUtil.modelEt0(kc, fileName, modelName);
		} catch (Exception e) {

			return e.getMessage();
		}
	}
	
	@PostMapping("/uploadFile")
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("kc")  double kc) {
		String fileName = "";
		File file_exists = null;
		try {
			fileName = file.getOriginalFilename();
			String path = "/home/antoniorrm/" + fileName;
			System.out.println(fileName + " " + kc);
			byte[] bytes = file.getBytes();
			file_exists = new File(path);
			if (!file_exists.exists()) {
				System.out.println("teste");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
				stream.write(bytes);
				stream.close();
			}
			return WekaUtil.quixadaHC(kc, fileName);
		} catch (Exception e) {

			return e.getMessage();
		}
	}

	// Upload single file using Spring Controller
	@PostMapping("/uploadFile/{id}")
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file,
			@PathVariable("id") String id) {
		String fileName = "";
		int type = Integer.parseInt(id);
		File file_exists = null;
		try {
			fileName = file.getOriginalFilename();
			String path = "/home/antoniorrm/" + fileName;
			System.out.println(fileName + " " + id);
			byte[] bytes = file.getBytes();
			file_exists = new File(path);
			if (!file_exists.exists()) {
				System.out.println("teste");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
				stream.write(bytes);
				stream.close();
			}
			return WekaUtil.classifie(type, fileName);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/download/{nome}")
	public ResponseEntity<byte[]> downloadFileHandler(@PathVariable("nome") String nome) {

		// retrieve contents of "C:/tmp/report.pdf" that were written in showHelp
		String path = "/home/antoniorrm/" + nome + ".model";
		File file = null;
		file = new File(path);
		System.out.println(file.getName());
		if (file.isFile()) {
			HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.parseMediaType("application/pdf"));
			String filename = file.getName();

			byte[] contents = null;
			try {
				contents = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			headers.setContentDispositionFormData(filename, filename);
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
			return response;
		}
		return null;

	}
}
