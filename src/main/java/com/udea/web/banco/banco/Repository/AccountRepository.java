package com.udea.web.banco.banco.Repository;

import com.udea.web.banco.banco.Model.Account;
import com.udea.web.banco.banco.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select u from Account as u where u.number=:id")
    Account findByUid(@Param("id") String id);
}
