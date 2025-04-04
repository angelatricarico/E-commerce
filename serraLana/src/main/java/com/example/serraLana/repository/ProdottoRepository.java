package com.example.serraLana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.serraLana.model.CategoriaEnum;
import com.example.serraLana.model.Prodotto;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long>{

	List<Prodotto> findByCategoria(CategoriaEnum categoria);
	
	 Optional<Prodotto> findById(Long id);

}
