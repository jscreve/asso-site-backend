package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResource;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResourceAssembler;
import com.enrsolidr.energyanalysis.resources.MemberResource;
import com.enrsolidr.energyanalysis.resources.MemberResourceAssembler;
import com.enrsolidr.energyanalysis.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @Autowired
    MemberService memberService;

    @Autowired
    MemberController() {
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public ResponseEntity<List<MemberResource>> fetchAll() {
        logger.info("Get members : {}");

        List<Member> members = memberService.getAllMembers();

        MemberResourceAssembler resourceAssembler = new MemberResourceAssembler(MemberController.class, MemberResource.class);
        List<MemberResource> resources = resourceAssembler.toResources(members);
        return new ResponseEntity<List<MemberResource>>(resources, HttpStatus.OK);
    }
}