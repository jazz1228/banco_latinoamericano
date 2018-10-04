package com.udea.web.banco.banco;


import org.apache.catalina.WebResource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Pruebas


{

   /* public float getConversionRate(String from, String to) throws IOException
    {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost get = new HttpPost("http://api.cambio.today/v1/quotes/EUR/USD/json?quantity=1&key=286|FXQk~MZrd_QW6a*6ZGNv1c3~^LZ08i03");
        try {

            CloseableHttpResponse response =  httpClient.execute(get);
           // HttpEntity entity = response.getEntity();
            //JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            System.out.println("hola");
            //System.out.println(exchangeRates.getString("result")+"hola");
            /* HttpGet httpGet = new HttpGet("https://api.cambio.today/v1/quotes/EUR/USD/json?quantity=1&key=286|FXQk~MZrd_QW6a*6ZGNv1c3~^LZ08i03");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httpGet.
            String responseBody = httpclient.execute(httpGet, responseHandler);*/

            /*return (float) 1.212;
        }catch (Exception e){
            return (float) 1.21;
        }
    }*/

    public static void main(String[] arguments) throws IOException, URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        Response geoIPXml=null;
        geoIPXml = restTemplate.getForObject(new URI("http://api.cambio.today/v1/quotes/USD/COP/json?quantity=1&key=286|FXQk~MZrd_QW6a*6ZGNv1c3~^LZ08i03") , Response.class);




    }
}
