package com.example.serraLana.controller;

import java.util.Collections;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serraLana.model.Utente;
import com.example.serraLana.repository.UtenteRepository;
import com.example.serraLana.auth.*;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/utente")
@Validated
@CrossOrigin("*")
public class UtenteController {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
    @Autowired
    private TokenService tokenService;
	
    

    @GetMapping 
    public Object getUtenteById(HttpServletRequest request, HttpServletResponse response) {
    	Optional<Utente> authUser = getAuthenticatedUser(request);
        if (!authUser.isPresent()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Non autorizzato");
        }else {
        	return new ResponseEntity<>(authUser.get(), HttpStatus.OK);
        }
    }
    

    
    @PostMapping
    public ResponseEntity<?> createUtenteUser(@Valid @RequestBody Utente utente, HttpServletRequest request, HttpServletResponse response) {
        if (utente.getPassword() == null || utente.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "La password è obbligatoria per la registrazione"));
        }
        utente.setRuolo("user");
        Utente savedUtente = utenteRepository.save(utente);

        return new ResponseEntity<>(savedUtente, HttpStatus.CREATED);
    }



    
    @PutMapping
    public ResponseEntity<?> updateUtente(@RequestBody Utente updatedUtente, HttpServletRequest request) {
        Optional<Utente> optionalUtente = getAuthenticatedUser(request);

        if (optionalUtente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Autenticazione richiesta"));
        }

        Utente utente = optionalUtente.get();

        if (updatedUtente.getNome() != null) utente.setNome(updatedUtente.getNome());
        if (updatedUtente.getCognome() != null) utente.setCognome(updatedUtente.getCognome());
        if (updatedUtente.getEmail() != null) utente.setEmail(updatedUtente.getEmail());
        if (updatedUtente.getUsername() != null) utente.setUsername(updatedUtente.getUsername());
        if (updatedUtente.getDataNascita() != null) utente.setDataNascita(updatedUtente.getDataNascita());
        if (updatedUtente.getIndirizzo() != null) utente.setIndirizzo(updatedUtente.getIndirizzo());

        if (updatedUtente.getNumeroCarta() != null && 
            updatedUtente.getScadenzaCarta() != null) {
            
            utente.setNumeroCarta(updatedUtente.getNumeroCarta());
            utente.setScadenzaCarta(updatedUtente.getScadenzaCarta());

            if (updatedUtente.getCvcCarta() != null && !updatedUtente.getCvcCarta().equals("•••")) {
                utente.setCvcCarta(updatedUtente.getCvcCarta());
            }
        }

        if (updatedUtente.getPassword() != null && !updatedUtente.getPassword().trim().isEmpty()) {
            utente.setPassword(updatedUtente.getPassword());
        }

        utenteRepository.save(utente);

        return ResponseEntity.ok(Collections.singletonMap("message", "Profilo aggiornato con successo"));
    }




    @DeleteMapping
    public Object deleteUtenteUser(HttpServletRequest request, HttpServletResponse response) {
    	Optional<Utente> authUser = getAuthenticatedUser(request);
        if (!authUser.isPresent()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Non autorizzato");
        }else {
        	utenteRepository.deleteById(authUser.get().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    


    
    private Optional<Utente> getAuthenticatedUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            String token;
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }
            return tokenService.getAuthUser(token);
        }
        System.out.println("Se non c'è header \"Authorization\", restituisce null");
        return null;
    }

}
