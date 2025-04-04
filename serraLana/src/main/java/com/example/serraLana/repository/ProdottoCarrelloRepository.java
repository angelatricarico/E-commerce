package com.example.serraLana.repository;

import com.example.serraLana.model.Carrello;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.model.ProdottoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProdottoCarrelloRepository extends JpaRepository<ProdottoCarrello, Long> {

    Optional<ProdottoCarrello> findByProdottoAndCarrello(Prodotto prodotto, Carrello carrello);

    Optional<ProdottoCarrello> findByProdotto_IdAndCarrello_Id(Long prodottoId, Long carrelloId);
    

}

