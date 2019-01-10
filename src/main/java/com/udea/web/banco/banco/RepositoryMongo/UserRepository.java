package com.udea.web.banco.banco.RepositoryMongo;


import com.udea.web.banco.banco.ModelMONGO.Account;
import com.udea.web.banco.banco.ModelMONGO.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'$and':[{'email':?0}, {pass:?1}]}")
    User findUser(String email, String contrasena);

    @Query("{'email':?0}")
    User findUserByEmail(String email);

    @Query("{'id':?0}")
    User findByUid( String id);

    @Query("{'numberAccount':?0}")
    User findByNumberAccount( Account numberAccount);
}
