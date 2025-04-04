package com.example.serraLana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.serraLana.model.ListaDesideri;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.model.Utente;

public interface ListaDesideriRepository extends JpaRepository<ListaDesideri, Long> {

    Optional<ListaDesideri> findByUtenteAndProdotto(Utente utente, Prodotto prodotto);
}

