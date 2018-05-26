package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.util.RestTemplateWithCookies;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LinkyComputeService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateFormatHours = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Logger logger = LoggerFactory.getLogger(LinkyComputeService.class);

    @Autowired
    private LinkyDataService linkyDataService;

    public Double computePreviousDayEnergyUse(String username, String password) throws Exception {
        return computePreviousDayEnergyUse(username, password, Calendar.getInstance());
    }

    public Double computePreviousDayEnergyUse(String username, String password, Calendar fromDate) throws Exception {
        RestTemplateWithCookies restTemplateWithCookies = new RestTemplateWithCookies();
        linkyDataService.login(username, password, restTemplateWithCookies);
        Calendar calStartDate = (Calendar) fromDate.clone();
        calStartDate.add(Calendar.DAY_OF_MONTH, -1);
        calStartDate.set(Calendar.HOUR_OF_DAY, 0);
        calStartDate.set(Calendar.MINUTE, 0);
        calStartDate.set(Calendar.SECOND, 0);
        ResponseEntity<String> out = linkyDataService.getData("urlCdcJour", dateFormat.format(calStartDate.getTime()), dateFormat.format(new Date()), 0, restTemplateWithCookies);
        if (out.getStatusCode().value() == 200) {
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
                Calendar calPreviousDay = (Calendar) fromDate.clone();
                calPreviousDay.add(Calendar.DAY_OF_MONTH, -1);
                String sYesterday = dateFormat.format(calPreviousDay.getTime());
                return powerUsageList.get(sYesterday);
            }
        }
        return -1.0d;
    }

    public Double computePreviousDayMinEnergyUse(String username, String password) throws Exception {
        return computePreviousDayMinEnergyUse(username, password, Calendar.getInstance());
    }

    public Double computePreviousDayMinEnergyUse(String username, String password, Calendar calStartDate) throws Exception {
        RestTemplateWithCookies restTemplateWithCookies = new RestTemplateWithCookies();
        linkyDataService.login(username, password, restTemplateWithCookies);
        calStartDate.add(Calendar.DAY_OF_MONTH, -1);
        calStartDate.set(Calendar.HOUR_OF_DAY, 0);
        calStartDate.set(Calendar.MINUTE, 0);
        calStartDate.set(Calendar.SECOND, 0);
        ResponseEntity<String> out = linkyDataService.getData("urlCdcHeure", dateFormat.format(calStartDate.getTime()), dateFormat.format(new Date()), 0, restTemplateWithCookies);
        if (out.getStatusCode().value() == 200) {
            JsonObject jsonObject = new JsonParser().parse(out.getBody()).getAsJsonObject();
            String etat = jsonObject.get("etat").getAsJsonObject().get("valeur").getAsString();
            if (etat.equals("termine")) {
                Map<String, Double> powerUsageList = new LinkedHashMap<String, Double>();
                int decalage = jsonObject.get("graphe").getAsJsonObject().get("decalage").getAsInt();
                calStartDate.add(Calendar.MINUTE, -(decalage) * 30);
                Iterator<JsonElement> jsonDataIter = jsonObject.get("graphe").getAsJsonObject().get("data").getAsJsonArray().iterator();
                jsonDataIter.forEachRemaining(jsonElement -> {
                    String sDate = dateFormatHours.format(calStartDate.getTime());
                    double powerUsage = jsonElement.getAsJsonObject().get("valeur").getAsDouble();
                    powerUsageList.put(sDate, powerUsage);
                    calStartDate.add(Calendar.MINUTE, 30);
                });
                Double minPowerUsage = 1000.0d;
                Iterator<Map.Entry<String, Double>> powerUsageIter = powerUsageList.entrySet().iterator();
                while (powerUsageIter.hasNext()) {
                    Map.Entry<String, Double> element = powerUsageIter.next();
                    if (element.getValue() > 0 && element.getValue() < minPowerUsage) {
                        minPowerUsage = element.getValue();
                    }
                }
                return minPowerUsage * 2 * 1000;
            }
        }
        return -1.0d;
    }
}