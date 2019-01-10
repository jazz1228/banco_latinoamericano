package com.udea.web.banco.banco.RepositoryMongo;


import com.udea.web.banco.banco.ModelMONGO.Pin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface PinRepository extends MongoRepository<Pin, Integer> {

    @Query("{'number':?0}")
    Pin findByNumber(String number);




}
