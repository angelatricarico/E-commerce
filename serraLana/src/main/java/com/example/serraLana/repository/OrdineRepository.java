package com.example.serraLana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.serraLana.model.Ordine;
import com.example.serraLana.model.Utente;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long>{
	
	@Query("SELECT o.id AS id, o.dataCreazione AS dataCreazione, o.status AS status, " +
		       "GROUP_CONCAT(p.prodotto.titolo) AS titoli " +
		       "FROM Ordine o " +
		       "JOIN o.prodotti p " +
		       "WHERE o.utente = :utente " +
		       "GROUP BY o.id")
		List<Object[]> findByUtente(Utente utente);




}
