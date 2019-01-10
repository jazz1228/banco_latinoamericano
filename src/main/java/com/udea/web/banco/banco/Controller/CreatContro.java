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
    public String prueba1(){
        User user=new User();
        Country country=new Country();
        Account account=new Account();

        account.setNumber("1234341231");
        account.setBalance(12312312.21);
        account.setPass("1234");
        account.setType("ahorro");
        account.setId(ObjectId.get());
        accountRepository.save(account);

        country.setName("Colombia");
        country.setCoin("COP");
        country.setId(ObjectId.get());
        countryRepository.save(country);

        user.setAddress("xxx-xxx");
        user.setBirthDay("12-28-1995");
        user.setCountry(country);
        user.setEmail("Juan@hotmail.com");
        user.setId("123456789");
        user.setName("Juan");
        user.setPass("123456");
        user.setPhone("320182321");
        user.setRole("Contador");
        user.setNumberAccount(account);
        user.setIden(ObjectId.get());
        userRepository.save(user);


        return "Funciono";
    }
}
