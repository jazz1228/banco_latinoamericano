package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
