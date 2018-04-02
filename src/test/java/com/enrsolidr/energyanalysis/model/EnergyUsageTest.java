package com.enrsolidr.energyanalysis.model;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EnergyUsageTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void computeEstimatedkWhPerYear() throws IOException {
        EnergyUsage energyUsage = new EnergyUsage();
        energyUsage.getHomeDataModel().setAppartmentSize(20);
        energyUsage.getHomeDataModel().setInhabitants(3);
        assertEquals(6969, energyUsage.getEstimatedElectrickWhPerYear());
    }
}