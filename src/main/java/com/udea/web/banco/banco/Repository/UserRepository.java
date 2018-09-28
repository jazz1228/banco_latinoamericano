package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
