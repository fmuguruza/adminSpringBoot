package com.fd.adminSpringBoot.controller;

import static com.fd.adminSpringBoot.data_service.criptomonedas.bisto.BitsoConstants.BITSO_URL_TICKER;
import static com.fd.adminSpringBoot.data_service.utils.AdminSpringConstants.APPLICATION_JSON;
import static com.fd.adminSpringBoot.data_service.utils.AdminSpringConstants.CONTENT_TYPE;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fd.adminSpringBoot.data_service.criptomonedas.bisto.BitsoTricker;


/**
 * 
 * @author Muguruza
 *
 */
@Controller
@RequestMapping(value = "/bitso")
public class BitsoWebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitsoWebController.class);
    private static final String VIEW_BITSO = "bitso/viewBitso";
    
    @RequestMapping(value = "/viewBitso", method = RequestMethod.GET)
    public String home(Model model) {
       try{
        
	        // Send request
	        HttpPost postRequest = new HttpPost(BITSO_URL_TICKER);
	        postRequest.addHeader(CONTENT_TYPE,APPLICATION_JSON); //El tipo de contenido que regresará BITSO_URL_TICKER
	
	        /* 
	         * HttpClients - Factory methods 
	         * In OOP Factory is and object for creating other objects. 
	         * */
	        CloseableHttpResponse response = HttpClients.createDefault().execute(postRequest);
	        
	        JsonObject jsonTricker;
	        try{
	        	//Leer el HttpResponse de la URL enviada como parámetro.
	        	jsonTricker = Json.createReader(response.getEntity().getContent()).readObject();
	        }catch(JsonParsingException jp){
	        	//El padre es RuntimeException, por lo cual previamente el desarrollador debio de considerar que esto no ocurra.
	        	LOGGER.error("ERROR AL TRATAR DE OBTENER EL JSON DE " + BITSO_URL_TICKER,jp);
	        	return VIEW_BITSO;
	        }
	        	
	        
        	//Obtener el valor booleano del atributo success, si este no se encuentra en el JSON obtener false por default.
        	if(jsonTricker.getBoolean("success",false)){

        		List<BitsoTricker> listBitsoTricker = new ArrayList<>(); 
        		
        		/*
        		 * Obtener el JSONArray de las divisas que se encuentran en el atributo payload.
        		 */
        		JsonArray jsonArray = jsonTricker.getJsonArray("payload");
        		jsonArray.getValuesAs(JsonObject.class).forEach(item->{
        			JsonObject jObj = (JsonObject) item;
        			
        			BitsoTricker bitsoTricker = new BitsoTricker();
        			bitsoTricker.setHigh(new BigDecimal(jObj.getString("high")));
        			bitsoTricker.setLast(new BigDecimal(jObj.getString("last")));
        			bitsoTricker.setCreated_at(LocalDateTime.parse(jObj.getString("created_at"),DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        			bitsoTricker.setBook(jObj.getString("book"));
        			bitsoTricker.setVolume(new BigDecimal(jObj.getString("volume")));
        			bitsoTricker.setVwap(new BigDecimal(jObj.getString("vwap")));
        			bitsoTricker.setLow(new BigDecimal(jObj.getString("low")));
        			bitsoTricker.setAsk(new BigDecimal(jObj.getString("ask")));
        			bitsoTricker.setBid(new BigDecimal(jObj.getString("bid")));
        			
        			listBitsoTricker.add(bitsoTricker);
        			
        		});
        		
        		
	        }
	        	
	        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return VIEW_BITSO;
    }
    
    /**
     * Process Tiwilio SMS.
     * @param twilioSMSSearchCriteria
     * @param result
     * @param model
     * @return
     */
//    @PostMapping("/viewBitso")
//    public String processTiwilioSMS(@ModelAttribute("twilioSMSSearchCriteria") TwilioSMSSearchCriteria twilioSMSSearchCriteria, BindingResult result, Model model) {
//        
//    	String phoneNumberTo = StringUtils.stripToEmpty(twilioSMSSearchCriteria.getPhoneNumberTo());
//    	phoneNumberTo = phoneNumberTo.replaceAll("[^0-9]",""); //Remove different characters that number. 
//    	twilioSMSSearchCriteria.setPhoneNumberTo(phoneNumberTo);
    	
//        try {
//            twilioValidator.validate(twilioSMSSearchCriteria, result);
//            if (result.hasErrors()) {
//                return VIEW_TIWILIO_SMS;
//            }
//            
//            TwilioSMSResult twilioSMSResult = twilioService.sendSMS(twilioSMSSearchCriteria);
//            model.addAttribute("msgResult", twilioSMSResult.getMessage());
//
//        } catch (Exception e) {
//            LOGGER.debug("Exception processTiwilioSMS", e);
//            result.addError(new ObjectError("exception", e.getMessage()));
//        }
        
//        return VIEW_TIWILIO_SMS;
//    }
    
}
