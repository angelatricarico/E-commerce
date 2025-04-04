package com.example.serraLana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.serraLana.model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>{
	
	Optional<Utente> findByToken(String token);
	
	Optional<Utente> findByUsername(String username);
	
	Optional<Utente> findByPassword(String password);

}
