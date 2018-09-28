package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
