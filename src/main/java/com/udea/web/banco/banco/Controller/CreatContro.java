package com.udea.web.banco.banco.Controller;


import com.udea.web.banco.banco.ModelMONGO.Account;
import com.udea.web.banco.banco.ModelMONGO.Country;
import com.udea.web.banco.banco.ModelMONGO.User;
import com.udea.web.banco.banco.RepositoryMongo.AccountRepository;
import com.udea.web.banco.banco.RepositoryMongo.CountryRepository;
import com.udea.web.banco.banco.RepositoryMongo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/prueba")
public class CreatContro {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/prueba1")
    public List<User> prueba1(){
       return userRepository.findAll();
    }
}
