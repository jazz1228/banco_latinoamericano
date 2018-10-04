package com.udea.web.banco.banco;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class Pruebas
{
    public float getConversionRate(String from, String to) throws IOException
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

            return (float) 1.212;
        }catch (Exception e){
            return (float) 1.21;
        }
    }

    public static void main(String[] arguments) throws IOException
    {
        Pruebas yahooCurrencyConverter = new Pruebas();
        float current = yahooCurrencyConverter.getConversionRate("USD", "ILS");
        System.out.println(current);
    }
}
