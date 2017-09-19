
package br.ufc.quixada.ext.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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
import br.ufc.quixada.ext.repository.DataRepository;
import br.ufc.quixada.ext.util.WekaUtil;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class DataController {

	private DataRepository dataRepository;

	public DataController(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
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
	// private final Logger log = LoggerFactory.getLogger(getClass());
	@PostMapping("/uploadFile/{id}")
//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file,
			@PathVariable("id") String id) {
		String fileName = "";
		int type = Integer.parseInt(id);
		try {
			fileName = file.getOriginalFilename();
			String path = "/home/antoniorrm/" + fileName;
			System.out.println(fileName +" "+ id);
			byte[] bytes = file.getBytes();
			// Creating the directory to store file
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
			stream.write(bytes);
			stream.close();

			return WekaUtil.classifie(type, fileName);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/download/{nome}")
	public ResponseEntity<byte[]> downloadFileHandler(@PathVariable("nome") String nome) {
	    // convert JSON to Employee 
	  //  Employee emp = convertSomehow(json);

	    // retrieve contents of "C:/tmp/report.pdf" that were written in showHelp
		String path = "/home/antoniorrm/"+nome+".model";
		File file = null;
		file = new File(path);
		if (file.isFile()) {
			HttpHeaders headers = new HttpHeaders();
		    //headers.setContentType(MediaType.parseMediaType("application/pdf"));
		    String filename = file.getName();
		    
		    byte[] contents = null;
			try {
				contents = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    headers.setContentDispositionFormData(filename, filename);
//		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		    return response;
		}
		return null;

	}
}
