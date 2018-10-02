package com.udea.web.banco.banco;

import org.springframework.session.MapSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class Pruebas {
    public static void main(String arg []){
        Date date = new Date();

        MapSession session=new MapSession("12413423rddfd");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate=dateFormat.format(date);
        Duration s = Duration.ofMinutes(5);
        Instant t1, t2;
        t1=Instant.now();
       // System.out.println(t1.);
        session.setAttribute("id","juancho");

        session.setMaxInactiveInterval(Duration.ofSeconds(5));
        System.out.println(session.getMaxInactiveInterval());
    }

}
