package com.udea.web.banco.banco;


import org.apache.catalina.WebResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Pruebas


{
    public static   Double convertMoney(Double valor, String MonedaOrigen, String MonedaDestino ) throws IOException, JSONException {
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


        return resultado;
    }

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

    public static void main(String[] arguments) throws IOException, URISyntaxException, JSONException {

        Double aDouble = Pruebas.convertMoney(1000.0, "USD", "COP");

        System.out.println(aDouble);


    }
}
