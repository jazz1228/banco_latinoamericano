package com.udea.web.banco.banco.RepositoryMongo;

import com.udea.web.banco.banco.ModelMONGO.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Account findAccountByNumber(String number);

}
