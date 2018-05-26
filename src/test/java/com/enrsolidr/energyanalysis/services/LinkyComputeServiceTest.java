package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.util.RestTemplateWithCookies;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LinkyComputeServiceTest {

    @Mock
    LinkyDataService linkyDataService;

    @InjectMocks
    LinkyComputeService linkyComputeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void canRetrieveLastEnergyUsage() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.YEAR, 2018);
        String body = "{\"etat\":{\"valeur\":\"termine\"},\"graphe\":{\"decalage\":15,\"puissanceSouscrite\":0,\"periode\":{\"dateFin\":\"11/05/2018\",\"dateDebut\":\"10/05/2018\"},\"data\":[{\"valeur\":-1,\"ordre\":1},{\"valeur\":-1,\"ordre\":2},{\"valeur\":-1,\"ordre\":3},{\"valeur\":-1,\"ordre\":4},{\"valeur\":-1,\"ordre\":5},{\"valeur\":-1,\"ordre\":6},{\"valeur\":-1,\"ordre\":7},{\"valeur\":-1,\"ordre\":8},{\"valeur\":-1,\"ordre\":9},{\"valeur\":-1,\"ordre\":10},{\"valeur\":-1,\"ordre\":11},{\"valeur\":-1,\"ordre\":12},{\"valeur\":-1,\"ordre\":13},{\"valeur\":-1,\"ordre\":14},{\"valeur\":-1,\"ordre\":15},{\"valeur\":5.832,\"ordre\":1111},{\"valeur\":-1,\"ordre\":17},{\"valeur\":-1,\"ordre\":18},{\"valeur\":-1,\"ordre\":19},{\"valeur\":-1,\"ordre\":20},{\"valeur\":-1,\"ordre\":21},{\"valeur\":-1,\"ordre\":22},{\"valeur\":-1,\"ordre\":23},{\"valeur\":-1,\"ordre\":24},{\"valeur\":-1,\"ordre\":25},{\"valeur\":-1,\"ordre\":26},{\"valeur\":-1,\"ordre\":27},{\"valeur\":-1,\"ordre\":28},{\"valeur\":-1,\"ordre\":29},{\"valeur\":-1,\"ordre\":30},{\"valeur\":-1,\"ordre\":31}]}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(body, HttpStatus.OK);
        when(linkyDataService.getData(anyString(), anyString(), anyString(), anyInt(), any(RestTemplateWithCookies.class))).thenReturn(responseEntity);

        Double energyUsed = linkyComputeService.computePreviousDayEnergyUse("", "", calendar);
        Assert.assertEquals("Bad computed energy use", 5.832d, energyUsed.doubleValue(), 0.0d);
    }

    @Test
    public void canRetrieveMinEnergyUsage() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.YEAR, 2018);
        String body = "{\"etat\":{\"valeur\":\"termine\"},\"graphe\":{\"decalage\":0,\"puissanceSouscrite\":9,\"periode\":{\"dateFin\":\"11/05/2018\",\"dateDebut\":\"10/05/2018\"},\"data\":[{\"valeur\":0.032,\"ordre\":1},{\"valeur\":0.072,\"ordre\":2},{\"valeur\":0.072,\"ordre\":3},{\"valeur\":0.032,\"ordre\":4},{\"valeur\":0.688,\"ordre\":5},{\"valeur\":0.034,\"ordre\":6},{\"valeur\":0.074,\"ordre\":7},{\"valeur\":0.07,\"ordre\":8},{\"valeur\":0.034,\"ordre\":9},{\"valeur\":0.724,\"ordre\":10},{\"valeur\":0.034,\"ordre\":11},{\"valeur\":0.064,\"ordre\":12},{\"valeur\":0.078,\"ordre\":13},{\"valeur\":0.03,\"ordre\":14},{\"valeur\":0.106,\"ordre\":15},{\"valeur\":0.03,\"ordre\":16},{\"valeur\":0.044,\"ordre\":17},{\"valeur\":0.258,\"ordre\":18},{\"valeur\":0.128,\"ordre\":19},{\"valeur\":1.052,\"ordre\":20},{\"valeur\":0.37,\"ordre\":21},{\"valeur\":0.4,\"ordre\":22},{\"valeur\":0.394,\"ordre\":23},{\"valeur\":0.344,\"ordre\":24},{\"valeur\":0.144,\"ordre\":25},{\"valeur\":0.036,\"ordre\":26},{\"valeur\":0.242,\"ordre\":27},{\"valeur\":0.162,\"ordre\":28},{\"valeur\":0.078,\"ordre\":29},{\"valeur\":0.154,\"ordre\":30},{\"valeur\":0.11,\"ordre\":31},{\"valeur\":0.03,\"ordre\":32},{\"valeur\":0.086,\"ordre\":33},{\"valeur\":0.27,\"ordre\":34},{\"valeur\":0.472,\"ordre\":35},{\"valeur\":0.286,\"ordre\":36},{\"valeur\":0.196,\"ordre\":37},{\"valeur\":0.074,\"ordre\":38},{\"valeur\":0.174,\"ordre\":39},{\"valeur\":0.12,\"ordre\":40},{\"valeur\":0.072,\"ordre\":41},{\"valeur\":0.168,\"ordre\":42},{\"valeur\":0.084,\"ordre\":43},{\"valeur\":2.206,\"ordre\":44},{\"valeur\":1.09,\"ordre\":45},{\"valeur\":0.104,\"ordre\":46},{\"valeur\":0.046,\"ordre\":47},{\"valeur\":0.096,\"ordre\":48}]}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(body, HttpStatus.OK);
        when(linkyDataService.getData(anyString(), anyString(), anyString(), anyInt(), any(RestTemplateWithCookies.class))).thenReturn(responseEntity);

        Double energyUsed = linkyComputeService.computePreviousDayMinEnergyUse("", "", calendar);
        Assert.assertEquals("Bad computed energy use", 60d, energyUsed.doubleValue(), 0.0d);
    }
}