package com.udea.web.banco.banco;

import com.udea.web.banco.banco.Controller.BankController;
import com.udea.web.banco.banco.ModelMONGO.User;
import com.udea.web.banco.banco.RepositoryMongo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebMvcTest(BankController.class)
public class BancoApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MockMvc mvc;

	@Test
	public void contextLoads() {

		given(userRepository.findAll());
	}

}
