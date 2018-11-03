package com.udea.web.banco.banco.Controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.udea.web.banco.banco.Model.*;
import com.udea.web.banco.banco.Object.MessageObject;
import com.udea.web.banco.banco.Object.PinObject;
import com.udea.web.banco.banco.Object.TokenObject;
import com.udea.web.banco.banco.Repository.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.udea.web.banco.banco.Object.Cuenta;

import java.util.List;
import org.springframework.session.MapSession;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;
import java.util.Random;

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

    //Manejar session desde el backEnd
    MapSession session;


    @PostMapping("/logini")
    public MessageObject logini(@RequestBody String credentials){

        User user=null;
        try {
            JSONObject obj=new JSONObject(credentials);
            String correo=obj.getString("correo");
            String contrasena = obj.getString("contrasena");
            //Decodificar
            user=userRepository.findUser(correo,contrasena);
            if (user!=null){
                String pin =generatePin("ingreso");
                //Obtengo la fecha del sistema
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String startDate=dateFormat.format(date);
                while(pinRepository.findByNumber(pin)!=null){
                    //Genero pin y envio al celular del user
                    pin =generatePin("ingreso");
                }
                    //Creo y guardo el pin en la base de datos
                    sendPin(pin, user.getPhone());
                    Pin pin1=new Pin();
                    pin1.setIdUser(user);
                    pin1.setNumber(pin);
                    pin1.setStartDate(startDate);
                    pin1.setEndDate(".");
                    pinRepository.save(pin1);
                    MessageObject mensaje=new MessageObject("enviado");
                    return mensaje;

            }

            else{
                MessageObject mensaje=new MessageObject(session.getId());
                return mensaje;
            }

        }catch (Exception e){

            MessageObject mensaje=new MessageObject(session.getId());
            return mensaje;
        }
    }


    @PostMapping("/loginf")
    public TokenObject loginF(@RequestBody String credentials){
        TokenObject tokenObject;
        try{
            User user;
            Duration s=Duration.ofSeconds(2);
            JSONObject obj=new JSONObject(credentials);
            String correo=obj.getString("correo");
            String pin = obj.getString("pin");
            user=userRepository.findUserByEmail(correo);
            String aux=pinRepository.findByNumber(pin).getEndDate();
            if(pinRepository.findByNumber(pin)!=null && aux.equals(".")) {
                tokenObject = new TokenObject(generateToken(),user.getRole());
                session = new MapSession(tokenObject.getToken());
                session.setMaxInactiveInterval(s);
                return tokenObject;
            }else
                return tokenObject =new TokenObject("error","");
        }
        catch (Exception e){
            return tokenObject =new TokenObject("error","");
        }

    }


    @GetMapping("/consignacion")
    public String consignacion (@RequestBody String consignacion,@RequestHeader String token) throws JsonProcessingException, ParseException, JSONException {

        Account account;
        Double money;
        User usuario;
        String monedaDestino;

        try {
            JSONObject obj=new JSONObject(consignacion);

            //Conversion a Json a java
            String cedula=obj.getString("cedula");
            String cuentaDestino = obj.getString("cuentaDestino");
            Double monto = obj.getDouble("monto");
            String tipoTransaccion = obj.getString("tipoTransaccion");
            String moneda = obj.getString("moneda");

            account=accountRepository.findByUid(cuentaDestino);

            //Conversion de moneda y cambio en cuenta
            usuario= userRepository.findByNumberAccount(account);
            monedaDestino=usuario.getCountry().getCoin();
            money = this.convertMoney(monto,moneda,monedaDestino);

            account.setBalance(account.getBalance()+money);
            accountRepository.save(account);

            //Guardar transaccion
            Transaction transaction = new Transaction();
            transaction.setAccount(cedula);
            transaction.setFinalAccount(account);

            //Guardado de fecha
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            transaction.setDate(dateFormat.format(fecha));

            transaction.setType("CONSIGNACION");
            transaction.setAmount(money);
            transaction.setCoin(monedaDestino);
            transactionRepository.save(transaction);
            return "Exitoso";
        }catch (Exception e){
            return "Fallo";
        }

    }


    @PostMapping("/retiro")
    public String retiro (@RequestBody String retiro,@RequestHeader String token) throws JsonProcessingException, ParseException, JSONException {

        String pinRetiro;
        Account cuenta;
        Double money;
        User usuario;
        String monedaUsuario;
        Double saldo;

        try {
            JSONObject obj=new JSONObject(retiro);

            //Json a java
            Double monto = obj.getDouble("monto");
            String monedaRetiro = obj.getString("moneda");

            //Usuario que retira
            String cedula = session.getAttribute("id");
            usuario = userRepository.findByUid(cedula);
            cuenta = usuario.getNumberAccount();

            //Manejo de la moneda
            monedaUsuario = usuario.getCountry().getCoin();
            money = this.convertMoney(monto,monedaRetiro,monedaUsuario);

            //Validaciones
            saldo = cuenta.getBalance();
            if(saldo>=money){
                cuenta.setBalance(saldo-money);
                pinRetiro = generatePin("retiro");
                accountRepository.save(cuenta);
            } else{
                return "saldo insuficiente";
            }

            //Guardar transaccion
            Transaction transaction = new Transaction();
            transaction.setAccount(cuenta.getNumber());
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            transaction.setDate(dateFormat.format(fecha));
            transaction.setType("RETIRO");
            transaction.setAmount(money);
            transaction.setCoin(monedaUsuario);
            transactionRepository.save(transaction);
            return pinRetiro;
        }catch (Exception e){
            return "Fallo";
        }
    }

    @PostMapping("/registro")
    public String registro (@RequestBody String registro) throws JsonProcessingException, ParseException, JSONException {

        String id;
        String nombre;
        String password;
        String fechaNacimiento;
        String phone;
        String location;
        String country;
        String email;
        String tipoCuenta;
        String passAccount;
        String numberAccount;

        try {
            JSONObject obj=new JSONObject(registro);
            User usr1;
            User usr2;

            //Json a java
            id = obj.getString("id");
            nombre = obj.getString("nombre");
            password = obj.getString("password");
            fechaNacimiento = obj.getString("fechaNacimiento");
            phone = obj.getString("telefono");
            location = obj.getString("residencia");
            country = obj.getString("pais");
            email = obj.getString("correo");
            tipoCuenta = obj.getString("tipoCuenta");
            passAccount = obj.getString("passwordCuenta");
            Country pais = countryRepository.findByName(country);

            usr1 = userRepository.findUserByEmail(email);
            usr2 = userRepository.findByUid(id);
            if(usr1 != null || usr2 != null){
                return "Fallo";
            }

            numberAccount = createNumberAccount();

            //Crear cuenta
            Account cuenta = new Account();
            cuenta.setNumber(numberAccount);
            cuenta.setBalance(0);
            cuenta.setPass(passAccount);
            cuenta.setType(tipoCuenta);
            accountRepository.save(cuenta);

            //Crear usuario
            User usuario = new User();
            usuario.setId(id);
            usuario.setName(nombre);
            usuario.setPass(password);
            usuario.setBirthDay(fechaNacimiento);
            usuario.setPhone(phone);
            usuario.setAddress(location);
            usuario.setCountry(pais);
            usuario.setEmail(email);
            usuario.setRole("cliente");
            usuario.setNumberAccount(cuenta);
            userRepository.save(usuario);

            return numberAccount;

        }catch (Exception e){
            return "Fallo";
        }
    }

    @PostMapping("/listarCuentas")
    public List<Cuenta> listarCuentas () {

        List<Account> cuentas1;
        List<Cuenta> cuentas2 = new ArrayList<Cuenta>();
        cuentas1 = accountRepository.findAll();
        Cuenta c;
        User user;

        try {
            for(Account cuenta:cuentas1){
                user=userRepository.findByNumberAccount(cuenta);
                c = new Cuenta();
                c.setNombre(user.getName());
                c.setNroCuenta(cuenta.getNumber());
                cuentas2.add(c);
            }
            return cuentas2;

        }catch (Exception e){
            return null;
        }
    }

    public String createNumberAccount(){
        String max = accountRepository.findLastAccount();
        int newAccount = Integer.parseInt(max);
        newAccount++;
        max = Integer.toString(newAccount);
        return max;
    }

    public  Double convertMoney(Double monto, String monedaOrigen, String monedaDestino ){
        return monto;
    }

    public boolean validateToken(String token){
        return (session.getId()==token);
    }

    public String generateToken() {
        SecureRandom random = new SecureRandom();

        Long longToken = Math.abs( random.nextLong());
        String random1 = Long.toString(longToken,1021202322);
        return random1;
    }

    public String generatePin(String tipo){
        Random rgn = new Random();
        int num;
        if(tipo.equals("ingreso")) {
            num=1000;
        } else{
            num=10000;
        }
        int digi = rgn.nextInt(9000) + num;
        return Integer.toString(digi);
    }

    public String sendPin(String pin, String numero) {
        try {
            // construir estructura
            String apiKey = "apikey=" + "bXhJc7jMfE0-17gqqc6ZZs39PcUeCgrn8Q6fvxX3GZ";
            String message = "&message=" +"Verification code: "+ pin;
            String sender = "&sender=" + "El juan";
            String numbers = "&numbers=" + numero;

            // enviar datos
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }
}
