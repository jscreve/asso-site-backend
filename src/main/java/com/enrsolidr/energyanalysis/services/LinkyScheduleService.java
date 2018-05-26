package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkyScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(LinkyScheduleService.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private LinkyComputeService linkyComputeService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    EmailService emailService;

    @Scheduled(cron = "${cron.expression}")
    public void computeEnergyUsage() {
        //fetch all members with Linky activated
        List<Member> members = memberService.getMembersWithLinky();
        for (Member member : members) {
            try {
                Double energyUseYesterday = linkyComputeService.computePreviousDayEnergyUse(member.getLinky().getUsername(), member.getLinky().getPassword());
                Double minEnergyYesterday = linkyComputeService.computePreviousDayMinEnergyUse(member.getLinky().getUsername(), member.getLinky().getPassword());
                member.getLinky().setEnergyYesterday(energyUseYesterday);
                member.getLinky().setLowestPowerYesterday(minEnergyYesterday);
                memberService.updateMember(member);
                if (energyUseYesterday > member.getLinky().getThreshold()) {
                    String subject = "Votre consommation électrique";
                    String text = "Votre consommation électrique  : " + energyUseYesterday + " kWh dépasse le seul défini : " + member.getLinky().getThreshold() + " kWh";
                    emailService.sendSimpleMessage(emailFrom, member.getUser().getEmail(), subject, text);
                }
            } catch (Exception e) {
                logger.error("Error occured fetching energy use for member : " + member.getUsername(), e);
                emailService.sendSimpleMessage(emailFrom, member.getUser().getEmail(), "Erreur linky", "Problème de connexion au site Enedis, vérifier vos identifiants de conexion");
            }
        }
    }
}