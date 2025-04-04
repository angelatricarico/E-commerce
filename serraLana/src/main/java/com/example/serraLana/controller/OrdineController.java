package com.example.serraLana.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serraLana.auth.TokenService;
import com.example.serraLana.model.Carrello;
import com.example.serraLana.model.Ordine;
import com.example.serraLana.model.ProdottoCarrello;
import com.example.serraLana.model.Utente;
import com.example.serraLana.repository.CarrelloRepository;
import com.example.serraLana.repository.OrdineRepository;
import com.example.serraLana.repository.ProdottoCarrelloRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ordini")
@CrossOrigin("*")
public class OrdineController {

    @Autowired
    private final OrdineRepository ordineRepository;
    
    @Autowired
    private CarrelloRepository carrelloRepository;
    
    @Autowired
    private ProdottoCarrelloRepository prodottoCarrelloRepository;
       
    @Autowired
    private TokenService tokenService;


    public OrdineController(OrdineRepository ordineRepository, CarrelloRepository carrelloRepository,ProdottoCarrelloRepository prodottoCarrelloRepository,TokenService tokenService) {
        this.ordineRepository = ordineRepository;
        this.carrelloRepository = carrelloRepository;
        this.prodottoCarrelloRepository = prodottoCarrelloRepository;
        this.tokenService = tokenService;
    }



		@PostMapping("/crea")
		public ResponseEntity<String> creaOrdine(HttpServletRequest request) {
		    Optional<Utente> utenteOpt = getAuthenticatedUser(request);
		    if (!utenteOpt.isPresent()) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autenticato");
		    }
		
		    Utente utente = utenteOpt.get();
		
		    Optional<Carrello> carrelloOpt = carrelloRepository.findByUtente(utente);
		    if (!carrelloOpt.isPresent()) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrello non trovato");
		    }
		
		    Carrello carrello = carrelloOpt.get();
		
		    Ordine ordine = new Ordine();
		    ordine.setUtente(utente);
		
		    List<ProdottoCarrello> prodotti = new ArrayList<>();
		    for (ProdottoCarrello prodottoCarrello : carrello.getProdottoCarrello()) {
	
		        ProdottoCarrello nuovoProdotto = new ProdottoCarrello();
		        nuovoProdotto.setProdotto(prodottoCarrello.getProdotto());  
		        nuovoProdotto.setQuantity(prodottoCarrello.getQuantity());  
		        nuovoProdotto.setOrdine(ordine);  
		
		        prodotti.add(nuovoProdotto);
		    }
		
		    ordine.setProdotti(prodotti);
		
		    ordineRepository.save(ordine);
		
		    prodottoCarrelloRepository.saveAll(prodotti);
		
		    return ResponseEntity.ok("Ordine creato con successo e pagamento confermato.");
		}



		@GetMapping
		public ResponseEntity<List<Map<String, Object>>> getOrdiniUtente(HttpServletRequest request) {
		    Optional<Utente> utenteOpt = getAuthenticatedUser(request);
		    if (!utenteOpt.isPresent()) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		    }

		    List<Object[]> ordini = ordineRepository.findByUtente(utenteOpt.get());
		    System.out.println("Ordini trovati per l'utente: " + ordini.size());

		    List<Map<String, Object>> result = new ArrayList<>();
		    for (Object[] ordine : ordini) {
		        Map<String, Object> ordineMap = new HashMap<>();
		        ordineMap.put("id", ordine[0]);
		        ordineMap.put("dataCreazione", ordine[1]);
		        ordineMap.put("status", ordine[2]);
		        ordineMap.put("titoli", ordine[3]);  

		        result.add(ordineMap);
		    }

		    return ResponseEntity.ok(result);
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
