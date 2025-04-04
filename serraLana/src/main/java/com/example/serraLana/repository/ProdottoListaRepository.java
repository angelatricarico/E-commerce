package com.example.serraLana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.serraLana.model.Prodotto;
import com.example.serraLana.model.ProdottoLista;
import com.example.serraLana.model.Utente;

@Repository
public interface ProdottoListaRepository extends JpaRepository<ProdottoLista, Long>{
	
	Optional<ProdottoLista> findByUtenteAndProdotto(Utente utente, Prodotto prodotto);

	List<ProdottoLista> findByUtente(Utente utente);


}
