package com.udea.web.banco.banco.Controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.udea.web.banco.banco.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/v1/bank")
public class BankController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CountryRepository countryRepository;
    @Autowired
    PinRepository pinRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;


    /*@PostMapping("/search")
    public Search getAllHomes(@RequestBody String search) throws JsonProcessingException, ParseException {




    }*/


}
