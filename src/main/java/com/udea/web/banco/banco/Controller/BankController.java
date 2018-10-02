package com.udea.web.banco.banco.Controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.udea.web.banco.banco.Model.Account;
import com.udea.web.banco.banco.Model.Pin;
import com.udea.web.banco.banco.Model.Transaction;
import com.udea.web.banco.banco.Model.User;
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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;



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
    public String logini(@RequestBody String credentials){

        User user=null;
        try {
            JSONObject obj=new JSONObject(credentials);
            String correo=obj.getString("correo");
            String contrasena = obj.getString("contrasena");
            //Decodificar
            user=userRepository.findUser(correo,contrasena);
            if (user!=null){
                String pin =generatePin();
                if(pinRepository.findByNumber(pin)!=null){

                }
            }

            else
                    return "Acceso incorrecto";
                            //generatePin();
        }catch (Exception e){

            return "Men aca no fue";
        }


    }


    @PostMapping("/loginf")
    public TokenObject loginF(@RequestBody String credentials){
        TokenObject tokenObject;
        try{
            Duration s=Duration.ofMinutes(5);
            JSONObject obj=new JSONObject(credentials);
            String correo=obj.getString("correo");
            String pin = obj.getString("pin");
            tokenObject =new TokenObject(generateToken());
            session= new MapSession(tokenObject.getToken());
            session.setMaxInactiveInterval(s);
            return tokenObject;
        }
        catch (Exception e){
            return tokenObject =new TokenObject("error");
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
    public String generatePin(){
        Random rgn = new Random();
        int digi=rgn.nextInt(9000)+1000;
        return Integer.toString(digi);
    }

    public String sendPin(String pin, String numero) {
        try {
            // Construct data
            String apiKey = "apikey=" + "bXhJc7jMfE0-17gqqc6ZZs39PcUeCgrn8Q6fvxX3GZ";
            String message = "&message=" + pin;
            String sender = "&sender=" + "El juan";
            String numbers = "&numbers=" + numero;

            // Send data
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
