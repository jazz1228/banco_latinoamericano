package com.udea.web.banco.banco.Controller;



import com.fasterxml.jackson.core.JsonProcessingException;

import com.udea.web.banco.banco.Model.*;
import com.udea.web.banco.banco.Object.Mensaje;

import com.udea.web.banco.banco.Object.TokenObject;
import com.udea.web.banco.banco.Object.*;

import com.udea.web.banco.banco.Repository.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
    MapSession session=new MapSession("");


    @PostMapping("/logini")
    public Mensaje logini(@RequestBody String credentials){

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
                    //sendPin(pin, user.getPhone());
                    session.setAttribute("pin",pin);
                    Pin pin1=new Pin();
                    pin1.setIdUser(user);
                    pin1.setNumber(pin);
                    pin1.setStartDate(startDate);
                    pin1.setEndDate(".");
                    pinRepository.save(pin1);
                    Mensaje mensaje=new Mensaje("enviado");
                    return mensaje;

            }

            else{
                Mensaje mensaje=new Mensaje("error");
                return mensaje;
            }

        }catch (Exception e){

            Mensaje mensaje=new Mensaje("error");
            return mensaje;
        }
    }


    @PostMapping("/loginf")
    public TokenObject loginF(@RequestBody String credentials){
        TokenObject tokenObject;
        try{
            User user;
            Duration minus=Duration.ofMinutes(5);
            JSONObject obj=new JSONObject(credentials);
            String correo=obj.getString("correo");
            String pin = obj.getString("pin");
            user=userRepository.findUserByEmail(correo);
            String aux=pinRepository.findByNumber(pin).getEndDate();
            if(pinRepository.findByNumber(pin)!=null && aux.equals(".")) {
                tokenObject = new TokenObject(generateToken(),user.getRole());
                session.setId(tokenObject.getToken());
                session.setAttribute("id",user.getId());
                session.setAttribute("rol",user.getRole());
                if(user.getRole().equals("cliente"))
                    session.setMaxInactiveInterval(minus);
                return tokenObject;
            }else
                return tokenObject =new TokenObject("error","");
        }
        catch (Exception e){
            return tokenObject =new TokenObject("error","");
        }

    }

    @PostMapping("/logout")
    public Mensaje logout(@RequestHeader("token") String token) throws JSONException {
        Mensaje message;

        Pin pin;
        if(validateTokenandActiveSession(token, session)) {

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String endDate = dateFormat.format(date);
                session.setId("");
                pin = pinRepository.findByNumber(session.getAttribute("pin"));
                session.removeAttribute("id");
                session.removeAttribute("pin");
                session.removeAttribute("rol");
                session.setMaxInactiveInterval(Duration.ofNanos(1));
                pin.setEndDate(endDate);
                pinRepository.save(pin);
                message = new Mensaje("exitoso");
                return message;


        }else{
            message = new Mensaje("sesion finalizada");
            return message;
        }

    }




    @PostMapping("/consignacion")
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT)
    public Mensaje consignacion (@RequestBody String consignacion1, @RequestHeader("token") String token){
        Mensaje message;
        if(validateTokenandActiveSession(token, session)) {
            Account account;
            Double money;
            User usuario;
            String monedaDestino;


            try {
                JSONObject obj = new JSONObject(consignacion1);

                //Conversion a Json a java
                String cedula = obj.getString("cedula");
                String cuentaDestino = obj.getString("cuentaDestino");
                Double monto = obj.getDouble("monto");
                String moneda = obj.getString("moneda");

                account = accountRepository.findByUid(cuentaDestino);

                //Conversion de moneda y cambio en cuenta
                usuario = userRepository.findByNumberAccount(account);
                monedaDestino = usuario.getCountry().getCoin();
                money = convertMoney(monto, moneda, monedaDestino);

                account.setBalance(account.getBalance() + money);
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
                transaction.setAmount(monto);
                transaction.setCoin(moneda);
                transaction.setFinalAccount(account);
                transactionRepository.save(transaction);
                message = new Mensaje("exitoso");
                return message;
            } catch (Exception e) {
                message = new Mensaje("error");
                return message;
            }
        }else{
            message = new Mensaje("sesion finalizada");
            return message;
        }
    }


    @PostMapping("/retiro")
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT)
    public PinRetiro retiro (@RequestBody String retiro, @RequestHeader("token") String token) throws JsonProcessingException, ParseException, JSONException {
        if(validateTokenandActiveSession(token, session)){
        String pinRetiro = null;
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


            }else{
                PinRetiro pin = new PinRetiro("no money");
                return  pin;
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
            PinRetiro pin = new PinRetiro(pinRetiro);
            return pin;
        }catch (Exception e){
            PinRetiro pin = new PinRetiro("error");
            return pin;
        }}
        else{
            PinRetiro pin = new PinRetiro("sesion finalizada");
            return pin;
        }
    }

    @PostMapping("/transferencia")
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT)
    public Mensaje transferencia (@RequestBody String consignacion,@RequestHeader("token") String token){
        Mensaje message;
        if(validateTokenandActiveSession(token, session)) {
        Account accountOrigen;
        Account accountDestino;
        Double money;
        User usuario;
        String monedaOrigen;
        String monedaDestino;

        try {
            JSONObject obj=new JSONObject(consignacion);

            //Conversion a Json a java
            String cedula=session.getAttribute("id");

            String cuentaDestino = obj.getString("cuentaDestino");
            Double monto = obj.getDouble("monto");
            String moneda = obj.getString("moneda");


            //Obtengo este usuario
            usuario= userRepository.findByUid(cedula);
            //Obtengo la cuenta de origen
            accountOrigen=usuario.getNumberAccount();
            //Obtengo la cuenta de destino
            accountDestino=accountRepository.findByUid(cuentaDestino);

            monedaOrigen=usuario.getCountry().getCoin();
            monedaDestino=userRepository.findByNumberAccount(accountDestino).getCountry().getCoin();
            money = this.convertMoney(monto,moneda,monedaOrigen);
            //Validaciones
            double saldo = accountOrigen.getBalance();
            if(saldo>=money){
                accountOrigen.setBalance(saldo-money);
                accountRepository.save(accountOrigen);
            } else{
                message = new Mensaje("no money");
                return message;
            }
            money = this.convertMoney(monto,moneda,monedaDestino);
            accountDestino.setBalance(money+accountDestino.getBalance());
            accountRepository.save(accountDestino);

            //Guardar transaccion
            Transaction transaction = new Transaction();
            transaction.setAccount(accountOrigen.getNumber());
            transaction.setFinalAccount(accountDestino);
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            transaction.setDate(dateFormat.format(fecha));
            transaction.setType("TRANSFERENCIA");
            transaction.setAmount(monto);
            transaction.setCoin(moneda);
            transactionRepository.save(transaction);
            message = new Mensaje("exitoso");
            return message;

        }catch (Exception e){
            message = new Mensaje("error");
            return message;
        }
        }else{
            message = new Mensaje("sesion finalizada");
            return message;
        }
    }

    @PostMapping("/registro")
    public NumeroCuenta registro (@RequestBody String registro) throws JsonProcessingException, ParseException, JSONException {

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
        NumeroCuenta numeroCuenta;

        try {
            JSONObject obj=new JSONObject(registro);

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

            numeroCuenta=new NumeroCuenta(numberAccount);
            return numeroCuenta;

        }catch (Exception e){
            numeroCuenta=new NumeroCuenta("error");
            return numeroCuenta;
        }
    }

    @PostMapping("/listarCuentas")
    public List<Cuenta> listarCuentas (@RequestHeader("token") String token) {
        if((validateTokenandActiveSession(token, session)) && (session.getAttribute("rol").equals("admin"))){
            List<Account> cuentas1;
            List<Cuenta> cuentas2 = new ArrayList<Cuenta>();
            cuentas1 = accountRepository.findAll();
            Cuenta c;
            User user;

            try {
                for (Account cuenta : cuentas1) {
                    user = userRepository.findByNumberAccount(cuenta);
                    c = new Cuenta();
                    c.setNombre(user.getName());
                    c.setNroCuenta(cuenta.getNumber());
                    cuentas2.add(c);
                }
                return cuentas2;

            } catch (Exception e) {
                return null;
            }
        }else{
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
            

    public  Double convertMoney(Double valor, String MonedaOrigen, String MonedaDestino ) throws IOException, JSONException {
        Double resultado = null;
        Double conversor = null;
        String valorString=String.valueOf(valor);;
        String url = "https://api.cambio.today/v1/quotes/"+MonedaOrigen+"/"+MonedaDestino+"/json?quantity=1&key=286|FXQk~MZrd_QW6a*6ZGNv1c3~^LZ08i03";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        System.out.println(response.toString());
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());

        System.out.println(myResponse.getJSONObject("result").toString());
        JSONObject resul = myResponse.getJSONObject("result");

        conversor = resul.getDouble("value");

        resultado= conversor*valor;

        System.out.println(resultado);
        return resultado;
    }

    //Me valida Que el usuario este conectado y que no haya expirado su sesion
    public boolean validateTokenandActiveSession(String token, MapSession session){
        String vea=session.getAttribute("pin");
        System.out.println(vea);
        System.out.println(session.getId());
        System.out.println(token);
        if((session.getId().equals(token)) && (!session.isExpired()))
            return true;
        else
            return false;
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
        if(tipo.equals("ingreso")){
            num=1000;

        }else{
            num=10000;
        }
        int digi=rgn.nextInt(9000)+num;
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

    @PostMapping("/buscaPorCuenta")
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT)
    public Historial buscaPorCuenta(@RequestBody String cuenta,@RequestHeader("token") String token){
        if((validateTokenandActiveSession(token, session)) && (session.getAttribute("rol").equals("admin"))){

        Account account;
        User usuario;
        Historial historial;

        try {
            JSONObject obj=new JSONObject(cuenta);

            //Conversion a Json a java
            String numeroCuenta=obj.getString("numeroCuenta");

            account=accountRepository.findByUid(numeroCuenta);
            usuario= userRepository.findByNumberAccount(account);

            historial = new Historial(usuario.getId());
            historial.setNombre(usuario.getName());
            historial.setFecha(usuario.getBirthDay());
            historial.setTelefono(usuario.getPhone());
            historial.setDireccion(usuario.getAddress());
            historial.setPais(usuario.getCountry().getName());
            historial.setCorreo(usuario.getEmail());
            historial.setTipo(account.getType());
            historial.setSaldo(account.getBalance());

            List<Transaction> tranR;

            List<Transacciones> aux1= new ArrayList<Transacciones>();
            tranR=transactionRepository.findAllByAccount(numeroCuenta,account);

            for(Transaction t:tranR){

                String aux="";
                if((t.getAccount().equals(numeroCuenta)) && (t.getType().equals("CONSIGNACION")))
                    aux=" REALIZADA";
                else if((!t.getAccount().equals(numeroCuenta)) && (t.getType().equals("CONSIGNACION")))
                    aux=" RECIBIDA";

                Transacciones transac=new Transacciones(t.getType()+aux,t.getAmount(),t.getDate(),t.getCoin());
                aux1.add(transac);

            }
            historial.setTransacciones(aux1);
            return historial;
        }catch (Exception e){
            return null;
        }

    }else{
           return null;
        }
    }

}
