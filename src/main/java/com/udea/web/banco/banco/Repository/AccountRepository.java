package com.udea.web.banco.banco.Repository;

import com.udea.web.banco.banco.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
