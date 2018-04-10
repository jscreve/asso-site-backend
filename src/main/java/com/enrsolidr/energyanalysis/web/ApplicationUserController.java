package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.model.ApplicationUser;
import com.enrsolidr.energyanalysis.model.AuthToken;
import com.enrsolidr.energyanalysis.repository.ApplicationUserRepository;
import com.enrsolidr.energyanalysis.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.EXPIRATION_TIME;
import static com.enrsolidr.energyanalysis.util.SecurityConstants.SECRET;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public ApplicationUserController(ApplicationUserRepository applicationUserRepository,
                                     BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public void signUp(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
    }

    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody ApplicationUser user) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final ApplicationUser applicationUser = userService.findOne(user.getUsername());
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        final String token = Jwts.builder()
                .setSubject(applicationUser.getUsername())
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        return ResponseEntity.ok(new AuthToken(token, expiresAt));
    }
}