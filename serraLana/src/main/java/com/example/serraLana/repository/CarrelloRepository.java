package com.example.serraLana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.serraLana.model.Carrello;
import com.example.serraLana.model.Utente;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long>{
    Optional<Carrello> findByUtente(Utente utente);
}
