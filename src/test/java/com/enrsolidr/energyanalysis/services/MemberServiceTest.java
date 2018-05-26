package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.model.Address;
import com.enrsolidr.energyanalysis.model.Linky;
import com.enrsolidr.energyanalysis.model.User;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import com.enrsolidr.energyanalysis.util.SecurityConstants;
import com.enrsolidr.energyanalysis.web.PaymentController;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.MEMBER_ROLE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    public void canRetrieveLinkyMember() throws Exception {
        Member member = createMember("toto");
        memberService.addMember(member);
        Assert.assertEquals("Must retrieve one linky member", 1, memberService.getMembersWithLinky().size());
    }

    @Test
    public void canRetrieveMemberByUsername() throws Exception {
        Member member = createMember("toto");
        memberService.addMember(member);
        Assert.assertEquals("Must retrieve one linky member", true, memberService.getMemberByUsername("toto").isPresent());
    }

    @Test
    public void canRetrieveMemberByRole() throws Exception {
        Member member = createMember("toto");
        memberService.addMember(member);
        Assert.assertEquals("Must retrieve one linky member", 1, memberService.getMembersWithRole(SecurityConstants.MEMBER_ROLE).size());
    }

    @Test
    public void canUpdateMember() throws Exception {
        Member member = createMember("toto");
        memberService.addMember(member);
        member.getLinky().setActivated(false);
        memberService.updateMember(member);
        Assert.assertEquals("Must not retrieve linky members", 0, memberService.getMembersWithLinky().size());
    }

    private Member createMember(String username) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword("");
        User user = new User();
        user.setEmail("toto@toto.com");
        user.setLast_name("lastname");
        user.setNames("names");
        user.setPhone("0628337914");
        Address address = new Address();
        address.setCity("LILLE");
        address.setCountry("France");
        address.setPostalCode("59000");
        address.setStreet("Bastion Saint André");
        user.setAddress(address);
        member.setUser(user);
        Linky linky = new Linky();
        linky.setUsername("user");
        linky.setPassword("pass");
        linky.setActivated(true);
        member.setLinky(linky);
        member.setAuthorities(Collections.singletonList(MEMBER_ROLE));
        member.getMemberPayments().add(PaymentController.simpleDateFormat.format(new Date()));
        return member;
    }


}