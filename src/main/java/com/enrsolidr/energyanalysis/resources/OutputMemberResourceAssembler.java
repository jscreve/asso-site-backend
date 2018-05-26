package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.web.MemberController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutputMemberResourceAssembler extends ResourceAssemblerSupport<Member, OutputMemberResource> {


    public OutputMemberResourceAssembler(Class<?> controllerClass, Class<OutputMemberResource> resourceType) {
        super(controllerClass, resourceType);
    }


    public OutputMemberResource toResource(Member entity) {
        OutputMemberResource resource = createResourceWithId(entity.getId(), entity);
        resource.add(linkTo(methodOn(MemberController.class).fetch()).withSelfRel());
        resource.setUser(entity.getUser());
        resource.setLinky(entity.getLinky());
        resource.setAuthorities(entity.getAuthorities());
        resource.setUsername(entity.getUsername());
        resource.setPassword(entity.getPassword());
        return resource;
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.mvc.ResourceAssemblerSupport#toResources(java.lang.Iterable)
     */
    @Override
    public List<OutputMemberResource> toResources(Iterable<? extends Member> entities) {
        return super.toResources(entities);
    }
}
