package com.enrsolidr.energyanalysis.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import com.enrsolidr.energyanalysis.web.EnergyController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

public class EnergyUsageResourceAssembler extends ResourceAssemblerSupport<EnergyUsage, EnergyUsageResource> {


    public EnergyUsageResourceAssembler(Class<?> controllerClass, Class<EnergyUsageResource> resourceType) {
        super(controllerClass, resourceType);
    }


    public EnergyUsageResource toResource(EnergyUsage entity) {
        EnergyUsageResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(EnergyController.class).computeEnergyUsage(entity)).withSelfRel());
        int eleckWh = entity.getEstimatedElectrickWhPerYear();
        resource.setElectricEnergyUsage(eleckWh);
        int gazkWh = entity.getEstimatedGazkWhPerYear();
        resource.setGazEnergyUsage(gazkWh);
        resource.setAverageEnergy(entity.getAverageEnergy(eleckWh + gazkWh));
        return resource;
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.mvc.ResourceAssemblerSupport#toResources(java.lang.Iterable)
     */
    @Override
    public List<EnergyUsageResource> toResources(Iterable<? extends EnergyUsage> entities) {
        return super.toResources(entities);
    }
}
