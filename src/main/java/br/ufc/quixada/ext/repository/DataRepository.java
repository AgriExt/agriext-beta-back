package br.ufc.quixada.ext.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.ufc.quixada.ext.model.Data;

@Repository
public interface DataRepository extends MongoRepository<Data, String>{
	Data findById(String id);
}
