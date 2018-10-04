package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Account;
import com.udea.web.banco.banco.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction as t where t.account=:account or t.finalAccount=:finalAccount")
    List<Transaction> findAllByAccount(@Param("account")String account,@Param("finalAccount") Account finalAccount);
}
