package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Account;
import com.udea.web.banco.banco.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u where u.email=:email and u.pass=:contrasena")
    User findUser(@Param("email") String email, @Param("contrasena") String contrasena);

    @Query("select u from User as u where u.id=:id")
    User findByUid(@Param("id") String id);

    @Query("select u from User as u where u.numberAccount=:id")
    User findByNumberAccount(@Param("id")Account numberAccount);
}
