package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
