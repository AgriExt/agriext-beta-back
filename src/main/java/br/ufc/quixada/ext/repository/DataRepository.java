package br.ufc.quixada.ext.repository;
import br.ufc.quixada.ext.model.Data;
import com.mongodb.util.JSON;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<Data, String>{
	
}
