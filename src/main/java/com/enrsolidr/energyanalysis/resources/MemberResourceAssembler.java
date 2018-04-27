package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.web.EnergyController;
import com.enrsolidr.energyanalysis.web.MemberController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MemberResourceAssembler extends ResourceAssemblerSupport<Member, MemberResource> {


    public MemberResourceAssembler(Class<?> controllerClass, Class<MemberResource> resourceType) {
        super(controllerClass, resourceType);
    }


    public MemberResource toResource(Member entity) {
        MemberResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(MemberController.class).fetchAll()).withSelfRel());
        resource.setUser(entity.getUser());
        resource.setMemberPayments(entity.getMemberPayments());
        return resource;
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.mvc.ResourceAssemblerSupport#toResources(java.lang.Iterable)
     */
    @Override
    public List<MemberResource> toResources(Iterable<? extends Member> entities) {
        return super.toResources(entities);
    }
}
