package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {
}
