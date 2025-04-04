package com.example.serraLana.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serraLana.auth.TokenService;
import com.example.serraLana.dto.AggiungiProdottoRequest;
import com.example.serraLana.model.Carrello;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.model.ProdottoCarrello;
import com.example.serraLana.model.Utente;
import com.example.serraLana.repository.CarrelloRepository;
import com.example.serraLana.repository.ProdottoCarrelloRepository;
import com.example.serraLana.repository.ProdottoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carrello")
@CrossOrigin ("*")

public class CarrelloController {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ProdottoCarrelloRepository prodottoCarrelloRepository;
    
    @Autowired
    private TokenService tokenService;


    @PostMapping("/aggiungi")
	public ResponseEntity<?> aggiungiProdottoAlCarrello(@RequestBody @Valid AggiungiProdottoRequest request, HttpServletRequest httpRequest) {
	    Optional<Utente> authUser = getAuthenticatedUser(httpRequest);
	    if (!authUser.isPresent()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
	    }
	
	    Optional<Carrello> carrelloOpt = carrelloRepository.findByUtente(authUser.get());
	    Carrello carrello = carrelloOpt.orElseGet(() -> {
	        Carrello nuovoCarrello = new Carrello();
	        nuovoCarrello.setUtente(authUser.get());
	        return carrelloRepository.save(nuovoCarrello);
	    });
	
	    Optional<Prodotto> prodottoOpt = prodottoRepository.findById(request.getProdottoId());
	    if (!prodottoOpt.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato");
	    }
	
	    Prodotto prodotto = prodottoOpt.get();
	
	    Optional<ProdottoCarrello> prodottoCarrelloOpt = prodottoCarrelloRepository.findByProdottoAndCarrello(prodotto, carrello);
	
	    if (prodottoCarrelloOpt.isPresent()) {
	        ProdottoCarrello prodottoCarrello = prodottoCarrelloOpt.get();
	        prodottoCarrello.setQuantity(prodottoCarrello.getQuantity() + request.getQuantity());
	        prodottoCarrelloRepository.save(prodottoCarrello);
	    } else {
	        ProdottoCarrello nuovoProdottoCarrello = new ProdottoCarrello();
	        nuovoProdottoCarrello.setCarrello(carrello);
	        nuovoProdottoCarrello.setProdotto(prodotto);
	        nuovoProdottoCarrello.setQuantity(request.getQuantity());
	        prodottoCarrelloRepository.save(nuovoProdottoCarrello);
	    }
	
	    return ResponseEntity.ok(Map.of("message", "Prodotto aggiunto/aggiornato nel carrello", "carrelloId", carrello.getId()));
	}

    
	@DeleteMapping("/rimuovi/{prodottoId}/{carrelloId}")
	public ResponseEntity<String> rimuoviProdottoDalCarrello(@PathVariable Long prodottoId, @PathVariable Long carrelloId, HttpServletRequest httpRequest) {
	    Optional<Utente> authUser = getAuthenticatedUser(httpRequest);
	    if (!authUser.isPresent()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
	    }

	    Optional<ProdottoCarrello> prodottoCarrelloOpt = prodottoCarrelloRepository.findByProdotto_IdAndCarrello_Id(prodottoId, carrelloId);
	    
	    if (prodottoCarrelloOpt.isPresent()) {
	        ProdottoCarrello prodottoCarrello = prodottoCarrelloOpt.get();
	        
	        if (prodottoCarrello.getQuantity() > 1) {
	            prodottoCarrello.setQuantity(prodottoCarrello.getQuantity() - 1);
	            prodottoCarrelloRepository.save(prodottoCarrello);
	        } else {
	            prodottoCarrelloRepository.delete(prodottoCarrello);
	        }
	        
	        return ResponseEntity.ok("Una unità del prodotto è stata rimossa dal carrello");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato nel carrello");
	    }
	}


    
    @GetMapping("/visualizza/{carrelloId}")
    public ResponseEntity<?> visualizzaCarrello(@PathVariable Long carrelloId, HttpServletRequest httpRequest) {
        Optional<Utente> authUser = getAuthenticatedUser(httpRequest);
        if (!authUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
        }
        Optional<Carrello> carrelloOpt = carrelloRepository.findById(carrelloId);
        if (carrelloOpt.isPresent()) {
            return ResponseEntity.ok(carrelloOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            
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
        return null;
    }
}
