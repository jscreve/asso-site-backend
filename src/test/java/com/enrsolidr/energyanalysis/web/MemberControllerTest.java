package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.exceptions.UserAlreadyExistException;
import com.enrsolidr.energyanalysis.model.User;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import com.enrsolidr.energyanalysis.resources.SignUpResource;
import com.enrsolidr.energyanalysis.services.MemberService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class MemberControllerTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        memberController.setMemberService(memberService);
    }

    @Test
    public void canFetchMembers() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("toto");
        member.setId("id");

        //when
        when(memberRepository.findByAuthoritiesIn(Collections.singletonList("ROLE_MEMBER"))).thenReturn(Collections.singletonList(member));

        //test the add functionality
        Assert.assertEquals(1, memberController.fetchAll().getBody().size());
    }

    @Test
    public void canSignUp() throws Exception {
        // given
        SignUpResource signUpResource = new SignUpResource();
        User user = new User();
        user.setEmail("toto@toto.com");
        signUpResource.setUser(user);
        signUpResource.setUsername("toto");
        signUpResource.setPassword("password");

        // given
        Member member = new Member();
        member.setUsername("toto");
        member.setId("id");

        //when
        when(memberRepository.findByUsername(anyString())).thenReturn(Collections.emptyList());

        //test the add functionality
        Assert.assertEquals(201, memberController.signUp(signUpResource).getStatusCodeValue());
        verify(memberRepository).insert(any(Member.class));
    }

    @Test
    public void cannotSignUp() throws Exception {
        assertThrows(UserAlreadyExistException.class, () -> {
            // given
            SignUpResource signUpResource = new SignUpResource();
            User user = new User();
            user.setEmail("toto@toto.com");
            signUpResource.setUser(user);
            signUpResource.setUsername("toto");
            signUpResource.setPassword("password");

            // given
            Member member = new Member();
            member.setUsername("toto");
            member.setId("id");

            //when
            when(memberRepository.findByUsername(anyString())).thenReturn(Collections.singletonList(member));

            //test the add functionality
            memberController.signUp(signUpResource).getStatusCodeValue();
        });
    }

}