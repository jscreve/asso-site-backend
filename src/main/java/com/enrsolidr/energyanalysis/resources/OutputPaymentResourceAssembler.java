package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.entity.Payment;
import com.enrsolidr.energyanalysis.web.MemberController;
import com.enrsolidr.energyanalysis.web.PaymentController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutputPaymentResourceAssembler extends ResourceAssemblerSupport<Payment, OutputPaymentResource> {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");


    public OutputPaymentResourceAssembler(Class<?> controllerClass, Class<OutputPaymentResource> resourceType) {
        super(controllerClass, resourceType);
    }


    public OutputPaymentResource toResource(Payment entity) {
        OutputPaymentResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(PaymentController.class).fetchAll()).withSelfRel());
        resource.setUser(entity.getUser());
        resource.setAmount(entity.getAmount());
        resource.setPaymentDate(simpleDateFormat.format(entity.getPaymentDate()));
        resource.setTransactionType(entity.getTransactionType());
        return resource;
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.mvc.ResourceAssemblerSupport#toResources(java.lang.Iterable)
     */
    @Override
    public List<OutputPaymentResource> toResources(Iterable<? extends Payment> entities) {
        return super.toResources(entities);
    }
}
