package com.udea.web.banco.banco;

import org.springframework.session.MapSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Pruebas {
    public static void main(String arg []){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate=dateFormat.format(date);
        Duration s=Duration.ofSeconds((MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS));
        System.out.println(s);
    }

}
