package kuznietsov.gpcparser.service;

import kuznietsov.gpcparser.model.GpcModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GpcRepository extends MongoRepository<GpcModel, String> {

}
