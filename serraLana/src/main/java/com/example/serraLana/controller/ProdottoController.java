package com.example.serraLana.controller;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serraLana.dto.ProdottoResponseDto;
import com.example.serraLana.model.CategoriaEnum;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.repository.ProdottoRepository;


@RestController
@RequestMapping("/prodotti")
@CrossOrigin ("*")
public class ProdottoController {

    @Autowired
    private ProdottoRepository prodottoRepository;
    


    
    

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponseDto> getProdotto(@PathVariable Long id) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(id);

        if (prodottoOpt.isPresent()) {
            Prodotto prodotto = prodottoOpt.get();
            ProdottoResponseDto response = new ProdottoResponseDto();
            response.setId(prodotto.getId());
            response.setTitolo(prodotto.getTitolo());
            response.setDescrizione(prodotto.getDescrizione());
            response.setPrezzo(prodotto.getPrezzo());
            response.setCategoria(prodotto.getCategoria());

            if (prodotto.getImmagine() != null && prodotto.getImmagine().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(prodotto.getImmagine());
                response.setImmagine(base64Image); 
            } else {
                response.setImmagine(null); 
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



	@GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<ProdottoResponseDto>> getProdottiByCategoria(@PathVariable String categoria) {
	    CategoriaEnum categoriaEnum;
	    try {
	        categoriaEnum = CategoriaEnum.valueOf(categoria.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	
	    List<Prodotto> prodotti = prodottoRepository.findByCategoria(categoriaEnum);
	    List<ProdottoResponseDto> prodottoResponseDtos = new ArrayList<>();
	
	    for (Prodotto prodotto : prodotti) {
	        ProdottoResponseDto response = new ProdottoResponseDto();
	        response.setId(prodotto.getId());
	        response.setTitolo(prodotto.getTitolo());
	        response.setDescrizione(prodotto.getDescrizione());
	        response.setPrezzo(prodotto.getPrezzo());
	        response.setCategoria(prodotto.getCategoria());
	
	        if (prodotto.getImmagine() != null && prodotto.getImmagine().length > 0) {
	            String base64Image = Base64.getEncoder().encodeToString(prodotto.getImmagine());
	            response.setImmagine(base64Image);
	        } else {
	            response.setImmagine(null); 
	        }
	
	        prodottoResponseDtos.add(response);
	    }
	
	    return ResponseEntity.ok(prodottoResponseDtos);
	}    

}