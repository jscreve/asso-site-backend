package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.exceptions.UserAlreadyExistException;
import com.enrsolidr.energyanalysis.model.Linky;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import com.enrsolidr.energyanalysis.resources.*;
import com.enrsolidr.energyanalysis.services.MemberService;
import com.enrsolidr.energyanalysis.services.UserService;
import com.enrsolidr.energyanalysis.util.SecurityConstants;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.MEMBER_ROLE;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    @Setter
    private MemberService memberService;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody SignUpResource user) throws Exception {
        //create member
        // what if the member already exists ?
        Optional<Member> existingMember = memberService.getMemberByUsername(user.getUsername());
        if (existingMember.isPresent()) {
            throw new UserAlreadyExistException("User already exists");
            //if password is the same, we continue the process
        }

        Member member = new Member();
        Linky linky = new Linky();
        linky.setActivated(false);
        member.setLinky(linky);
        member.setUser(user.getUser());
        member.setUsername(user.getUsername());
        member.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        member.setAuthorities(Collections.singletonList(MEMBER_ROLE));
        memberService.addMember(member);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/remove/{username}")
    public ResponseEntity<?> remove(@PathVariable String username) throws Exception {
        memberService.deleteMemberByUsername(username);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/fetch")
    public ResponseEntity<OutputMemberResource> fetch() {
        logger.info("Get member : {}");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            Optional<Member> member = memberService.getMemberByUsername(username);
            if (member.isPresent()) {
                OutputMemberResourceAssembler resourceAssembler = new OutputMemberResourceAssembler(MemberController.class, OutputMemberResource.class);
                OutputMemberResource resource = resourceAssembler.toResource(member.get());
                return new ResponseEntity<OutputMemberResource>(resource, HttpStatus.OK);
            } else {
                return new ResponseEntity<OutputMemberResource>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<OutputMemberResource>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public ResponseEntity<?> update(@RequestBody InputMemberResource inputMemberResource) throws Exception {
        logger.info("Update member : {}");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            Optional<Member> member = memberService.getMemberByUsername(username);
            if (member.isPresent()) {
                Member tempMember = member.get();
                tempMember = InputMemberResource.fromResource(tempMember, inputMemberResource);
                memberService.updateMember(tempMember);
                return new ResponseEntity<OutputMemberResource>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<OutputMemberResource>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/fetchAllMemberAndPayments", method = RequestMethod.GET)
    public ResponseEntity<List<MemberAndPaymentsResource>> fetchAll() {
        logger.info("Get members : {}");

        List<Member> members = memberService.getMembersWithRole(SecurityConstants.MEMBER_ROLE);
        MemberAndPaymentsResourceAssembler resourceAssembler = new MemberAndPaymentsResourceAssembler(MemberController.class, MemberAndPaymentsResource.class);
        List<MemberAndPaymentsResource> resources = resourceAssembler.toResources(members);
        return new ResponseEntity<List<MemberAndPaymentsResource>>(resources, HttpStatus.OK);
    }
}