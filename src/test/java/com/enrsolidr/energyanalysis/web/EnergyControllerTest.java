package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EnergyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void computeEnergyUsage() throws Exception {
        EnergyUsage energyUsage = new EnergyUsage();
        ResponseEntity<EnergyUsageResource> energyUsageResource = this.restTemplate.postForEntity("http://localhost:" + port + "/energy/usage", energyUsage, EnergyUsageResource.class);
        assertEquals(energyUsageResource.getStatusCode().value(), 201);
    }
}