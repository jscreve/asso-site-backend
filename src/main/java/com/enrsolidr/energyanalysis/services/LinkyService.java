package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.util.RestTemplateWithCookies;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LinkyService {

    private static final String LOGIN_BASE_URI = "https://espace-client-connexion.enedis.fr";
    private static final String API_BASE_URI = "https://espace-client-particuliers.enedis.fr/group/espace-particuliers";

    private static final String API_ENDPOINT_LOGIN = "/auth/UI/Login";
    private static final String API_ENDPOINT_DATA = "/suivi-de-consommation";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private RestTemplate restTemplate = new RestTemplateWithCookies();

    public static void main(String[] args) throws Exception {
        LinkyService linkyService = new LinkyService();
    }

    public Double getPreviousDayEnergyUse(String username, String password) throws Exception {
        this.login(username, password);
        Calendar calStartDate = Calendar.getInstance();
        calStartDate.add(Calendar.MONTH, -1);
        ResponseEntity<String> out = this.getData("urlCdcJour", dateFormat.format(calStartDate.getTime()), dateFormat.format(new Date()));
        JsonObject jsonObject = new JsonParser().parse(out.getBody()).getAsJsonObject();
        String etat = jsonObject.get("etat").getAsJsonObject().get("valeur").getAsString();
        if (etat.equals("termine")) {
            Map<String, Double> powerUsageList = new LinkedHashMap<String, Double>();
            int decalage = jsonObject.get("graphe").getAsJsonObject().get("decalage").getAsInt();
            calStartDate.add(Calendar.DAY_OF_MONTH, -decalage);
            Iterator<JsonElement> jsonDataIter = jsonObject.get("graphe").getAsJsonObject().get("data").getAsJsonArray().iterator();
            jsonDataIter.forEachRemaining(jsonElement -> {
                String sDate = dateFormat.format(calStartDate.getTime());
                double powerUsage = jsonElement.getAsJsonObject().get("valeur").getAsDouble();
                powerUsageList.put(sDate, powerUsage);
                calStartDate.add(Calendar.DAY_OF_MONTH, 1);
            });
            Calendar calPreviousDay = Calendar.getInstance();
            calPreviousDay.add(Calendar.DAY_OF_MONTH, -1);
            String sYesterday = dateFormat.format(calPreviousDay.getTime());
            return powerUsageList.get(sYesterday);
        }
        return -1.0d;
    }

    private ResponseEntity<String> login(String username, String password) throws Exception {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("IDToken1", username);
        requestBody.add("IDToken2", password);
        requestBody.add("SunQueryParamsString", Base64.getEncoder().encodeToString("realm=particuliers".getBytes()));
        requestBody.add("encoded", "true");
        requestBody.add("gx_charset", "UTF-8");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        requestHeaders.setAccept(new ArrayList<MediaType>(mediaTypes));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestBody, requestHeaders);

        ResponseEntity<String> output = restTemplate.postForEntity(LOGIN_BASE_URI + API_ENDPOINT_LOGIN, entity, String.class);
        return output;
    }


    private ResponseEntity<String> getData(String resourceId, String dateStart, String dateEnd) throws Exception {
        String req_part = "lincspartdisplaycdc_WAR_lincspartcdcportlet";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_BASE_URI + API_ENDPOINT_DATA);
        builder.queryParam("p_p_id", req_part);
        builder.queryParam("p_p_lifecycle", 2);
        builder.queryParam("p_p_state", "normal");
        builder.queryParam("p_p_mode", "view");
        builder.queryParam("p_p_resource_id", resourceId);
        builder.queryParam("p_p_cacheability", "cacheLevelPage");
        builder.queryParam("p_p_col_id", "column-1");
        builder.queryParam("p_p_col_pos", 1);
        builder.queryParam("p_p_col_count", 3);


        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();

        requestBody.add("_" + req_part + "_dateDebut", dateStart);
        requestBody.add("_" + req_part + "_dateFin", dateEnd);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        requestHeaders.setAccept(new ArrayList<MediaType>(mediaTypes));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestBody, requestHeaders);

        ResponseEntity<String> out = restTemplate.postForEntity(builder.build().encode().toUri(), entity, String.class);
        if (out.getStatusCode().value() == 302)
            return getData(resourceId, dateStart, dateEnd);
        else
            return out;
    }
}