package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.model.Email;
import com.enrsolidr.energyanalysis.model.EnergyUsage;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResource;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResourceAssembler;
import com.enrsolidr.energyanalysis.services.EmailService;
import com.enrsolidr.energyanalysis.services.EnergyUsageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    public static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    EmailService emailService;

    @Value("${spring.mail.username}")
    private String emailTo;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<?> sendEmail(@RequestBody Email email) {
        logger.info("Sending email : {}", email);

        emailService.sendSimpleMessage(email.getEmail(), emailTo, email.getSubject(), email.getMessage());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}