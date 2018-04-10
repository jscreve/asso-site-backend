package com.enrsolidr.energyanalysis;

import com.enrsolidr.energyanalysis.model.ApplicationUser;
import com.enrsolidr.energyanalysis.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EnergyAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyAnalysisApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Value("${mongodb.admin.user}")
	private String mongoDBUser;

	@Value("${mongodb.admin.password}")
	private String mongoDBPassword;

	@Bean
	CommandLineRunner init(final ApplicationUserRepository applicationUserRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
				insertUser();
			}

			private void insertUser() {
				if(applicationUserRepository.findByUsername(mongoDBUser).isEmpty()) {
					ApplicationUser applicationUser = new ApplicationUser();
					applicationUser.setUsername(mongoDBUser);
					applicationUser.setPassword(bCryptPasswordEncoder().encode(mongoDBPassword));
					applicationUserRepository.save(applicationUser);
				}
			}
		};
	}
}
