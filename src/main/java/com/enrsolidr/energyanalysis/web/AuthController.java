package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.model.AuthToken;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import com.enrsolidr.energyanalysis.resources.LoginMemberResource;
import com.enrsolidr.energyanalysis.services.MemberService;
import com.enrsolidr.energyanalysis.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.EXPIRATION_TIME;
import static com.enrsolidr.energyanalysis.util.SecurityConstants.SECRET;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private MemberRepository memberRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    MemberService memberService;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    public AuthController(MemberRepository memberRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginMemberResource user) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Member member = userService.findOne(user.getUsername());
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        if (member.getAuthorities() == null || member.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(member.getUsername());
        claims.put("scopes", member.getAuthorities());

        final String token = Jwts.builder()
                .setSubject(member.getUsername())
                .setExpiration(expiresAt)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        return ResponseEntity.ok(new AuthToken(token, expiresAt, member.getAuthorities()));
    }
}