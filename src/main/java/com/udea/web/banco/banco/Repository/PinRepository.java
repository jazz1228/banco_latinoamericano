package com.udea.web.banco.banco.Repository;


import com.udea.web.banco.banco.Model.Pin;
import com.udea.web.banco.banco.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PinRepository extends JpaRepository<Pin, Long> {
    @Query("select p from Pin as p where p.number=:number")
    Pin findByNumber(@Param("number") String number);
}
