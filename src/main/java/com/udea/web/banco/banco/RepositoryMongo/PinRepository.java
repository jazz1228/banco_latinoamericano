package com.udea.web.banco.banco.RepositoryMongo;


import com.udea.web.banco.banco.ModelMONGO.Pin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PinRepository extends MongoRepository<Pin, Object> {


    Pin findPinByNumber(String number);




}
