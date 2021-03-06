package com.fd.mvc.common;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

import static com.fd.mvc.common.Constants.APPLICATION_JSON;
import static com.fd.mvc.common.Constants.CONTENT_TYPE;

public class Utils {

    public static JsonObject readUrlJSON(String urlJSON) {

        JsonObject jsonTricker = null;

        try {
            // Send request
            HttpGet postRequest = new HttpGet(urlJSON);
            postRequest.addHeader(CONTENT_TYPE, APPLICATION_JSON); //El tipo de contenido que regresará BITSO_URL_TICKER

            /*
             * HttpClients - Factory methods
             * In OOP Factory is and object for creating other objects.
             * */
            CloseableHttpResponse response = HttpClients.createDefault().execute(postRequest);

            try {
                //Leer el HttpResponse de la URL enviada como parámetro.
                jsonTricker = Json.createReader(response.getEntity().getContent()).readObject();
            } catch (JsonParsingException jp) {
                //El padre es RuntimeException, por lo cual previamente el desarrollador debio de considerar que esto no ocurra.
                System.out.println("ERROR AL TRATAR DE OBTENER EL JSON DE " + urlJSON);
                jp.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("ERROR AL TRATAR DE OBTENER EL JSON DE " + urlJSON);
            e.printStackTrace();
        }

        return jsonTricker;
    }

}
