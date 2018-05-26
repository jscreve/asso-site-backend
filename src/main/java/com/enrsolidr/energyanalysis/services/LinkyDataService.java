package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.util.RestTemplateWithCookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class LinkyDataService {

    private static final String LOGIN_BASE_URI = "https://espace-client-connexion.enedis.fr";
    private static final String API_BASE_URI = "https://espace-client-particuliers.enedis.fr/group/espace-particuliers";

    private static final String API_ENDPOINT_LOGIN = "/auth/UI/Login";
    private static final String API_ENDPOINT_DATA = "/suivi-de-consommation";

    private static final Logger logger = LoggerFactory.getLogger(LinkyDataService.class);

    public ResponseEntity<String> login(String username, String password, RestTemplateWithCookies restTemplate) throws Exception {

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


    public ResponseEntity<String> getData(String resourceId, String dateStart, String dateEnd, int callNb, RestTemplateWithCookies restTemplate) throws Exception {
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
        if (out.getStatusCode().value() == 302 && callNb <= 1)
            return getData(resourceId, dateStart, dateEnd, callNb + 1, restTemplate);
        else
            return out;
    }
}