package com.udea.web.banco.banco.RepositoryMongo;


import com.udea.web.banco.banco.ModelMONGO.Account;
import com.udea.web.banco.banco.ModelMONGO.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, Long> {

    @Query("{'$or':[{'account':?0},{'finalAccount':?1}]}")
    List<Transaction> findAllByAccount( String account,  Account finalAccount);
}
