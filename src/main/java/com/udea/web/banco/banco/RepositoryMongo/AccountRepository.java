package com.udea.web.banco.banco.RepositoryMongo;

import com.udea.web.banco.banco.ModelMONGO.Account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

public interface AccountRepository extends MongoRepository<Account, String> {

    @Query("{'number':?0}")
    Account findByUid(String id);

}
