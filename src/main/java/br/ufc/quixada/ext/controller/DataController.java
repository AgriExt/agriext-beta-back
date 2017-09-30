
package br.ufc.quixada.ext.controller;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
//import br.ufc.quixada.ext.service.DataService;
import br.ufc.quixada.ext.util.WekaUtil;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class DataController {

	/*
	 * COLOQUE O CAMINHO DO SEU PROJETO
	 */
	String myPath = "/home/antoniorrm/";

	// @Autowired
	// private DataService dataservice;
	//
	// @GetMapping("/all")
	// public ResponseEntity<List<Data>> getAll() {
	// return dataservice.get();
	// }
	//
	// @GetMapping("/{id}")
	// public ResponseEntity<Data> getById(@PathVariable("id") String id) {
	// return dataservice.get(id);
	// }
	//
	// @PutMapping
	// public ResponseEntity<String> insert(@RequestBody Data data) {
	// return dataservice.save(data);
	//
	// }
	//
	// @PostMapping
	// public ResponseEntity<String> update(@RequestBody Data data) {
	// return dataservice.update(data);
	// }
	//
	// @DeleteMapping("/{id}")
	// public void delete(@PathVariable("id") String id) {
	// this.dataservice.delete(id);
	// }

	@PostMapping("/uploadModel")
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file,
			@RequestParam("model") MultipartFile model, @RequestParam("kc") double kc) {
		String fileName = "";
		String modelName = "";
		File file_exists = null;
		File model_existis = null;

		try {
			// Pega os nomes dos arquivos
			fileName = file.getOriginalFilename();
			modelName = model.getOriginalFilename();
			// Define o caminho que os arquivos vao ser salvos
			String pathFile = myPath + fileName;
			String pathModel = myPath + modelName;
			// Pega os bytes dos arquivos
			byte[] csv = file.getBytes();
			byte[] bModel = model.getBytes();
			// Cria os Files
			file_exists = new File(pathFile);
			model_existis = new File(pathModel);

			// Verifica se o csv ja existem
			if (!file_exists.exists()) {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file_exists));
				stream.write(csv);
				stream.close();
			}
			// Salva o model enviado
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(model_existis));
			stream.write(bModel);
			stream.close();

			// Retorna a predição do csv e do model enviado
			return WekaUtil.modelEt0(kc, fileName, modelName);
		} catch (

		Exception e) {

			return e.getMessage();
		}
	}

	@PostMapping("/uploadFile")
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file,
			@RequestParam("kc") double kc) {
		
		String fileName = "";
		
		File file_exists = null;
		
		try {
			//Pega os nome dos arquivo
			fileName = file.getOriginalFilename();
			//Define o caminho que o arquivo vai ser salvo
			String path = myPath + fileName;
			//Pega o byte do arquivo
			byte[] bytes = file.getBytes();
			
			//Cria o File
			file_exists = new File(path);
			
			//Verifica se o csv ja existem
			if (!file_exists.exists()) {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file_exists));
				stream.write(bytes);
				stream.close();
			}
			
			//Returno a predição do Modelo QuixadaHC
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
			//Pega os nome dos arquivo
			fileName = file.getOriginalFilename();
			//Define o caminho que o arquivo vai ser salvo
			String path = myPath + fileName;
			//Pega o byte do arquivo
			byte[] bytes = file.getBytes();
			//Cria o File
			file_exists = new File(path);

			//Verifica se o csv ja existem
			if (!file_exists.exists()) {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file_exists));
				stream.write(bytes);
				stream.close();
			}
			
			//Faz a classificação do modelo a partir do csv enviado e salvo o modelo no path definido
			return WekaUtil.classifier(type, fileName);
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/download/{nome}")
	public ResponseEntity<byte[]> downloadFileHandler(@PathVariable("nome") String nome) {

		//Local onde o model gerado no classificador foi salvo
		String path = myPath + nome + ".model";
		File file = null;
		file = new File(path);
		//Verifica se o model existe
		if (file.isFile()) {
			//Cria o header Http
			HttpHeaders headers = new HttpHeaders();
			//Pega o nome do arquivo
			String filename = file.getName();
			//Cria o array de bytes
			byte[] contents = null;
			try {
				//Pega os bytes do arquivo e joga no array
				contents = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
				return response;
			}
			//forma o header 
			headers.setContentDispositionFormData(filename, filename);
			//Cria o response
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
			//Retorna a response criada
			return response;
		}
		return null;

	}
}
