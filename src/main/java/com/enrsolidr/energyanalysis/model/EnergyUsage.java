package com.enrsolidr.energyanalysis.model;

import com.enrsolidr.energyanalysis.web.EnergyController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "energyUsage")
public class EnergyUsage {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EnergyController.class);
    private static Properties props = new Properties();

    static {
        try {
            props.load(EnergyUsage.class.getClassLoader().getResourceAsStream(("static/energy_usage.ini")));
        } catch(Exception ex) {
            logger.error("error reading ini file", ex);
        }
    }

    private static String LIGHTNING_EU = "LIGHTNING";
    private static String FRIDGE_EU = "FRIDGE";
    private static String FREEZE_EU = "FREEZE";
    private static String TV_EU = "TV";
    private static String DISH_WASHER_EU = "DISH_WASHER";
    private static String TUMBLE_DRYER_EU = "TUMBLE_DRYER";
    private static String WASHING_MACHINE_EU = "WASHING_MACHINE";
    private static String PC_EU = "PC";
    private static String ELECTRIC_HEATING_EU = "ELECTRIC_HEATING";
    private static String GAZ_HEATING_EU = "GAZ_HEATING";
    private static String ELECTRIC_BOILER_EU = "ELECTRIC_BOILER";
    private static String GAZ_BOILER_EU = "GAZ_BOILER";
    private static String ELECTRIC_COOKING_EU = "ELECTRIC_COOKING";
    private static String GAZ_COOKING_EU = "GAZ_COOKING";
    private static String ELECTRIC_OVEN_EU = "ELECTRIC_OVEN";
    private static String MICROWAVE_OVEN_EU = "MICROWAVE_OVEN";
    private static String IRON_EU = "IRON";
    private static String VACUUM_EU = "VACUUM";
    private static String HAIR_DRYER_EU = "HAIR_DRYER";

    @Id
    private String id;

    private HomeDataModel homeDataModel = new HomeDataModel();
    private ElectricityDataModel electricityDataModel = new ElectricityDataModel();
    private GazDataModel gazDataModel = new GazDataModel();
    //home characteristics
    //private ConsumerType consumerType;

    private int knownkWhPerYear;

    public int getEstimatedElectrickWhPerYear() {
        int energyPerYear = 0;
        //electricity
        energyPerYear += (electricityDataModel.isElectricHeating()? Integer.parseInt(props.getProperty(ELECTRIC_HEATING_EU)) * homeDataModel.getAppartmentSize() : 0);
        energyPerYear += (electricityDataModel.isElectricBoiler() ? Integer.parseInt(props.getProperty(ELECTRIC_BOILER_EU)) * homeDataModel.getInhabitants() : 0);
        energyPerYear += Integer.parseInt(props.getProperty(LIGHTNING_EU)) * homeDataModel.getInhabitants();

        energyPerYear += (electricityDataModel.isElectricCooking() ? Integer.parseInt(props.getProperty(ELECTRIC_COOKING_EU)) : 0);
        energyPerYear += (electricityDataModel.isElectricOven() ? Integer.parseInt(props.getProperty(ELECTRIC_OVEN_EU)) : 0);
        energyPerYear += (electricityDataModel.isMicroWaveOven() ? Integer.parseInt(props.getProperty(MICROWAVE_OVEN_EU)) : 0);
        energyPerYear += (electricityDataModel.isIron() ? Integer.parseInt(props.getProperty(IRON_EU)) : 0);
        energyPerYear += (electricityDataModel.isVacuum() ? Integer.parseInt(props.getProperty(VACUUM_EU)) : 0);
        energyPerYear += (electricityDataModel.isHairDryer() ? Integer.parseInt(props.getProperty(HAIR_DRYER_EU)) : 0);
        energyPerYear += electricityDataModel.getFridge() * Integer.parseInt(props.getProperty(FRIDGE_EU));
        energyPerYear += electricityDataModel.getTV() * Integer.parseInt(props.getProperty(TV_EU));
        energyPerYear += electricityDataModel.getDishWasher() * Integer.parseInt(props.getProperty(DISH_WASHER_EU));
        energyPerYear += electricityDataModel.getTumbleDryer() * Integer.parseInt(props.getProperty(TUMBLE_DRYER_EU));
        energyPerYear += electricityDataModel.getWashingMachine()* Integer.parseInt(props.getProperty(WASHING_MACHINE_EU));
        energyPerYear += electricityDataModel.getPCs() * Integer.parseInt(props.getProperty(PC_EU));

        return energyPerYear;
    }

    public int getEstimatedGazkWhPerYear() {
        int energyPerYear = 0;

        //gaz
        energyPerYear += (gazDataModel.isGazHeating()? Integer.parseInt(props.getProperty(GAZ_HEATING_EU)) * homeDataModel.getAppartmentSize() : 0);
        energyPerYear += (gazDataModel.isGazWaterHeating()? Integer.parseInt(props.getProperty(GAZ_BOILER_EU)) * homeDataModel.getInhabitants() : 0);
        energyPerYear += (gazDataModel.isGazCooking()? Integer.parseInt(props.getProperty(GAZ_COOKING_EU)) * homeDataModel.getInhabitants() : 0);

        return energyPerYear;
    }


}

