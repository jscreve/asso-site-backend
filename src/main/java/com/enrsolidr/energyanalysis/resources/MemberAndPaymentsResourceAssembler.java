package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.web.MemberController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MemberAndPaymentsResourceAssembler extends ResourceAssemblerSupport<Member, MemberAndPaymentsResource> {


    public MemberAndPaymentsResourceAssembler(Class<?> controllerClass, Class<MemberAndPaymentsResource> resourceType) {
        super(controllerClass, resourceType);
    }


    public MemberAndPaymentsResource toResource(Member entity) {
        MemberAndPaymentsResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(MemberController.class).fetchAll()).withSelfRel());
        resource.setUser(entity.getUser());
        resource.setMemberPayments(entity.getMemberPayments());
        return resource;
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.mvc.ResourceAssemblerSupport#toResources(java.lang.Iterable)
     */
    @Override
    public List<MemberAndPaymentsResource> toResources(Iterable<? extends Member> entities) {
        return super.toResources(entities);
    }
}
