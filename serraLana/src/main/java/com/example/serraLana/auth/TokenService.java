package com.example.serraLana.auth;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.serraLana.repository.UtenteRepository;
import com.example.serraLana.model.*;

import java.util.Optional;
import java.util.UUID;


@Service
public class TokenService {
	
	@Autowired
    private UtenteRepository utenteRepository;


    public String generateToken(String username, String password) {
    	
       Optional<Utente> optionalUser = utenteRepository.findByUsername(username);
        
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("L'utente non esiste");
        }
        
        Utente user = optionalUser.get();
        
        if(!user.getUsername().equals(username)&& !user.getPassword().equals(password)) {
        	 throw new IllegalArgumentException("Credenziali non valide");
        }
    	
        String token = UUID.randomUUID().toString();
        
        user.setToken(token);

        utenteRepository.save(user);

        return token;
    }


    public Optional<Utente> getAuthUser(String token) {
      	Optional<Utente> optionalUser = utenteRepository.findByToken(token);
      	if(!optionalUser.isPresent()) {
      		System.out.println("non è presente");
      	}
        return optionalUser;
    }


    public void removeToken(String token) {
    	Optional<Utente> optionalUser = utenteRepository.findByToken(token);
    	if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Token non trovato: " + token);
        }
        Utente user = optionalUser.get();  
        user.setToken(null);
        utenteRepository.save(user);
    }
}
