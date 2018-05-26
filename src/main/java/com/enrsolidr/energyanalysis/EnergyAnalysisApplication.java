package com.enrsolidr.energyanalysis;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.ADMIN_ROLE;

@SpringBootApplication
@EnableScheduling
public class EnergyAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyAnalysisApplication.class, args);
	}

	private static final Logger logger = LoggerFactory.getLogger(EnergyAnalysisApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Value("${mongodb.admin.user}")
	private String mongoDBUser;

	@Value("${mongodb.admin.password}")
	private String mongoDBPassword;

	@Bean
    CommandLineRunner init(final MemberRepository memberRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
                insertOrUpdateAdminUser();
			}

            private void insertOrUpdateAdminUser() {
				try {
                    List<Member> mongoDbUserList;
                    if (!(mongoDbUserList = memberRepository.findByUsername(mongoDBUser)).isEmpty()) {
						logger.info(mongoDbUserList.get(0).getUsername());
						logger.info(mongoDbUserList.get(0).getPassword());
						logger.info("Id : " + mongoDbUserList.get(0).getId());
                        memberRepository.deleteAll(mongoDbUserList);
					}
                    Member member = new Member();
                    member.setUsername(mongoDBUser);
                    member.setPassword(bCryptPasswordEncoder().encode(mongoDBPassword));
                    member.setAuthorities(Collections.singletonList(ADMIN_ROLE));
                    memberRepository.save(member);
				} catch(Exception ex) {
					logger.error("error at startup", ex);
				}
			}
		};
	}
}
