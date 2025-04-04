package com.example.serraLana.controller;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serraLana.auth.TokenService;
import com.example.serraLana.model.Carrello;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.model.ProdottoCarrello;
import com.example.serraLana.model.ProdottoLista;
import com.example.serraLana.model.Utente;
import com.example.serraLana.repository.CarrelloRepository;
import com.example.serraLana.repository.ProdottoCarrelloRepository;
import com.example.serraLana.repository.ProdottoListaRepository;
import com.example.serraLana.repository.ProdottoRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/listadesideri")
@CrossOrigin ("*")

public class ListaDesideriController {

    @Autowired
    private ProdottoListaRepository prodottoListaRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;
    
    @Autowired
    private ProdottoCarrelloRepository prodottoCarrelloRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private TokenService tokenService;

    
    
    @PostMapping("/aggiungi-desiderio/{prodottoId}")
    public ResponseEntity<String> aggiungiAllaListaDesideri(@PathVariable Long prodottoId, HttpServletRequest request) {
        Optional<Utente> authUser = getAuthenticatedUser(request);
        if (!authUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
        }

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(prodottoId);
        if (!prodottoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato");
        }

        Prodotto prodotto = prodottoOpt.get();

        Optional<ProdottoLista> prodottoListaOpt = prodottoListaRepository.findByUtenteAndProdotto(authUser.get(), prodotto);
        if (prodottoListaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Prodotto già presente nella lista desideri");
        }

        ProdottoLista prodottoLista = new ProdottoLista(authUser.get(), prodotto, 1); // Quantità impostata a 1
        prodottoListaRepository.save(prodottoLista);

        return ResponseEntity.ok("Prodotto aggiunto alla lista desideri");
    }

    
    
    @PostMapping("/aggiungi-dal-desiderio-al-carrello/{prodottoId}")
    public ResponseEntity<String> aggiungiDalDesiderioAlCarrello(@PathVariable Long prodottoId, HttpServletRequest request) {
        Optional<Utente> authUser = getAuthenticatedUser(request);
        if (authUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
        }

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(prodottoId);
        if (prodottoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato");
        }
        Prodotto prodotto = prodottoOpt.get();

        Optional<ProdottoLista> prodottoListaOpt = prodottoListaRepository.findByUtenteAndProdotto(authUser.get(), prodotto);
        if (prodottoListaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato nella lista desideri");
        }

        ProdottoLista prodottoLista = prodottoListaOpt.get();
        
        Carrello carrello = carrelloRepository.findByUtente(authUser.get())
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello();
                    nuovoCarrello.setUtente(authUser.get());
                    return carrelloRepository.save(nuovoCarrello);
                });

        Optional<ProdottoCarrello> prodottoCarrelloOpt = prodottoCarrelloRepository.findByProdottoAndCarrello(prodotto, carrello);
        if (prodottoCarrelloOpt.isPresent()) {
            ProdottoCarrello prodottoCarrello = prodottoCarrelloOpt.get();
            prodottoCarrello.setQuantity(prodottoCarrello.getQuantity() + prodottoLista.getQuantity()); 
            prodottoCarrelloRepository.save(prodottoCarrello);
        } else {
            ProdottoCarrello nuovoProdottoCarrello = new ProdottoCarrello();
            nuovoProdottoCarrello.setCarrello(carrello);
            nuovoProdottoCarrello.setProdotto(prodotto);
            nuovoProdottoCarrello.setQuantity(prodottoLista.getQuantity());
            prodottoCarrelloRepository.save(nuovoProdottoCarrello);
        }

        prodottoListaRepository.delete(prodottoLista);

        return ResponseEntity.ok("Prodotto aggiunto al carrello e rimosso dalla lista desideri");
    }


	
	
    @DeleteMapping("/rimuovi-desiderio/{prodottoId}")
    public ResponseEntity<String> rimuoviProdottoDallaListaDesideri(@PathVariable Long prodottoId, HttpServletRequest request) {
        Optional<Utente> authUser = getAuthenticatedUser(request);
        if (!authUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
        }

        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(prodottoId);
        if (!prodottoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato");
        }

        Optional<ProdottoLista> prodottoListaOpt = prodottoListaRepository.findByUtenteAndProdotto(authUser.get(), prodottoOpt.get());
        if (!prodottoListaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prodotto non trovato nella lista desideri");
        }

        ProdottoLista prodottoLista = prodottoListaOpt.get();

        if (prodottoLista.getQuantity() > 1) {
            prodottoLista.setQuantity(prodottoLista.getQuantity() - 1);
            prodottoListaRepository.save(prodottoLista);
        } else {
            prodottoListaRepository.delete(prodottoLista);
        }

        return ResponseEntity.ok("Prodotto aggiornato/rimosso dalla lista desideri");
    }




    @GetMapping("/visualizza")
    public ResponseEntity<?> visualizzaListaDesideri(HttpServletRequest request) {
        Optional<Utente> authUser = getAuthenticatedUser(request);
        if (!authUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticazione richiesta");
        }

        List<ProdottoLista> listaDesideri = prodottoListaRepository.findByUtente(authUser.get());

        if (listaDesideri.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Map<String, Object>> prodotti = listaDesideri.stream().map(prodottoLista -> {
            Map<String, Object> prodottoMap = new HashMap<>();
            Prodotto prodotto = prodottoLista.getProdotto();
            if (prodotto != null) {
                prodottoMap.put("id", prodotto.getId());
                prodottoMap.put("nome", prodotto.getTitolo());
                prodottoMap.put("prezzo", prodotto.getPrezzo());
                prodottoMap.put("quantity", prodottoLista.getQuantity());

                if (prodotto.getImmagine() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(prodotto.getImmagine());
                    prodottoMap.put("immagine", base64Image);
                } else {
                    prodottoMap.put("immagine", null);
                }
            }
            return prodottoMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(prodotti);
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
        return Optional.empty();
    }
}
