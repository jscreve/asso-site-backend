package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.ApplicationUser;
import com.enrsolidr.energyanalysis.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		List<ApplicationUser> user = applicationUserRepository.findByUsername(userId);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.get(0).getUsername(), user.get(0).getPassword(), getAuthority(user.get(0)));
	}

	private List<GrantedAuthority> getAuthority(ApplicationUser user) {
		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority))
				.collect(Collectors.toList());
		return authorities;
	}

	public List<ApplicationUser> findAll() {
		List<ApplicationUser> list = new ArrayList<>();
		applicationUserRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	public ApplicationUser save(ApplicationUser user) {
        return applicationUserRepository.save(user);
    }

	public ApplicationUser findOne(String name) {
		List<ApplicationUser> user = applicationUserRepository.findByUsername(name);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		} else {
			return user.get(0);
		}
	}
}
