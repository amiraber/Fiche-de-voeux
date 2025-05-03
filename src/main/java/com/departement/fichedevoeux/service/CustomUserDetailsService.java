package com.departement.fichedevoeux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Professeur prof = professeurRepository.findByEmail(email);
        if(prof == null) {
        	throw new UsernameNotFoundException("Professeur non trouvé");
        }

        String role = prof.isChef() ? "CHEF_DEP" : "PROF";

        return User.builder()
            .username(prof.getEmail())
            .password(prof.getMotDePasse()) // mot de passe encodé
            .roles(role)
            .build();
    }
}
